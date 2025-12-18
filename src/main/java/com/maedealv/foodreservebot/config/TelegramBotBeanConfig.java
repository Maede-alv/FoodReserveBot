package com.maedealv.foodreservebot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotBeanConfig {

    @Bean
    public TelegramBot telegramBot(@Value("${spring.bot.token}") String token) {
        return new TelegramBot(token);
    }
}

