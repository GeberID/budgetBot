package bot.core.user;

import bot.core.budget.Budget;

import java.util.HashMap;
import java.util.Map;


public class User {
    private final String name;
    private boolean isWaitingAnswer;
    private String lastCommand;
    private String currentCategory;
    private Map<String, Budget> budgetList;
    private Budget currentBudget;

    public User(String name) {
        this.name = name;
        budgetList = new HashMap<>();
        this.isWaitingAnswer = false;
        lastCommand = "null";
        currentCategory = "";
        budgetList = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand(String lastCommand) {
        this.lastCommand = lastCommand;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public Map<String, Budget> getBudgetList() {
        return budgetList;
    }

    public void setBudgetList(Map<String, Budget> budgetList) {
        this.budgetList = budgetList;
    }

    public Budget getCurrentBudget() {
        return currentBudget;
    }

    public void setCurrentBudget(Budget currentBudget) {
        this.currentBudget = currentBudget;
    }

    public boolean waitAnswer(boolean value) {
        if (value) {
            return isWaitingAnswer = true;
        } else return isWaitingAnswer = false;
    }
}
