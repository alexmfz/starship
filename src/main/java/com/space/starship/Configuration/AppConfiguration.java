package com.space.starship.Configuration;

import com.space.starship.Aspect.LoggerAspect;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppConfiguration {
    @Bean
    public LoggerAspect loggerAspect(){
        return new LoggerAspect();
    }

}
