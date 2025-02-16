package bot;

import bot.core.Budget;
import bot.core.Commands;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
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
        if (update.hasMessage() & update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if(message.equals(Commands.START_COMMAND.getCommand())){
                start(chatId,update.getMessage().getChat().getUserName());
            }else if (message.split(" ")[0].equals(Commands.CREATE_COMMAND.getCommand())){
                String name = message.split(" ")[1];
                create(chatId,name);
            }
            else if (message.equals(Commands.LIST_COMMAND.getCommand())){
                list(chatId);
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
        sendMessage(chatId, "Привет %s я твой помошник в ведении бюджета".formatted(user));
        sendMessage(chatId, "Давай создадим новую запись. Для получения подсказки нажми /help");
    }
    private void create(long chatId,String name){
        if(!budgetList.containsKey(name)){
            budgetList.put(name,new Budget());
            currentBudget = budgetList.get(name);
            sendMessage(chatId, "Создана запись с именем %s\n".formatted(name)+ info());
        }
        else {
            sendMessage(chatId, "Запись с именем %s уже существует".formatted(name));
        }
    }
    @Deprecated
    private void list(long chatId){
        sendMessage(chatId,"Все созданные записи:\n" + budgetList.keySet());
    }
    private void use(long chatId, String name){
        currentBudget = budgetList.get(name);
        sendMessage(chatId, "Выбрана запись %s\n".formatted(name) + info());
    }
    private void help(long chatId){
        sendMessage(chatId, "Команды:\n"
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
            sendMessage(chatId,"Добавлено\n" + info());
        }else {
            sendMessage(chatId,"Выберите запись для работы");
        }
    }
    private void addOutcome(long chatId, long money){
        if(currentBudget != null){
            currentBudget.addOutcome(money);
            sendMessage(chatId,"Добавлено\n" + info());
        }else {
            sendMessage(chatId,"Выберите запись для работы");
        }
    }
    private String info(){
        return  "Информация по счету:\n"
                +"Прибыль = %s рублей\n".formatted(currentBudget.getIncome())
                +"Траты = %s рублей\n".formatted(currentBudget.getOutcome())
                +"Общая оценка = %s рублей\n".formatted(currentBudget.statusBudget());
    }
    private void sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void sentButtonList(long chatId, String text){
        //TODO
    }
}
