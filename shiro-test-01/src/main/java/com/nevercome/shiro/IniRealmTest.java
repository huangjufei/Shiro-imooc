package com.nevercome.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;


/**
 * 第2天
 * realm
 * IniRealm :通过文件的方式进行测试
 */
public class IniRealmTest {


    private IniRealm iniRealm = new IniRealm("classpath:user.ini");

    @Test
    public void test() {
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        defaultSecurityManager.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("huangjufei", "1234");

        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        subject.checkRoles("admin", "user");
        //可以发现subject可以,校验认证(账号,密码),授权(角色,资源)
        subject.checkPermission("user:delete");
        subject.checkPermission("a:c");
        subject.checkPermission("a");

    }
}
