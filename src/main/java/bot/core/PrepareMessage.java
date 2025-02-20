package bot.core;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public abstract class PrepareMessage {
    public static InlineKeyboardMarkup createInlineKeyboard(List<String> texts){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        for (String text : texts) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(text);
            inlineKeyboardButton.setCallbackData(text);
            keyboardButtonsRow.add(inlineKeyboardButton);
        }
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
    public static SendMessage createMessage(long chatId, String text) {
        return new  SendMessage(String.valueOf(chatId), text);
    }
    public static SendMessage createMessage(long chatId, String text,InlineKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
