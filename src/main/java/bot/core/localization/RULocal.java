package bot.core.localization;


public abstract class RULocal {
    private static String startBot = "Привет %s я твой помошник в ведении бюджета";
    private static String letsCreateNewValue = "Давай создадим новую запись. Напиши название новой записи";
    private static String afterCreateingNewValue = "Создана запись с именем %s\n";
    private static String allValues = "Все созданные записи:\n";
    private static String selectedValue = "Выбрана запись %s\n";
    private static String added = "Добавлено\n";
    private static String selectValueForWork = "Выберите запись для работы\n";
    private static String info = "Информация по счету:\nПрибыль = %s рублей\nТраты = %s рублей\nОбщая оценка = %s рублей\n";
    private static String addIncome = "Добавить доходы\n";
    private static String addOutcome = "Добавить расходы\n";
    //Error
    private static String errorCreatingNewValue = "Запись с именем %s уже существует";
    private static String errorParseLong = "Вы ввели не числовое значение\nВведите число";
    //Help
    private static String help = "Команды:\n"
            + "/start - начать все с начала\n"
            + "/create ... - добавить новый бюджет. Для команды необходимо передать название новой записи\n"
            + "/list - список всех созданных записей\n"
            + "/use ... - выбрать бюджет для работы с ним. Для команды необходимо передать название новой записи\n"
            + "/help - получить справку у бота\n"
            + "/income ... - добавить доходы\n"
            + "/outcome ... - добавить расходы";

    public static String getStartBot() {
        return startBot;
    }

    public static String getLetsCreateNewValue() {
        return letsCreateNewValue;
    }

    public static String getAfterCreateingNewValue() {
        return afterCreateingNewValue;
    }

    public static String getAllValues() {
        return allValues;
    }

    public static String getSelectedValue() {
        return selectedValue;
    }

    public static String getAdded() {
        return added;
    }

    public static String getSelectValueForWork() {
        return selectValueForWork;
    }

    public static String getInfo() {
        return info;
    }

    public static String getErrorCreatingNewValue() {
        return errorCreatingNewValue;
    }

    public static String getHelp() {
        return help;
    }

    public static String getAddIncome() {
        return addIncome;
    }

    public static String getAddOutcome() {
        return addOutcome;
    }

    public static String getErrorParseLong() {
        return errorParseLong;
    }
}
