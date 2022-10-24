package fr.gvs.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@PropertySource({"classpath:application-${gvs.environment:dev}.properties"})
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Value("${gvs.environment}")
    private String environment;

    @Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        switch(environment)
        {
        case "dev":
            http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/logout").permitAll()
            .antMatchers("/api/user/current").permitAll()
            .antMatchers("/swagger/**").permitAll()
            .antMatchers("/api/**").authenticated()
            .anyRequest().permitAll();
            break;
        default:
        case "prod":
            http
            .csrf().disable()
            .requiresChannel()
            .anyRequest().requiresSecure()
            .and()
            .authorizeRequests()
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/logout").permitAll()
            .antMatchers("/api/user/current").permitAll()
            .antMatchers("/api/**").authenticated()
            .anyRequest().permitAll();
            break;
        } 
    }
}