package com.example.centeroauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.annotation.Resource;

/**
 * @author wangbin
 */
@SpringBootApplication
public class CenterOauthApplication {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Configuration
  @EnableAuthorizationServer
  class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients
              .inMemory()
              .withClient("client")
              .secret("$2a$10$BAOaZr9GEFB5tbq9XZFoVukuVOZk7kfPeXUyPaAuF5YysP9iZpmba")
              .authorizedGrantTypes("authorization_code", "password")
              .scopes("app")
              .redirectUris("http://www.baidu.com")
      ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
      security.checkTokenAccess("isAuthenticated()").tokenKeyAccess("permitAll()");
    }

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
      endpoints.authenticationManager(authenticationManager);
    }
  }

  @Configuration
  class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
              .authorizeRequests().anyRequest().authenticated()
              .and().formLogin().permitAll()
              .and().logout().permitAll()
      ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication()
              .withUser("user").password("$2a$10$nV6CpsBQ8RPKa97eOaHyGecfkdUZl62nC.5DaWh.etDEvzFMXxqDO").roles("USER");
    }
  }


  public static void main(String[] args) {
    SpringApplication.run(CenterOauthApplication.class, args);
  }

}
