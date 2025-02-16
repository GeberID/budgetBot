package bot;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    public Bot(String botName, String botToken) {
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() & update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start" -> {
                    String userName = update.getMessage().getChat().getUserName();
                    startCommand(chatId, userName);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }
    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    private void startCommand(long chatId, String userName) {
        sendMessage(chatId, "Привет %s я твой помошник в ведении бюджета".formatted(userName));
    }

    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
