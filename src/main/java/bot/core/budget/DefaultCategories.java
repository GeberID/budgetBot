package bot.core.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public enum DefaultCategories {
    FOOD("Еда"),
    TECH("Техника"),
    HOUSEHOLD("Хозяйственные товары"),
    HOME("Мебель/ремонт"),
    TRANSPORT("Транспорт"),
    ENTERTAINMENTS("Развлечения"),
    HEALTH("Здоровье"),
    CLOTHES("Одежда"),
    SPORT("Спорт"),
    GITS("Подарки"),
    ANIMALS("Животные"),
    CHILD("Дети"),
    UTILITIES("Жкх"),
    OTHER("Остальное");

    private final String name;
    DefaultCategories(String name){
        this.name = name;
    }
    public static List<String> getAllNames(){
        List<String> result = new ArrayList<>();
        Arrays.stream(DefaultCategories.values()).forEach(element -> result.add(element.getName()));
        return result;
    }

    public String getName() {
        return name;
    }
}
