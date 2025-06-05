package com.moro.tchat;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(TchatApplication.class, args);
    }

    @Bean
    public JwtParser jwtBuilder() {
        return Jwts.parser();
    }

}
