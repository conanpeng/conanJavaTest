package com.conan.javaTest;

import com.conan.javaTest.file.MessageStore;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by conan on 17-1-29.
 */
@Configuration
public class SpringConfig {

    @Bean
    public MessageStore messageStore(){
        return new MessageStore();
    }
}
