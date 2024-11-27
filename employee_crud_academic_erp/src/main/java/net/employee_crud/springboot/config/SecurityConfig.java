package net.employee_crud.springboot.config;

import lombok.RequiredArgsConstructor;
import net.employee_crud.springboot.helper.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Exclude login and registration endpoints from the interceptor
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/api/v1/employee/**")  // Apply to all employee routes
                .excludePathPatterns("/api/v1/employee/login", "/api/v1/employee/register");  // Exclude login and register paths
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
