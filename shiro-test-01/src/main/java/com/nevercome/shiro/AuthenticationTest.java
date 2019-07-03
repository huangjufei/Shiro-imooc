package com.nevercome.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;


/**
 * step 1
 * helloWorld
 */
public class AuthenticationTest {

    private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    //模拟数据源
    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("sun", "123", "admin", "user");
    }

    @Test
    public void test() {
        //1 构建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //2,将数据源(可以是数据库,缓存,或文件,这里用的是模拟缓存)
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //2,从静态类得到subject
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        //3,对用户登录的信息进行封装
        UsernamePasswordToken token = new UsernamePasswordToken("sun", "123");
        subject.login(token);

        //对用户认证信息进行比对(登录的账号密码)
        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        //下面是授权信息
        subject.checkRoles("admin", "user");
    }

}
