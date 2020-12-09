package com.nevercome.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * 第1天
 *shiro的helloWorld
 * 模拟用户登录账号和密码和数据库进行对比(这里是缓存)
 * 包含认证(账号密码)和授权(角色权限)
 * 请运行下面代码,并跟踪断点
 *
 * 抽象:Subject中既有数据库的信息,也有用户登录的信息,两边数据都会集中到这里,就可以比对了
 */
public class AuthenticationTest {

    private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
    //模拟数据库获取的信息
    @Before
    public void addUser() {
        //账号,密码,多个角色
        simpleAccountRealm.addAccount("huangjufei", "123", "admin", "user");
    }
    @Test
    public void test() {
        //1 创建securityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //可以是数据库,缓存,或文件,这里用的是模拟缓存
        defaultSecurityManager.setRealm(simpleAccountRealm);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //2,从静态类得到subject
        Subject subject = SecurityUtils.getSubject();
        //3,模拟用户登录的信息
        UsernamePasswordToken token = new UsernamePasswordToken("huangjufei", "123");

        /**
         * token 代码当前登录用户信息,subject有数据库中的用户信息,最好去test4跟踪断点
         */
        subject.login(token);//登录提交,这里打断点,这里就是判断逻辑的地方
        //对用户认证信息进行比对(数据库和登录的的账号密码进行对比)
        System.out.println("是否认证通过(isAuthenticated): " + subject.isAuthenticated());
        //授权检查,多个需要全部通过才通过,和SpringSecurity框架思想差不多
        subject.checkRoles("admin", "user1");
    }
}