package bot;

import bot.core.Budget;
import bot.core.Commands;
import bot.core.PrepareMessage;
import bot.core.localization.RULocal;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TelegramBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private Map<String, Budget> budgetList;
    private Budget currentBudget;
    private boolean isWaitingAnswer = false;
    private String lastCommand = "null";

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
                    isWaitingAnswer = true;
                    lastCommand = update.getMessage().getText();
                }
                else if (message.equals(Commands.CREATE_COMMAND.getCommand())){
                    executeMessage(PrepareMessage.createMessage(chatId, RULocal.getLetsCreateNewValue()));
                    isWaitingAnswer = true;
                    lastCommand = update.getMessage().getText();
                }
                else if (lastCommand.equals(Commands.START_COMMAND.getCommand()) || lastCommand.equals(Commands.CREATE_COMMAND.getCommand()) && isWaitingAnswer) {
                    create(chatId,message);
                    isWaitingAnswer = false;
                    lastCommand = "";
                }
                else if (message.equals(Commands.LIST_COMMAND.getCommand())){
                    getList(chatId);
                    isWaitingAnswer = false;
                    lastCommand = update.getMessage().getText();
                }
                else if (message.equals(Commands.HELP_COMMAND.getCommand())){
                    help(chatId);
                    isWaitingAnswer = false;
                    lastCommand = update.getMessage().getText();
                }else if (message.equals(Commands.INCOME_COMMAND.getCommand())){
                    executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAddIncome()));
                    isWaitingAnswer = true;
                    lastCommand = update.getMessage().getText();
                }
                else if (message.equals(Commands.OUTCOME_COMMAND.getCommand())){
                    executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAddOutcome()));
                    isWaitingAnswer = true;
                    lastCommand = update.getMessage().getText();
                }
                else if (lastCommand.equals(Commands.INCOME_COMMAND.getCommand()) && isWaitingAnswer) {
                    try {
                        addIncome(chatId, Long.parseLong(message));
                        deleteMessage(chatId,update.getMessage().getMessageId());
                        isWaitingAnswer = false;
                    } catch (NumberFormatException e) {
                        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getErrorParseLong()));
                        isWaitingAnswer = true;
                    }
                }
                else if (lastCommand.equals(Commands.OUTCOME_COMMAND.getCommand()) && isWaitingAnswer) {
                    try {
                        addOutcome(chatId, Long.parseLong(message));
                        deleteMessage(chatId,update.getMessage().getMessageId());
                        isWaitingAnswer = false;
                    } catch (NumberFormatException e) {
                        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getErrorParseLong()));
                        isWaitingAnswer = true;
                    }

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
        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getStartBot().formatted(user)));
        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getLetsCreateNewValue()));
    }
    private void create(long chatId,String name) {
        if(!budgetList.containsKey(name)){
            budgetList.put(name,new Budget());
            currentBudget = budgetList.get(name);
            executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAfterCreateingNewValue().formatted(name)+ info()));
        }
        else {
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getErrorCreatingNewValue().formatted(name)));
        }
    }
    private void getList(long chatId){
        executeMessage(PrepareMessage.createMessage(chatId,RULocal.getAllValues(), PrepareMessage.createInlineKeyboard(budgetList.keySet().stream().toList())));
    }
    private void use(long chatId, String name) {
        currentBudget = budgetList.get(name);
        executeMessage(PrepareMessage.createMessage(chatId,RULocal.getSelectedValue().formatted(name) + info()));
    }
    private void help(long chatId) {
        executeMessage(PrepareMessage.createMessage(chatId,RULocal.getHelp()));
    }
    private void addIncome(long chatId, long money) {
        if(currentBudget != null){
            currentBudget.addIncome(money);
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getAdded() + info()));
        }else {
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getSelectValueForWork()));
        }
    }
    private void addOutcome(long chatId, long money){
        if(currentBudget != null){
            currentBudget.addOutcome(money);
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getAdded()+ info()));
        }else {
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getSelectValueForWork()));
        }
    }
    private String info(){
        return  RULocal.getInfo().formatted(currentBudget.getIncome(),currentBudget.getOutcome(),currentBudget.statusBudget());
    }
    private void executeMessage(BotApiMethodMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
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
