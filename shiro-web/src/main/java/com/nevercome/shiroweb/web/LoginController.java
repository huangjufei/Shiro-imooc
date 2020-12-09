package com.nevercome.shiroweb.web;

import com.nevercome.shiroweb.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class LoginController {


    //produces 解决页面乱码问题,subLogin路径和页面login.html页面的请求路径一致
    @RequestMapping(value = "/subLogin", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String login(User user) {
        //主体提交请求进行验证
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            //设置rememberMe的作用就是无需要要登录就可以访问,这里是一个开关,是否启动是用户是否选择
            token.setRememberMe(user.isRememberMe());
            /**
             * token 代码当前登录用户信息,subject有数据库中的用户信息,判断逻辑核心就这里
             */
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }
        //上面是认证,下面是授权
        String res = "";
        if (subject.hasRole("admin")) {
            res = "admin";
        } else {
            res = "no admin";
        }
        if (subject.isPermitted("admin:update")) {
            res += " admin:update";
        } else {
            res += " no admin:update";
        }
        return res;
    }

    /**
     * 通过注解来控制权限(角色)
     *
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    /**
     * 通过注解来控制权限(资源)推荐资源控制
     *
     * @return
     */
    @RequiresPermissions("admin:update")
    @RequestMapping(value = "/testAdmin", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testAdmin() {
        return "admin:update success";
    }


    @RequiresPermissions("admin2")
    @RequestMapping(value = "/testAdmin2", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String testAdmin2() {
        return "没有被放过的资源权限";
    }


}
