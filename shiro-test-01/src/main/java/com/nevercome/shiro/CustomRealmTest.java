package com.nevercome.shiro;

import com.nevercome.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;


/**
 * 第4天
 * 自定义realm
 * 模仿 JdbcRealm 也是继承 AuthorizingRealm(public class CustomRealm extends AuthorizingRealm)
 * 这里看不懂时请看第3天
 *
 *
 *   subject.login(token)核心对比流程分析:
 *   请在AuthenticatingRealm类的getAuthenticationInfo()方法上打一个断点,这里会调用我们自定义的CustomRealm
 *   的doGetAuthenticationInfo(),从而得到数据库的用户信息
 */
public class CustomRealmTest {

    @Test
    public void test() {
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 自定义realm
        CustomRealm customRealm = new CustomRealm();
        defaultSecurityManager.setRealm(customRealm);
        //这里和加密相关
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashIterations(1);//加密次数
        matcher.setHashAlgorithmName("md5");
        customRealm.setCredentialsMatcher(matcher);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("sun", "123");
        subject.login(token);//核心点,流程解释请看上面
        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");
        subject.checkPermissions("user:add", "admin:add");
    }
}
