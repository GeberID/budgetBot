package org.main;

import bot.Bot;
import bot.ProjectProperties;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot(ProjectProperties.getProperty("telegramBot.name"),
                    ProjectProperties.getProperty("telegramBot.token")));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}