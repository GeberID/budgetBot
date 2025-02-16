package bot.core;


public class Budget {
    private Long income;
    private Long outcome;
    public Budget(){
        this.income = 0L;
        this.outcome = 0L;
    }

    public Long getIncome() {
        return income;
    }

    public Long getOutcome() {
        return outcome;
    }
    public void addIncome(Long money){
        income = income + money;
    }
    public void addOutcome(Long money){
        outcome = outcome + money;
    }
    public Long statusBudget(){
        return income-outcome;
    }

}
