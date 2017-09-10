package de.mrfrey.photos.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean globalCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return bean;
    }

    @Configuration
    public static class AuthWebConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("rainer").password("secret").roles("ADMIN", "USER").and()
                    .withUser("stefan").password("secret").roles("USER").and()
                    .withUser("michael").password("secret").roles("GUEST")
            ;
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().httpBasic();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    @EnableConfigurationProperties(AuthorizationServerProperties.class)
    static class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {
        @Value("${key.private.file}")
        private String privateKeyFile;

        @Value("${key.public.file}")
        private String publicKeyFile;

        @Autowired
        private AuthorizationServerProperties properties;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security)
                throws Exception {
            if (this.properties.getCheckTokenAccess() != null) {
                security.checkTokenAccess(this.properties.getCheckTokenAccess());
            }
            if (this.properties.getTokenKeyAccess() != null) {
                security.tokenKeyAccess(this.properties.getTokenKeyAccess());
            }
            if (this.properties.getRealm() != null) {
                security.realm(this.properties.getRealm());
            }
        }


        @Bean
        public JwtAccessTokenConverter accessTokenConverter() throws IOException {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setSigningKey(new String(Files.readAllBytes(Paths.get(privateKeyFile))));
            converter.setVerifierKey(new String(Files.readAllBytes(Paths.get(publicKeyFile))));
            return converter;
        }

        @Bean
        public TokenStore tokenStore() throws IOException {
            return new JwtTokenStore(accessTokenConverter());
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("photo-ui")
                        .authorizedGrantTypes("password", "refresh_token")
                        .autoApprove(true)
                        .scopes("photo-ui")
                    .and()
                    .withClient("metadata-extractor")
                        .authorizedGrantTypes("client_credentials", "refresh_token")
                        .scopes("images")
                        .autoApprove(true)
                        .secret("secret")
                    .and()
                    .withClient("image-scaler")
                        .authorizedGrantTypes("client_credentials", "refresh_token")
                        .scopes("images")
                        .autoApprove(true)
                        .secret("secret")
            ;
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .accessTokenConverter(accessTokenConverter())
                    .tokenStore(tokenStore())
                    .tokenEnhancer(accessTokenConverter())
                    .authenticationManager(authenticationManager)
            ;
        }
    }
}
