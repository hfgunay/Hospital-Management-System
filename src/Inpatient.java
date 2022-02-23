public class Inpatient implements IExamination{

    @Override
    public String getDescription() {
        return "Inpatient ";
    }

    @Override
    public int getCost() {
        return 10;
    }
}
