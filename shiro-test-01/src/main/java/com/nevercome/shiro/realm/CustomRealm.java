package com.nevercome.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 自定义 CustomRealm 需要继承 AuthorizingRealm
 * <p>
 * 主要就是重写两个方法:一个认证,一个授权;它们没有必然联系
 */
public class CustomRealm extends AuthorizingRealm {

    Map<String, String> userMap = new HashMap();//模拟用户表
    Set<String> roles = new HashSet<String>();//模拟角色表
    Set<String> permissions = new HashSet<String>();//模拟权限表

    {
        //模拟数据源(开发这里来至数据库)
        userMap.put("sun", "1d7b217127d82ea1eac7e3b92090a463");
        roles.add("admin");
        roles.add("user");
        permissions.add("user:add");
        permissions.add("admin:add");
        super.setName("customRealm");
    }


    /**
     * Authentication
     * 用户认证(效验账号密码)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1. 从主体获取提交的认证信息
        String username = (String) authenticationToken.getPrincipal();
        //  从数据库验证(模拟)
        String password = getPasswordByUsername(username);
        if (password == null) {
            return null;
        }
        //封装返回对象AuthenticationInfo
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        //设置证书加盐的字符串必须和数据库的一致
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("zaq"));
        return simpleAuthenticationInfo;
    }


    /**
     * Authorization
     * 用户授权认证(访问权限)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String) principalCollection.getPrimaryPrincipal();
        //模拟从数据库获取角色和permission信息
        Set<String> roles = getRolesByUsername(username);
        Set<String> permissions = getRolesByPermissions(username);
        //封装返回对象AuthorizationInfo
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    //本来该从数据库中去获取,现在直接从缓存中返回
    private Set<String> getRolesByPermissions(String username) {
        return permissions;
    }
    //本来该从数据库中去获取,现在直接从缓存中返回
    private Set<String> getRolesByUsername(String username) {
        return roles;
    }
    //本来该从数据库中去获取,现在直接从缓存中返回
    private String getPasswordByUsername(String username) {
        return userMap.get(username);
    }


    /**
     * 测试密码通过md5加盐方式加密
     */
    public static void main(String args[]) {
        // Md5Hash md5Hash = new Md5Hash("123");
        Md5Hash md5Hash = new Md5Hash("123", "zaq");  //密码和盐
        System.out.println(md5Hash.toString());
    }
}
