package com.sip.ams.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {


    private final DataSource dataSource;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.queries.users-query}")
    private String usersQuery;
    
	@Value("${spring.queries.roles-query}")
    private String rolesQuery;

	
    @Autowired
    public SecurityConfiguration(DataSource dataSource, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.dataSource = dataSource;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .passwordEncoder(bCryptPasswordEncoder);
    }
    
    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http

              .csrf(csrf -> csrf.disable())
              .authorizeHttpRequests(auth -> auth
                      .requestMatchers("/login","/registration","/roles/**","/accounts/**").permitAll()
                      .requestMatchers("/provider/**").hasAnyAuthority("ADMIN", "SUPERADMIN")
                      .requestMatchers("/article/**").hasAnyAuthority("USER", "SUPERADMIN")

                      // .requestMatchers("/providers/**").hasAuthority("ADMIN")
                      //.requestMatchers("/articles/**").hasAuthority("USER")
                      .anyRequest().authenticated()
                      )

              .formLogin(formLogin -> formLogin
                      .loginPage("/login")
                      .failureUrl("/login?error=true")
                      .defaultSuccessUrl("/home",true) // page d'accueil après login avec succès
                      .usernameParameter("email") // paramètres d'authentifications login et password
                      .passwordParameter("password")
                      )

             .logout(logout -> logout
                      .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // route de deconnexion ici /logut
                      .logoutSuccessUrl("/login")
                  );
             return http.build();
    }
    
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/images/**");
    }
    
 // laisser l'accès aux ressources
   /* @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }*/




  /*
	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		  http.csrf().disable().authorizeRequests()
    	  .requestMatchers("/providers/**").hasAuthority("ADMIN")
          .requestMatchers("/articles/**").hasAuthority("USER")
          .requestMatchers("/role/**").permitAll()
          .requestMatchers("/accounts/**").permitAll()
          .and().formLogin()
          .loginPage("/login")
          .failureUrl("/login?error=true")
          .defaultSuccessUrl("/home") // page d'accueil après login avec succès
          .usernameParameter("email") // paramètres d'authentifications login et password
          .passwordParameter("password")
          .and().logout()
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // route de deconnexion ici /logut
          .logoutSuccessUrl("/login");
    	  return http.build();
    }*/


}
