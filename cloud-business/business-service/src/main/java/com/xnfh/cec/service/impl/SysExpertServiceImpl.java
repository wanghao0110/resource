package com.xnfh.cec.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.xnfh.cec.repository.BusPlantRepository;
import com.xnfh.cec.repository.SysExpertRepository;
import com.xnfh.cec.repository.specification.ExpertSpecification;
import com.xnfh.cec.service.BusPlantService;
import com.xnfh.cec.service.SysExpertService;
import com.xnfh.cec.util.IdCardCheck;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.vo.SysExpertVo;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
@Service
@Slf4j
public class SysExpertServiceImpl implements SysExpertService {


    /**
     * 定义全局的常量值
     */
    private static final String XLS = "xls";
    /**
     * 定义全局的常量值
     */
    private static final String XLSX = "xlsx";

    @Autowired
    private SysExpertRepository sysExpertRepository;

    @Autowired
    @Lazy
    private BusPlantRepository busPlantRepository;

    @Autowired
    private BusPlantService busPlantService;


    //汉字校验
    private String regexIsHanZi = "[\\u4e00-\\u9fa5]+";

    @Override
    public Page<SysExpert> expertList(SysExpertVo sysExpertVo, Pageable pageable) {

        Page<SysExpert> sysExpertRepositoryAll = sysExpertRepository.findAll(ExpertSpecification.sysExpertSpecification(sysExpertVo),pageable);
        if(sysExpertRepositoryAll.getContent() != null){
            sysExpertRepositoryAll.getContent().forEach(sysExpert -> {

                Integer plantId = sysExpert.getPlantId();
                if(plantId!=null){
                    BusPlant busPlant = busPlantRepository.findBusPlantByPlantId(plantId);
                    if(busPlant != null){
                        sysExpert.setPlantName(busPlant.getPlantName());
                    }
                }
            });
        }
        //共计的个数
        return new PageImpl<SysExpert>(sysExpertRepositoryAll.getContent(),pageable,sysExpertRepositoryAll.getTotalElements());

    }

    /**
     * 后端管理 添加专家
     * @param sysExpert
     */
    @Override
    public void addExpert(SysExpert sysExpert) {
       log.info("sysExpert object",sysExpert);
        //校验参数
        checkSysExpertParams(sysExpert);
        //数据校验完成执行保存操作
        sysExpert.setCreateTime(new Date());
        sysExpert.setUpdateTime(new Date());
        //1 状态可用
        sysExpert.setStatus(1);
        log.info("current operate data sysExpert userName is:{},phone :{}",sysExpert.getUserName(),sysExpert.getPhone());
        sysExpertRepository.saveAndFlush(sysExpert);

    }

    private void checkSysExpertParams(SysExpert sysExpert) {
        //校验userName用户名
        if(StringUtils.isEmpty(sysExpert.getUserName())){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_USERNAME_NOT_EXIST_4037);
        }else if(sysExpert.getUserName().length() > 20){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_USERNAME_lENGTH_TO_LONG_4038);
        }else if(sysExpertRepository.getSysExpertByUserName(sysExpert.getUserName())!=null){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_USERNAME_EXIST_4039);
        }
        //校验姓名expertName
        Pattern pattern = Pattern.compile(regexIsHanZi);
        Matcher matcher = pattern.matcher(sysExpert.getExpertName());
        boolean matches = matcher.matches();
        if(StringUtils.isEmpty(sysExpert.getExpertName())){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTNAME_IS_NULL_4040);
        }else if(sysExpert.getExpertName().length() > 10){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTNAME_lENGTH_TO_LONG_4041);
        }else if(!matches){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTNAME_MUST_INPUT_HANZI_4042);
        }
        //手机号校验
        if(!StringUtils.isEmpty(sysExpert.getPhone())){
            //用户手机号码校验
            if(!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",sysExpert.getPhone())){
                throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_ERROR_4008);
            }
            SysExpert sysExpertPhoneDb = sysExpertRepository.getSysExpertByPhone(sysExpert.getPhone());
            if(sysExpertPhoneDb!=null){
                throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTPHONE_IS_EXIST_4043);
            }
        }
        //校验身份证
        String idCard = sysExpert.getIdCard();
        IdCardCheck idCardCheck = new IdCardCheck();
        boolean shenFenZhengBollean = idCardCheck.getShenFenZhengBollean(idCard);
        if(!shenFenZhengBollean){
           throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERT_IDCARD_IS_PARAM_ERROR_4045);
        }else {
            String inoNumber = idCard.substring(0, 7) + "****" + idCard.substring(11, idCard.length());
            sysExpert.setIdCard(inoNumber);
        }
        //校验密码
        String password = sysExpert.getPassword();
        if(password.length()>20){
            throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_PASSWORD_ERROR_4007);
        }else{
            //数据加密，前端两次确认
            String hashpw = BCrypt.hashpw(password,BCrypt.gensalt());
            sysExpert.setPassword(hashpw);
        }
        //检验邮箱
        if(!StringUtils.isEmpty(sysExpert.getEmail())){
            //匹配校验规则
           if(!checkEmail(sysExpert.getEmail())){
               throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERT_EMAIL_IS_PARAM_ERROR_4044);
           }
        }
    }

    /**
     * 专家的详情接口
     * @param id
     * @return
     */
    @Override
    public SysExpert getSysExpertById(Integer id) {
        log.info("current search sysExpert data id:{}",id);
        SysExpert sysExpert = sysExpertRepository.findSysExpertById(id);
        if(sysExpert != null){
            if(sysExpert.getPlantId()!=null){
                BusPlant busPlant = busPlantRepository.findBusPlantByPlantId(sysExpert.getPlantId());
                if(busPlant!=null){
                    sysExpert.setPlantName(busPlant.getPlantName());
                    return sysExpert;
                }
            }else{
                return sysExpert;
            }
        }
        return null;
    }

    /**
     * 修改专家信息
     * 只修改姓名、手机号、身份证、邮件，只对下面数据进行校验
     * @param sysExpert
     */
    @Override
    public void updateSysExpert(SysExpert sysExpert) {
        Assert.notNull(sysExpert,"current update sysExpert");
        //点击编辑之前需要查询数据是否存在
        SysExpert sysExpertDb = sysExpertRepository.findSysExpertById(sysExpert.getId());
        if(sysExpertDb == null){
            throw new ApiException(ExceptionDefinition.CURRENT_SEARCH_SYSEXPERT_DATA_DB_IS_NULL);
        }else{
            //此校验和之前不一样,需要加判断
            checkUpdateSysExpertParams(sysExpert,sysExpertDb);
            sysExpertDb.setUpdateTime(new Date());
            sysExpertRepository.saveAndFlush(sysExpertDb);
        }
    }

    /**
     * 批量删除专家信息
     * @param ids
     */
    @Override
    public void batchDelete(List<Integer> ids) {
        //先查询db
        log.info("current operate need delete ids:{}",ids);
        for (Integer id : ids) {
            SysExpert sysExpertById = sysExpertRepository.findSysExpertById(id);
            if(sysExpertById == null){
                throw new ApiException(ExceptionDefinition.CURRENT_SEARCH_SYSEXPERT_DATA_DB_IS_NULL);
            }
        }
        sysExpertRepository.deleteChoice(ids);

    }

    /**
     * 修改专家的禁用、启用状态
     * @param sysExpert
     */
    @Override
    public void updateStatus(SysExpert sysExpert) {
        //1、先查询库
        SysExpert sysExpertByIdDb = sysExpertRepository.findSysExpertById(sysExpert.getId());
        if(sysExpertByIdDb == null){
            throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTNAME_IS_NULL_4040);
        }else {
            sysExpertByIdDb.setStatus(sysExpert.getStatus());
            sysExpertRepository.saveAndFlush(sysExpertByIdDb);
        }
    }

    /**
     * 专家管理批量导入接口
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> importExcel(MultipartFile file) {
        List<SysExpert> tblFixChangeList = new ArrayList<SysExpert>();
        Map<String, Object> map = new HashMap<>();

        Workbook workbook = null;

        String filename = file.getOriginalFilename();
        try {
            if (filename != null) {
                if (filename.endsWith(XLS)) {
                    // 2003
                    workbook = new HSSFWorkbook(file.getInputStream());
                } else if (filename.endsWith(XLSX)) {
                    // 2007
                    workbook = new XSSFWorkbook(file.getInputStream());
                } else {
                    throw new Exception("文件不是Excel文件");
                }
            } else {
                log.error("文件为空");
            }
            // 获取sheet
            Sheet sheet = workbook.getSheet("Sheet1");

            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum == 0) {
                throw new Exception("请填写行数");
            }

            for (int i = 1; i < lastRowNum + 1; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    // 读取cell单元格内容
                    SysExpert tblFixChange = new SysExpert();

                    Integer id = Integer.valueOf(getCellValue(row.getCell(0)));
                    tblFixChange.setId(id);

                    String userName = getCellValue(row.getCell(1));
                    tblFixChange.setUserName(userName);

                    String expertName = getCellValue(row.getCell(2));
                    tblFixChange.setExpertName(expertName);

                    String phone =getCellValue(row.getCell(3));
                    tblFixChange.setPhone(phone);

                    String idCard = getCellValue(row.getCell(4));
                    tblFixChange.setIdCard(idCard);

                    String email = getCellValue(row.getCell(4));
                    tblFixChange.setEmail(email);

                    String password = getCellValue(row.getCell(5));
                    tblFixChange.setPassword(password);

                    String create = getCellValue(row.getCell(6));
                    Date created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create+" 00:00:00");
                    tblFixChange.setCreateTime(created);

                    String cellValue = getCellValue(row.getCell(7));
                    Date update = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cellValue+" 00:00:00");
                    tblFixChange.setUpdateTime(update);

                    Integer status = Integer.valueOf(getCellValue(row.getCell(8)));
                    tblFixChange.setStatus(status);

                    Integer plantId = Integer.valueOf(getCellValue(row.getCell(9)));
                    tblFixChange.setPlantId(plantId);

                    tblFixChangeList.add(tblFixChange);
                }
            }
            // 批量插入
            sysExpertRepository.saveAll(tblFixChangeList);
            map.put("status", 1);
            map.put("data", "导入数据成功");
        } catch (Exception e) {
            map.put("status", -1);
            map.put("data", "导入数据异常");
            log.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 获取每个单元格内容
     *
     * @param cell cell
     * @return String
     */
    private String getCellValue(Cell cell) {
        // 单元格内容
        String value = "";
        if (cell != null) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                // 数字
                case HSSFCell
                        .CELL_TYPE_NUMERIC:
                    value = cell.getNumericCellValue() + "";
                    // 判断cell是不是日期类型的
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 获取日期类型的单元格
                        Date date = cell.getDateCellValue();
                        if (null != date) {
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            value = "";
                        }
                    } else {
                        value = new DecimalFormat("0").format(cell.getNumericCellValue());
                    }
                    break;
                // 字符串
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                // Boolean
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue() + "";
                    break;
                // 公式
                case HSSFCell.CELL_TYPE_FORMULA:
                    value = cell.getCellFormula() + "";
                    break;
                // 空格
                case HSSFCell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                // 错误
                case HSSFCell.CELL_TYPE_ERROR:
                    value = "非法字符";
                default:
                    value = "未知类型";
                    break;
            }
        }
        return value.trim();
    }

    /**
     * 修改专家时的校验方法
     * @param sysExpert
     * @param sysExpertDb
     */
    private void checkUpdateSysExpertParams(SysExpert sysExpert,SysExpert sysExpertDb) {
        //用户名不可修改userName
        //校验姓名expertName
        if(!sysExpertDb.getExpertName().equals(sysExpert.getExpertName())){
            Pattern pattern = Pattern.compile(regexIsHanZi);
            Matcher matcher = pattern.matcher(sysExpert.getExpertName());
            boolean matches = matcher.matches();
            if(StringUtils.isEmpty(sysExpert.getExpertName())){
                throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTNAME_IS_NULL_4040);
            }else if(sysExpert.getExpertName().length() > 10){
                throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTNAME_lENGTH_TO_LONG_4041);
            }else if(!matches){
                throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTNAME_MUST_INPUT_HANZI_4042);
            }
        }
        //手机号校验
        if(!sysExpert.getPhone().equals(sysExpertDb.getPhone())){
            if(!StringUtils.isEmpty(sysExpert.getPhone())){
                //用户手机号码校验
                if(!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",sysExpert.getPhone())){
                    throw new ApiException(ExceptionDefinition.CURRENT_CHECK_PARAMS_ERROR_4008);
                }
                SysExpert sysExpertPhoneDb = sysExpertRepository.getSysExpertByPhone(sysExpert.getPhone());
                if(sysExpertPhoneDb!=null){
                    throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERTPHONE_IS_EXIST_4043);
                }
            }
        }
        //校验身份证号
        if(!sysExpert.getIdCard().equals(sysExpertDb.getIdCard())){
            String idCard = sysExpert.getIdCard();
            IdCardCheck idCardCheck = new IdCardCheck();
            boolean shenFenZhengBollean = idCardCheck.getShenFenZhengBollean(idCard);
            if(!shenFenZhengBollean){
                throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERT_IDCARD_IS_PARAM_ERROR_4045);
            }else {
                String inoNumber = idCard.substring(0, 7) + "****" + idCard.substring(11, idCard.length());
                sysExpert.setIdCard(inoNumber);
            }
        }
        //校验邮箱
        if(!sysExpert.getEmail().equals(sysExpertDb.getEmail())){
            if(!StringUtils.isEmpty(sysExpert.getEmail())){
                //匹配校验规则
                if(!checkEmail(sysExpert.getEmail())){
                    throw new ApiException(ExceptionDefinition.CURRENT_OPERATE_DATA_EXPERT_EMAIL_IS_PARAM_ERROR_4044);
                }
            }
        }
    }

    /**
     * 校验邮箱
     * @param email
     */
    private boolean checkEmail(String email) {
        int k = 0;
        //判断是否有仅有一个@且不能在开头或结尾
        if(email.indexOf("@") > 0 && email.indexOf('@') == email.lastIndexOf('@') && email.indexOf('@') < email.length()-1) {
            k++;
        }
        //判断"@"之后必须有"."且不能紧跟
        if(email.indexOf('.',email.indexOf('@')) > email.indexOf('@')+1 ) {
            k++;
        }
        //判断"@"之前或之后不能紧跟"."
        if(email.indexOf('.') < email.indexOf('@')-1 || email.indexOf('.') > email.indexOf('@')+1 ) {
            k++;
        }
        //@之前要有6个字符
        if(email.indexOf('@') > 5 ) {
            k++;
        }

        if(email.endsWith("com") || email.endsWith("org") || email.endsWith("cn") ||email.endsWith("net")) {
            k++;
        }
        Pattern pattern = Pattern.compile("[0-9]+");
        String emailString = email.substring(0, email.indexOf('@'));
        if(pattern.matcher(emailString).matches()){
            k++;
        }
        if(k == 6) {
            return true;
        }
        return false;
    }
}
