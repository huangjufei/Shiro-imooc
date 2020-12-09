package com.nevercome.shiroweb.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 自定义的filter,项目中不常用
 */
public class RolesOrFilter extends AuthorizationFilter {

    protected boolean isAccessAllowed(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, Object o) throws Exception {
        Subject subjecj = getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) o;
        if(roles == null || roles.length == 0) {
            return true;
        }
        for(String role : roles) {
            if(subjecj.hasRole(role))
                return true;
        }
        return false;
    }
}
