package bot;

import bot.core.Commands;
import bot.core.PrepareMessage;
import bot.core.budget.Budget;
import bot.core.budget.DefaultCategories;
import bot.core.localization.RULocal;
import bot.core.user.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    Map<Long, User> users = new HashMap<>();

    public TelegramBot(String botName, String botToken) {
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
    }
    //TODO refactoring all methods
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if(update.getMessage().hasText()){
                String message = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                if(message.equals(Commands.START_COMMAND.getCommand())){
                    if (!users.containsKey(chatId)) {
                        users.put(chatId, new User(update.getMessage().getChat().getUserName()));
                    }
                    start(chatId, users.get(chatId));
                    users.get(chatId).waitAnswer(true);
                    users.get(chatId).setLastCommand(update.getMessage().getText());
                }
                //TODO fix bug. I can watch budgets from another person and use it
                else if (message.equals(Commands.CREATE_COMMAND.getCommand())){
                    executeMessage(PrepareMessage.createMessage(chatId, RULocal.getLetsCreateNewValue()));
                    users.get(chatId).waitAnswer(true);
                    users.get(chatId).setLastCommand(update.getMessage().getText());
                } else if (users.get(chatId).getLastCommand().equals(Commands.START_COMMAND.getCommand()) ||
                        users.get(chatId).getLastCommand().equals(Commands.CREATE_COMMAND.getCommand()) &&
                                users.get(chatId).waitAnswer(true)) {
                    create(chatId, users.get(chatId), message);
                    users.get(chatId).waitAnswer(false);
                    users.get(chatId).setLastCommand("");
                }
                else if (message.equals(Commands.LIST_COMMAND.getCommand())){
                    getList(chatId, users.get(chatId));
                    users.get(chatId).waitAnswer(false);
                    users.get(chatId).setLastCommand(update.getMessage().getText());
                }
                else if (message.equals(Commands.HELP_COMMAND.getCommand())){
                    help(chatId);
                    users.get(chatId).waitAnswer(false);
                    users.get(chatId).setLastCommand(update.getMessage().getText());
                }else if (message.equals(Commands.INCOME_COMMAND.getCommand())){
                    executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAddIncome()));
                    users.get(chatId).waitAnswer(true);
                    users.get(chatId).setLastCommand(update.getMessage().getText());
                }
                else if (message.equals(Commands.OUTCOME_COMMAND.getCommand())){
                    executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAddOutcome(),
                            PrepareMessage.inlineKeyboardMarkupBuilder(DefaultCategories.getAllNames(),2)));
                    users.get(chatId).waitAnswer(true);
                    users.get(chatId).setLastCommand(update.getMessage().getText());
                } else if (users.get(chatId).getLastCommand().equals(Commands.INCOME_COMMAND.getCommand()) &&
                        users.get(chatId).waitAnswer(true)) {
                    try {
                        addIncome(chatId, users.get(chatId), Float.parseFloat(message));
                        deleteMessage(chatId,update.getMessage().getMessageId());
                        users.get(chatId).waitAnswer(false);
                    } catch (NumberFormatException e) {
                        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getErrorParseLong()));
                        users.get(chatId).waitAnswer(true);
                    }
                } else if (users.get(chatId).getLastCommand().equals(Commands.OUTCOME_COMMAND.getCommand()) &&
                        users.get(chatId).waitAnswer(true)) {
                    try {
                        deleteMessage(chatId,update.getMessage().getMessageId());
                        addOutcome(chatId, users.get(chatId), users.get(chatId).getCurrentCategory(), Double.parseDouble(message));
                        users.get(chatId).waitAnswer(false);
                    } catch (NumberFormatException e) {
                        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getErrorParseLong()));
                        users.get(chatId).waitAnswer(true);
                    }
                }
            }
        }
        else if(update.hasCallbackQuery()){
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (users.get(chatId).getLastCommand().equals(Commands.LIST_COMMAND.getCommand())) {
                use(update.getCallbackQuery().getMessage().getChatId(), users.get(chatId), update.getCallbackQuery().getData());
                deleteMessage(update.getCallbackQuery().getMessage().getChatId(),update.getCallbackQuery().getMessage().getMessageId());
            } else if (users.get(chatId).getLastCommand().equals(Commands.OUTCOME_COMMAND.getCommand())) {
                users.get(chatId).setCurrentCategory(update.getCallbackQuery().getData());
                deleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
                executeMessage(PrepareMessage.createMessage(update.getCallbackQuery().getMessage().getChatId(), RULocal.getAddOutcome()));

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

    private void start(long chatId, User user) {

        user.setBudgetList(new HashMap<>());
        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getStartBot().formatted(user)));
        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getLetsCreateNewValue()));
    }

    private void create(long chatId, User user, String name) {
        if (!user.getBudgetList().containsKey(name)) {
            user.getBudgetList().put(name, new Budget());
            user.setCurrentBudget(user.getBudgetList().get(name));
            executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAfterCreateingNewValue().formatted(name) + info(user)));
        }
        else {
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getErrorCreatingNewValue().formatted(name)));
        }
    }

    private void getList(long chatId, User user) {
        executeMessage(PrepareMessage.createMessage(chatId,RULocal.getAllValues(),
                PrepareMessage.inlineKeyboardMarkupBuilder(user.getBudgetList().keySet().stream().toList(), 1)));
    }

    private void use(long chatId, User user, String name) {
        user.setCurrentBudget(user.getBudgetList().get(name));
        executeMessage(PrepareMessage.createMessage(chatId, RULocal.getSelectedValue().formatted(name) + info(user)));
    }
    private void help(long chatId) {
        executeMessage(PrepareMessage.createMessage(chatId,RULocal.getHelp()));
    }

    private void addIncome(long chatId, User user, Float money) {
        if (user.getCurrentBudget() != null) {
            user.getCurrentBudget().addIncome(money);
            executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAdded() + info(user)));
        }else {
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getSelectValueForWork()));
        }
    }

    private void addOutcome(long chatId, User user, String defaultCategories, Double money) {
        if (user.getCurrentBudget() != null) {
            user.getCurrentBudget().addOutcome(defaultCategories, money);
            executeMessage(PrepareMessage.createMessage(chatId, RULocal.getAdded() + info(user)));
        }else {
            executeMessage(PrepareMessage.createMessage(chatId,RULocal.getSelectValueForWork()));
        }
    }

    private String info(User user) {
        return RULocal.getInfo().formatted(user.getCurrentBudget().getIncome(), user.getCurrentBudget().getOutcome(), user.getCurrentBudget().total());
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
