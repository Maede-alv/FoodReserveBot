package com.maedealv.foodreservebot.bot;

import com.maedealv.foodreservebot.dto.FoodDto;
import com.maedealv.foodreservebot.service.FoodService;
import com.maedealv.foodreservebot.service.OrderService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TelegramBotListener {

    private static final Logger log = LoggerFactory.getLogger(TelegramBotListener.class);

    private final TelegramBot bot;
    private final FoodService foodService;
    private final OrderService orderService;

    private final MessageSource messageSource;
    private static final Locale FA_LOCALE = new Locale("fa");

    @PostConstruct
    public void start() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                processUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        log.info("Telegram Bot started with polling (synchronous)...");
    }

    private void processUpdate(Update update) {
        try {
            if (update.message() != null && update.message().text() != null) {
                handleMessage(update);
            } else if (update.callbackQuery() != null) {
                handleCallback(update.callbackQuery());
            }
        } catch (Exception e) {
            log.error("Error processing update", e);
        }
    }

    private void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text().trim();

        if (text.equals("/menu")) {
            showMenu(chatId);
        } else {
            sendMessage(chatId, msg("bot.welcome"));
        }
    }

    private void showMenu(long chatId) {
        List<FoodDto> foods = foodService.getAvailableFoods();

        if (CollectionUtils.isEmpty(foods)) {
            sendMessage(chatId, msg("bot.noFood"));
            return;
        }

        InlineKeyboardMarkup keyboard = createKeyboard(foods);
        sendMessage(
                chatId,
                "*" + msg("bot.menu.title") + "*\n" + msg("bot.menu.choose"),
                keyboard
        );
    }

    private InlineKeyboardMarkup createKeyboard(List<FoodDto> foods) {
        InlineKeyboardButton[][] buttons = foods.stream()
                .map(food -> new InlineKeyboardButton[]{
                        new InlineKeyboardButton(
                                food.name() + " - " + formatPrice(food.price()) + " " + msg("currency.toman")
                        ).callbackData("food_" + food.id())
                })
                .toArray(InlineKeyboardButton[][]::new);

        return new InlineKeyboardMarkup(buttons);
    }

    private void handleCallback(CallbackQuery callback) {
        long chatId = callback.message().chat().id();
        String data = callback.data();
        long foodId = Long.parseLong(data.substring(5));

        try {
            orderService.createOrder(chatId, foodId);
            sendMessage(chatId, msg("bot.order.success"));
        } catch (Exception e) {
            sendMessage(chatId, msg("bot.order.error"));
            log.error("Order creation failed", e);
        }
    }

    private void sendMessage(long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    private void sendMessage(long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage request = new SendMessage(chatId, text)
                .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown);

        if (keyboard != null) {
            request.replyMarkup(keyboard);
        }

        var response = bot.execute(request);
        if (!response.isOk()) {
            log.error("Failed to send message: {}", response.description());
        }
    }

    private String msg(String key) {
        return messageSource.getMessage(key, null, FA_LOCALE);
    }

    private String formatPrice(BigDecimal price) {
        NumberFormat format = NumberFormat.getInstance(FA_LOCALE);
        format.setMaximumFractionDigits(0);
        return format.format(price);
    }
}
