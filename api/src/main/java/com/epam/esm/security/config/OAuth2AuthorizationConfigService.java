package com.epam.esm.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type OAuth2AuthorizationConfigService.
 */
@Configuration
public class OAuth2AuthorizationConfigService extends AuthorizationServerConfigurerAdapter {
    @Value("${application.oauth.client-id}")
    private String clientId;
    @Value("${application.oauth.client-secret}")
    private String clientSecret;
    @Value("${application.jwt.access-token-expiration-period-sec}")
    private int validPeriodAccessTokenSec;
    @Value("${application.jwt.refresh-token-expiration-period-sec}")
    private int validPeriodRefreshTokenSec;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtAccessTokenConverter tokenConverter;

    /**
     * JwtTokenStore jwtTokenStore.
     *
     * @return the jwtTokenStore
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(tokenConverter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(encoder.encode(clientSecret))
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(validPeriodAccessTokenSec)
                .refreshTokenValiditySeconds(validPeriodRefreshTokenSec);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(jwtTokenStore())
                .accessTokenConverter(tokenConverter);
    }
}