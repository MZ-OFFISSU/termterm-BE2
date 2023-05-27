package server.api.termterm.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {
    private static final Integer CONN_TIMEOUT = 15 * 1000;  // 15초
    private static final Integer READ_TIMEOUT = 15 * 1000;  // 15초

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(CONN_TIMEOUT); // 연결시간 초과
        httpRequestFactory.setReadTimeout(READ_TIMEOUT);

        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(httpRequestFactory))
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                .build();
    }
}