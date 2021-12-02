package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Class contains spring web configuration
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Configuration
@EntityScan(value = "com.epam.esm.entity")
@EnableJpaRepositories(value = "com.epam.esm.repository")
public class SpringConfig{

    @Value("UTF-8")
    private String encoding;
    @Value("localization/exception")
    private String fileName;

    /**
     * Create bean MessageSource which will be used to get info from properties
     * files
     *
     * @return the message source
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(fileName);
        messageSource.setDefaultEncoding(encoding);
        return messageSource;
    }

}
