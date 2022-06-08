package com.xnfh.cec.jwt;

import cn.hutool.core.bean.BeanUtil;
import com.xnfh.cec.service.SysUserService;
import com.xnfh.cec.util.JwtUtils;
import com.xnfh.entity.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SysUserService sysUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        System.out.println("---------------");
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        int userIdInteger = Integer.parseInt(userId);
        SysUser sysUser = sysUserService.findByUserId(userIdInteger);
        if (sysUser == null) {
            throw new UnknownAccountException("账户不存在");
        }


        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(sysUser, profile);

        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(),getName());


    }
}
