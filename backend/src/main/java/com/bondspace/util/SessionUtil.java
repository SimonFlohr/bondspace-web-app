package com.bondspace.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

    @Autowired
    private HttpSession httpSession;

    public Integer getLoggedInUserId() {
        return (Integer) httpSession.getAttribute("userId");
    }

    public String getLoggedInEmailAddress() {
        return (String) httpSession.getAttribute("emailAddress");
    }

    public boolean isLoggedIn() {
        return httpSession.getAttribute("userId") != null;
    }

}
