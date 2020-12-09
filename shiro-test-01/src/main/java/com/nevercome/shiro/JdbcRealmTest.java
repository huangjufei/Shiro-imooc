package com.nevercome.shiro;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;


/**
 * 第3天
 * realm
 * JdbcRealm : 数据库作为数据源(需要创建数据库test.sql)
 * jdbcRealm 可以写基础的查询语句,jdbcRealm类中有默认的sql语句,当我们创建的表字段和默认sql的字段不一致时
 * 就需要自己重写sql
 * 这里看不懂时,请看第一天
 */
public class JdbcRealmTest {


    private DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
        dataSource.setUsername("root");
        dataSource.setPassword("hjf,.123");
    }

    private JdbcRealm jdbcRealm = new JdbcRealm();

    @Test
    public void test() {
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

        jdbcRealm.setDataSource(dataSource);
        defaultSecurityManager.setRealm(jdbcRealm);
        //默认为false,默认不会查询资源url,只有设置为true才会去查询资源url
        jdbcRealm.setPermissionsLookupEnabled(true);

        String userSql = "select password from users where username = ?";
        jdbcRealm.setAuthenticationQuery(userSql);//jdbcRealm,设置密码

        String roleSql = "select role_name from test_user_roles where username = ?";
        jdbcRealm.setUserRolesQuery(roleSql);//jdbcRealm,设置角色

        String permSql = "select permission from test_roles_permissions where role_name = ?";
        jdbcRealm.setPermissionsQuery(permSql);//jdbcRealm,设置资源

        //设置SecurityManager
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        /**
         * token 代码当前登录用户信息,subject有数据库中的用户信息
         */
        UsernamePasswordToken token = new UsernamePasswordToken("sun", "123");
        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        subject.checkRoles("admin", "user");
        subject.checkPermissions("user:add", "admin:update");
    }

}
