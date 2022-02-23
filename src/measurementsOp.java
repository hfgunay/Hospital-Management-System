public class measurementsOp extends OperationDecorator{

    public measurementsOp(IExamination newIExamination) {       // measurementsOp class for calculate cost.
        super(newIExamination);
    }

    public String getDescription() {

        return newIExamination.getDescription() + "measurements ";
    }

    public int getCost() {

        return newIExamination.getCost() + 5;
    }
}
