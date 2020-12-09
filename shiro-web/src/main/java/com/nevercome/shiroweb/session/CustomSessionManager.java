package com.nevercome.shiroweb.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;


/**
 * 使用自定义SessionManager主要原因是,默认的会查多次redis,当前你忽略redis这点性能可以使用默认的
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);

        ServletRequest request = null;
        //WebSessionKey是shiro的类
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null)
                return session;
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
