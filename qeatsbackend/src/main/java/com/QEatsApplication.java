package com;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@Log4j2
public class QEatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(QEatsApplication.class, args);


        log.info("Congrats! Your QEatsApplication server has started");

    }


    @Bean
    @Scope("prototype")
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
