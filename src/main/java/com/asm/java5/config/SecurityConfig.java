package com.asm.java5.config;


import com.asm.java5.constant.SessionAttr;
import com.asm.java5.enums.Role;
import com.asm.java5.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    CookieService cookieService;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                "/assets/**", "/register","/customer/error","/index/**",
                "/forgot-pass","/search/**","/page-faq",
                "/page-contact","/rest/home/**"
                )
                .permitAll()
//                .requestMatchers("/cart/**").hasAuthority(Role.CUSTOMER.toString())
//                .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.toString())
                .anyRequest()
                .authenticated()
               )
            .formLogin(login -> login
                    .loginPage("/login")
                    .defaultSuccessUrl("/index", false)
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .failureHandler((request, response, exception) -> {
                        cookieService.exception("Sai tài khoản hoặc mật khẩu", request, response);
                    }).permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .defaultSuccessUrl("/oauth2", true)
                    .failureHandler((request, response, exception) -> {
                        exception.printStackTrace();
                    })
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/auth/logoff")
                    .invalidateHttpSession(true)
                    .deleteCookies(SessionAttr.CUSTOMER)
                    .logoutSuccessUrl("/login")
                    .permitAll());
        return http.build();
   }
}