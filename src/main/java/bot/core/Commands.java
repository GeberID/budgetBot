package bot.core;

public enum Commands {
    START_COMMAND("/start"),
    CREATE_COMMAND("/create"),
    LIST_COMMAND("/list"),
    HELP_COMMAND("/help"),
    INCOME_COMMAND("/income"),
    OUTCOME_COMMAND("/outcome");
    private final String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
