package christian.skillfactory.registro.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import christian.skillfactory.registro.model.Account;
import christian.skillfactory.registro.service.AccountService;


/**
 * Configuration class for Spring Security.
 * Defines access control rules, login/logout behavior, and password encoding.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	private final AccountService account;
	
	public SecurityConfig(AccountService account)
	{
		this.account=account;
	}
	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth 
	            .requestMatchers("/studente/**").hasRole("ADMIN")
	            .requestMatchers("/registro/**").hasRole("ADMIN")
	            .requestMatchers("/titolo/**").hasRole("ADMIN")
	            .requestMatchers("/admin/home**").hasRole("ADMIN")
	            .requestMatchers("/professore/**").hasRole("ADMIN")
	            .requestMatchers("/userStudente/**").hasRole("STUDENTE")
	            .requestMatchers("/userProfessore/**").hasRole("PROF")
	            .anyRequest().authenticated()
	    )
	    .formLogin(form -> form
	            .loginPage("/auth/login")
	            .loginProcessingUrl("/auth/login")       
	            .successHandler(customAuthenticationSuccessHandler())
	            .failureUrl("/auth/login?error")       
	            .permitAll()
	    )
	    .logout(logout -> logout
	    	    .logoutUrl("/auth/logout")          
	    	    .logoutSuccessUrl("/auth/login")    
	    	    .invalidateHttpSession(true)           
	    	    .deleteCookies("JSESSIONID")           
	    	    .permitAll()
	    	);


	    return http.build();
	}
	@Bean
	PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	/**
     * Custom handler to redirect users to their specific dashboard based on their role 
     * after a successful login.
     */
    @Bean
    AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            Account account = (Account) authentication.getPrincipal();
            String ruolo = account.getRuolo().getNome(); 
            if ("ROLE_ADMIN".equals(ruolo)) {
                response.sendRedirect("/admin/home");
            } else if ("ROLE_PROF".equals(ruolo)) {
                response.sendRedirect("/userProfessore");
            } else if ("ROLE_STUDENTE".equals(ruolo)) {
                response.sendRedirect("/userStudente");
            } else {
                response.sendRedirect("/auth/login?error");
            }
        };
    }
  
}
