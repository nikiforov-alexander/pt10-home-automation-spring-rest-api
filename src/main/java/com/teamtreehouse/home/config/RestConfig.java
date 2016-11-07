package com.teamtreehouse.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

@Configuration
// added this suppress warning because in Intellijidea Ultimate
// @Bean Validator could not be found
@SuppressWarnings("SpringJavaAutowiringInspection")
// we extend this REST configuration class to use our validators on
// @Entity marked classes
public class RestConfig extends RepositoryRestConfigurerAdapter {
    @Autowired
    @Lazy
    private Validator validator;

    @Override
    public void configureValidatingRepositoryEventListener(
            ValidatingRepositoryEventListener validatingListener) {
        // validation occurs before creation of new resource
        // and before update
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);
    }
}
