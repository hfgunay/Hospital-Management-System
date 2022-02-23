public class Outpatient implements IExamination{

    @Override
    public String getDescription() {
        return "Outpatient ";
    }

    @Override
    public int getCost() {
        return 15;
    }
}
