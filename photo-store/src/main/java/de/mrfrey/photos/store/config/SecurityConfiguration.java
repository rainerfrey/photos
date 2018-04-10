package de.mrfrey.photos.store.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class SecurityConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure( HttpSecurity http ) throws Exception {
//@formatter:off
        http
            .authorizeRequests()
            .requestMatchers( PathRequest.toStaticResources().atCommonLocations() ).permitAll()
            .requestMatchers( EndpointRequest.toAnyEndpoint() ).hasRole( "ACTUATOR" )
            .antMatchers( "/", "/photos/*/image/*" ).permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
        ;
//@formatter:on
    }
}
