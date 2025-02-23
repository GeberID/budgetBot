package bot.core.budget;


import java.util.HashMap;
import java.util.Map;

public class Budget {
    private Float income;
    private final Map<String,Float> outcome = new HashMap<>();
    public Budget(){
        this.income = 0.0F;
        for (DefaultCategories value : DefaultCategories.values()) {
            outcome.put(value.getName(),0.0F);
        }
    }

    public Float getIncome() {
        return income;
    }

    public Map<String,Float> getOutcome() {
        return outcome;
    }
    public void addIncome(Float money){
        income = income + money;
    }
    public void addOutcome(String categories,Float money){
        outcome.replace(categories,money);
    }
    public Float total(){
        Float totalOutcome = 0.0F;
        for (Float value : outcome.values()) {
            totalOutcome = totalOutcome + value;
        }
        return income-totalOutcome;
    }

}
