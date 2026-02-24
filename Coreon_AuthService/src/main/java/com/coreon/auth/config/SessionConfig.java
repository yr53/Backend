package com.coreon.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();

        serializer.setCookieName("SESSION"); 
        serializer.setCookiePath("/");
        serializer.setUseHttpOnlyCookie(true);

        // 🔥 CORS 환경에서 필수
        // 세션관련 이슈 해결
        serializer.setSameSite("lax");

  
        serializer.setUseSecureCookie(false);

        return serializer;
    }
}
