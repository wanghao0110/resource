package com.xnfh.cec.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.xnfh.cec.repository.SysUserRepository;
import com.xnfh.cec.service.SysUserService;
import com.xnfh.cec.util.IdCardCheck;
import com.xnfh.cec.util.UserRegCheck;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.dto.LoginDto;

import com.xnfh.entity.vo.SysUserVo;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.Query;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/18
 */

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {


    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public SysUser findByUserId(int userId) {
        return sysUserRepository.getOne(userId);
    }

    /**
     * 注册用户
     * @param sysUser
     * @return
     */
    @Override
    public SysUser addUser(SysUser sysUser) {
        Integer status = sysUser.getStatus();
        if(status == null){
            throw new ApiException(ExceptionDefinition.CURRENT_CHOICE_STATUS_NOT_EXIST_4004);
        }
        switch (status){
            case 0:
                //单位注册
                registerUserInCompany(sysUser);
                break;
            case 1:
                //个人注册
                registerUserInPersonal(sysUser);
                break;
            //status状态错误
            default:
                break;
        }
        return sysUser;
    }

    /**
     * 执行登录
     * @param loginDto
     * @return
     */
    //TODO登录的时候需要判断当时审核状态，如果状态是0 表示审核不通过
    @Override
    public SysUser loadUsernameAndPassword(LoginDto loginDto) {
        Integer loginType = loginDto.getLoginType();
        SysUser sysUser = null;
        switch (loginType){
            case 0:
                //账号密码登录
                if(!StringUtils.isEmpty(loginDto.getAccountName()) && !StringUtils.isEmpty(loginDto.getPassword())){
                    String password = loginDto.getPassword();
                    sysUser =  sysUserRepository.getSysUserByAccountNameAndPassword(loginDto.getAccountName());
                    if(sysUser != null){
                        boolean flag = BCrypt.checkpw(password, BCrypt.hashpw(password, BCrypt.gensalt()));
                        if(flag){
                            return sysUser;
                        }else{
                            throw new ApiException(ExceptionDefinition.CURRENT_PASSWORD_ERROR_4010);
                        }
                    }
                }
                break;
            case 1:
                //手机号登陆
                String phoneNumber = loginDto.getPhoneNumber();
                if(!StringUtils.isEmpty(phoneNumber)){
                    //用户手机号码校验
                    if(!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",sysUser.getContactPhone())){
                        throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_ERROR_4008);
                    }
                    //校验验证码,，判断输入和缓存的验证码是否一致，如果正确，则登录成功，否则，重新输入
                    Integer code = loginDto.getCode();
                    String codeRedis = redisTemplate.opsForValue().get("v1.xnfh" + loginDto.getPhoneNumber());
                    if(!StringUtils.isEmpty(codeRedis)){
                        int codeRedisInt = Integer.parseInt(codeRedis);
                        if(code != codeRedisInt){
                            throw new ApiException(ExceptionDefinition.CURRENT_CODE_INPUT_ERROR_4011);
                        }else{
                            sysUser =  sysUserRepository.getSysUserByAccountNameAndPassword(loginDto.getPhoneNumber());
                            if(sysUser != null){
                                return sysUser;
                            } else{
                                throw new ApiException(ExceptionDefinition.CURRENT_PASSWORD_ERROR_4010);
                            }
                        }

                    }else{
                        return null;
                    }
                }
                break;
            default:
                break;
        }
        return sysUser;
    }

    @Override
    public void sendMsg(Map<String, String> sendMap) {
        String phoneNum = sendMap.get("phone");
        //生成验证码
        String code = createCode();
        //放到redis里面 //设置过期时间 5min
        redisTemplate.opsForValue().set(phoneNum,code, Duration.ofMinutes(5));
        //组装一个参数 用mq去做
//        AliSmsModel aliSmsModel = new AliSmsModel();
//        aliSmsModel.setPhoneNumbers(phonenum);
//        aliSmsModel.setSignName("尚学堂教育");
//        aliSmsModel.setTemplateCode("SMS_184212012");
//        aliSmsModel.setTemplateParam("8888");
    }

    /**
     * 查询用户端的用户信息
     * @param sysUser
     * @return
     */
    @Override
    public SysUser viewUserMessage(SysUser sysUser) {
        log.info("current search sysUser accountName:{}",sysUser.getAccountName());
        //从token中获取当前用户信息
        //根据用户名称进行查询
        String accountName = sysUser.getAccountName();
        SysUser sysUserDb = sysUserRepository.findSysUserByAccountName(accountName);
        SysUser sysUSerNew = null;
        if(sysUserDb == null){
            throw new ApiException(ExceptionDefinition.GET_SYSUSER_SEARCH_DB_NOT_EXIST_4030);
        }else{
            //修改用户信息，账号是不允许修改的，修改完成后需要重新登录
            //用户密码、联系电话、住址、emai
            sysUSerNew = new SysUser();
            if(!StringUtils.isEmpty(sysUser.getContactPhone())){
                //用户手机号码校验
                if(!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",sysUser.getContactPhone())){
                    throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_ERROR_4008);
                }
            }
            sysUSerNew.setContactPhone(sysUser.getContactPhone());
            //校验邮箱
            if(!StringUtils.isEmpty(sysUser.getEmail())){
                if (sysUser.getEmail().indexOf('@') != 1 && sysUser.getEmail().indexOf('.') > sysUser.getEmail().indexOf('@')) {
                    sysUSerNew.setEmail(sysUser.getEmail());
                }else{
                    throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_ERROR_4008);
                }
            }
            //校验密码
            if(sysUser.getPassword()!=null){
                //密码加密
                String password = sysUser.getPassword();
                if(password.length()>10){
                    throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_PASSWORD_ERROR_4007);
                }else{
                    //数据加密，前端两次确认 返回登录
                    String hashpw = BCrypt.hashpw(password,BCrypt.gensalt());
                    if(!hashpw.equals(sysUserDb.getPassword())){
                        log.info("current operate sysuser password not change.");
                        sysUSerNew.setPassword(hashpw);
                    }
                }

            }
        }
        return  sysUserRepository.saveAndFlush(sysUSerNew);
    }

    /**
     * 生成验证码
     * @return
     */
    private String createCode() {
        return "8888";
    }


    //个人注册
    private void registerUserInPersonal(SysUser sysUser) {
        validationParam(sysUser);
    }

    //单位注册
    private void registerUserInCompany(SysUser sysUser) {
        validationParam(sysUser);
    }

    private void validationParam(SysUser sysUser) {
        UserRegCheck userRegCheck = new UserRegCheck();
        //姓名校验
        if(StringUtils.isEmpty(sysUser.getTrueName()) || sysUser.getTrueName().length()>10){
            throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_TRUENAME_ERROR_4005);
        }else{
            boolean checkTrueName = userRegCheck.validateUserName(sysUser.getTrueName());
            if(!checkTrueName){
               return;
            }
        }
        //身份证校验
        String idCard = sysUser.getIdCard();
        IdCardCheck idCardCheck = new IdCardCheck();
        boolean shenFenZhengBollean = idCardCheck.getShenFenZhengBollean(idCard);
        if(!shenFenZhengBollean){
            return;
        }else {
            String inoNumber = idCard.substring(0, 7) + "****" + idCard.substring(11, idCard.length());
            sysUser.setIdCard(inoNumber);
        }
        if(StringUtils.isEmpty(sysUser.getAccountName()) || sysUser.getAccountName().length() > 6){
            throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_ACCOUNTNAME_ERROR_4006);
        }
        //密码加密
        String password = sysUser.getPassword();
        if(password.length()>10){
            throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_PASSWORD_ERROR_4007);
        }else{
            //数据加密，前端两次确认
            String hashpw = BCrypt.hashpw(password,BCrypt.gensalt());
            sysUser.setPassword(hashpw);
        }
        //此处只校验11为电话，不校验区话
        if(!StringUtils.isEmpty(sysUser.getContactPhone())){
            //用户手机号码校验
            if(!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",sysUser.getContactPhone())){
                throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_ERROR_4008);
            }
        }
        //校验企业名称
        if(StringUtils.isEmpty(sysUser.getBusinessName())){
            throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_BUSINESS_ERROR_4009);
        }
        //校验法人
        if(!StringUtils.isEmpty(sysUser.getLegalPerson())){
            userRegCheck.validateUserName(sysUser.getLegalPerson());
        }

        //校验邮箱TODO
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(new Date());
        sysUser.setAuditStatus(0);
        //默认为0
        sysUserRepository.saveAndFlush(sysUser);
        log.info("current insert user id :{},sysUser:{}",sysUser.getUserId(),sysUser);
    }
}
