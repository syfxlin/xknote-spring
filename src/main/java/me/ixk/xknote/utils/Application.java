package me.ixk.xknote.utils;

import me.ixk.xknote.security.UserDetailsImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Application implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        synchronized (this) {
            if (Application.applicationContext == null) {
                Application.applicationContext = applicationContext;
            }
        }
    }

    public static ApplicationContext get() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String qualifier, Class<T> clazz) {
        return applicationContext.getBean(qualifier, clazz);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserDetailsImpl getCurrentUser() {
        Object details = getAuthentication().getPrincipal();
        if ("anonymousUser".equals(details)) {
            return null;
        }
        return (UserDetailsImpl) details;
    }

    public static Long getCurrentUserId() {
        UserDetailsImpl userDetails = getCurrentUser();
        if (userDetails == null) {
            return null;
        }
        return userDetails.getId();
    }
}
