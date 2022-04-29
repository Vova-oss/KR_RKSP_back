package com.example.demo.security;

import com.example.demo.categories.domain.jwt_token.JwtTokenInputBoundary;
import com.example.demo.categories.domain.refresh_token.RefreshTokenInputBoundary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {


    @Autowired
    RefreshTokenInputBoundary refreshTokenInputBoundary;
    @Autowired
    JwtTokenInputBoundary jwtTokenInputBoundary;

    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .cors()
                .and()
                .csrf().disable().authorizeRequests()

                .antMatchers(HttpMethod.DELETE, "/user/deleteByTelephoneNumber").hasAuthority("ADMIN")
                .antMatchers( "/user/checkRole").hasAnyAuthority("USER", "ADMIN")
                .antMatchers( "/user/checkPassword/*").hasAuthority("USER")
                .antMatchers( "/user/changeGender/*").hasAuthority("USER")
                .antMatchers( "/user/changeFIO/*").hasAuthority("USER")
                .antMatchers( "/user/getInfoAboutUser").hasAuthority("USER")
                .antMatchers("/user/**").permitAll()

                .antMatchers("/type/getAll").permitAll()
                .antMatchers("/type/**").hasAuthority("ADMIN")

                .antMatchers("/brand/**").hasAuthority("ADMIN")

                .antMatchers("/device/getByParams").permitAll()
                .antMatchers("/device/getById/**").permitAll()
                .antMatchers("/device/getTopDevices").permitAll()
                .antMatchers("/device/**").hasAuthority("ADMIN")

                .antMatchers("/order/add").hasAuthority("USER")
                .antMatchers("/order/getAllByUser").hasAuthority("USER")
                .antMatchers("/order/changeStatusOfOrder").hasAuthority("ADMIN")
                .antMatchers("/order/getAll").hasAuthority("ADMIN")

                .antMatchers("/refreshToken").permitAll()

                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), refreshTokenInputBoundary, jwtTokenInputBoundary))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtTokenInputBoundary))


                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource(){
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*");
    }


}
