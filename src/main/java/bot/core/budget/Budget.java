package bot.core.budget;


import java.util.HashMap;
import java.util.Map;

public class Budget {
    private final Map<String, Double> outcome = new HashMap<>();
    private Double income;
    public Budget(){
        this.income = 0.0;
        for (DefaultCategories value : DefaultCategories.values()) {
            outcome.put(value.getName(), 0.0);
        }
    }

    public Double getIncome() {
        return income;
    }

    public Map<String, Double> getOutcome() {
        return outcome;
    }
    public void addIncome(Float money){
        income = income + money;
    }

    public void addOutcome(String categories, Double money) {
        outcome.replace(categories,money);
    }

    public Double total() {
        Double totalOutcome = 0.0;
        for (Double value : outcome.values()) {
            totalOutcome = totalOutcome + value;
        }
        return income-totalOutcome;
    }

}
