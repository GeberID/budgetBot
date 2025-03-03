package bot.core.localization;


public abstract class RULocal {
    private static String startBot = "Привет %s я твой помошник в ведении бюджета";
    private static String letsCreateNewValue = "Давай создадим новую запись. Напиши название новой записи";
    private static String afterCreateingNewValue = "Создана запись с именем %s\n";
    private static String allValues = "Все созданные записи:\n";
    private static String selectedValue = "Выбрана запись %s\n";
    private static String added = "Добавлено\n";
    private static String selectValueForWork = "Выберите запись для работы\n";
    private static String infoMessage = "Информация по счету:\nПрибыль:\n%s\n\nТраты:\n%s\nОбщая оценка:\n%s\n";
    private static String selectCategory = "Выберите категорию\n";
    private static String insertNumber = "Введите число\n";
    //Error
    private static String errorCreatingNewValue = "Запись с именем %s уже существует";
    private static String errorParseLong = "Вы ввели не числовое значение\nВведите число";
    //Help
    private static String help = "Команды:\n"
            + "/start - Начать работу с ботом заново\n"
            + "/create - Создать новый бюджет\n"
            + "/list - Отобразить список всех созданных бюджетов\n"
            + "/income - Добавить общий доход\n"
            + "/outcome - Добавить траты по категориям";

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

    public static String getInfoMessage() {
        return infoMessage;
    }

    public static String getErrorCreatingNewValue() {
        return errorCreatingNewValue;
    }

    public static String getHelp() {
        return help;
    }


    public static String getInsertNumber() {
        return insertNumber;
    }

    public static String getErrorParseLong() {
        return errorParseLong;
    }

    public static String getselectCategory() {
        return selectCategory;
    }
}
