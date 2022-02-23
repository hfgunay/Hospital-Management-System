public class testsOp extends OperationDecorator{

    public testsOp(IExamination newIExamination) {          // testsOp class for calculate cost.
        super(newIExamination);
    }

    public String getDescription() {

        return newIExamination.getDescription() + "tests ";
    }

    public int getCost() {

        return newIExamination.getCost() + 7;
    }
}
