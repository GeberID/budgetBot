package bot;

import bot.core.Budget;
import bot.core.Commands;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private Map<String, Budget> budgetList;
    private Budget currentBudget;

    public TelegramBot(String botName, String botToken) {
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
        budgetList = new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if(update.getMessage().hasText()){
                String message = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                if(message.equals(Commands.START_COMMAND.getCommand())){
                    start(chatId,update.getMessage().getChat().getUserName());
                }else if (message.split(" ")[0].equals(Commands.CREATE_COMMAND.getCommand())){
                    String name = message.split(" ")[1];
                    create(chatId,name);
                }
                else if (message.equals(Commands.LIST_COMMAND.getCommand())){
                    getList(chatId);
                }else if (message.split(" ")[0].equals(Commands.USE_COMMAND.getCommand())){
                    String name = message.split(" ")[1];
                    use(chatId,name);
                }
                else if (message.equals(Commands.HELP_COMMAND.getCommand())){
                    help(chatId);
                }else if (message.split(" ")[0].equals(Commands.INCOME_COMMAND.getCommand())){
                    Long income = Long.valueOf(message.split(" ")[1]);
                    addIncome(chatId,income);
                }
                else if (message.split(" ")[0].equals(Commands.OUTCOME_COMMAND.getCommand())){
                    Long outcome = Long.valueOf(message.split(" ")[1]);
                    addOutcome(chatId,outcome);
                }
            }
        }
        else if(update.hasCallbackQuery()){
            use(update.getCallbackQuery().getMessage().getChatId(),update.getCallbackQuery().getData());
            deleteMessage(update.getCallbackQuery().getMessage().getChatId(),update.getCallbackQuery().getMessage().getMessageId());
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
    private void start(long chatId, String user){
        budgetList = new HashMap<>();
        sendSimpleMessage(chatId, "Привет %s я твой помошник в ведении бюджета".formatted(user));
        sendSimpleMessage(chatId, "Давай создадим новую запись. Для получения подсказки нажми /help");
    }
    private void create(long chatId,String name){
        if(!budgetList.containsKey(name)){
            budgetList.put(name,new Budget());
            currentBudget = budgetList.get(name);
            sendSimpleMessage(chatId, "Создана запись с именем %s\n".formatted(name)+ info());
        }
        else {
            sendSimpleMessage(chatId, "Запись с именем %s уже существует".formatted(name));
        }
    }
    private void getList(long chatId){
        sendMessageWithKeyboard(chatId,"Все созданные записи:\n", sentButtonList(budgetList.keySet().stream().toList()));

    }
    private void use(long chatId, String name){
        currentBudget = budgetList.get(name);
        sendSimpleMessage(chatId, "Выбрана запись %s\n".formatted(name) + info());
    }
    private void help(long chatId){
        sendSimpleMessage(chatId, "Команды:\n"
                +"/start - начать все с начала\n"
                +"/create ... - добавить новый бюджет. Для команды необходимо передать название новой записи\n"
                +"/list - список всех созданных записей\n"
                +"/use ... - выбрать бюджет для работы с ним. Для команды необходимо передать название новой записи\n"
                +"/help - получить справку у бота\n"
                +"/income ... - добавить доходы\n"
                +"/outcome ... - добавить расходы");
    }
    private void addIncome(long chatId, long money){
        if(currentBudget != null){
            currentBudget.addIncome(money);
            sendSimpleMessage(chatId,"Добавлено\n" + info());
        }else {
            sendSimpleMessage(chatId,"Выберите запись для работы");
        }
    }
    private void addOutcome(long chatId, long money){
        if(currentBudget != null){
            currentBudget.addOutcome(money);
            sendSimpleMessage(chatId,"Добавлено\n" + info());
        }else {
            sendSimpleMessage(chatId,"Выберите запись для работы");
        }
    }
    private String info(){
        return  "Информация по счету:\n"
                +"Прибыль = %s рублей\n".formatted(currentBudget.getIncome())
                +"Траты = %s рублей\n".formatted(currentBudget.getOutcome())
                +"Общая оценка = %s рублей\n".formatted(currentBudget.statusBudget());
    }
    private void sendSimpleMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendMessageWithKeyboard(long chatId, String text,InlineKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private InlineKeyboardMarkup sentButtonList(List<String> texts){
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
    private void deleteMessage(long chatId, int messageId){
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
