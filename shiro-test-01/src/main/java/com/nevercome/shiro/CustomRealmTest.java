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
 * step 4
 * 自定义
 * 其实 JdbcRealm 也是继承 AuthorizingRealm
 * public class JdbcRealm extends AuthorizingRealm
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
        matcher.setHashIterations(1);
        matcher.setHashAlgorithmName("md5");
        customRealm.setCredentialsMatcher(matcher);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("sun", "123");
        subject.login(token);
        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        subject.checkRoles("admin", "user");
        subject.checkPermissions("user:add", "admin:add");
    }
}
