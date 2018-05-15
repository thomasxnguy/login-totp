package com.example.auth.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;


import org.h2.server.web.WebServlet;


/**
 * Spring does not automatically add the currently logged in user (the
 * Principal) to the Model. This intercepter will do so.
 *
 * @param principal
 *            The currently logged in user (may be null)
 * @return Return the principal - will be added to the model as "principal".
 *
 */
class ParameterInterceptor implements HandlerInterceptor {
    /**
     * The post-handle method is invoked for EVERY request, after the request
     * handler (@Controller method) has been invoked and before the View is
     * processed to create the response.
     * <p>
     * The Spring Web course covers advanced Spring MVC features like this.
     * <p>
     * DETAILS: {@inheritDoc}
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        Principal principal = request.getUserPrincipal();

        if (principal != null)
            modelAndView.addObject("principal", principal);
    }
}

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Add a custom HandlerInterceptor to ensure the Principal is always added to
     * every request (if ther is one).
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParameterInterceptor());
    }

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/h2/*");
        return registrationBean;
    }

}