public class imagingOp extends OperationDecorator {

    public imagingOp(IExamination newIExamination) {            // imagingOp class for calculate cost.
        super(newIExamination);
    }

    public String getDescription() {

        return newIExamination.getDescription() + "imaging ";
    }

    public int getCost() {

        return newIExamination.getCost() + 10;
    }
}
