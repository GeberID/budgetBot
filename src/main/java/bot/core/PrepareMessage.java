package bot.core;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public abstract class PrepareMessage {
    public static InlineKeyboardMarkup inlineKeyboardMarkupBuilder(List<String> texts,int buttonsInRow){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        double rowsCount = Math.ceil((double) texts.size() /buttonsInRow);
        int countText = 0;
        for (int i = 0; i < rowsCount; i++) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            for (int j = 0; j < buttonsInRow; j++) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(texts.get(countText));
                inlineKeyboardButton.setCallbackData(texts.get(countText));
                keyboardButtonsRow.add(inlineKeyboardButton);
                countText++;
            }
            rowList.add(keyboardButtonsRow);
        }
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
