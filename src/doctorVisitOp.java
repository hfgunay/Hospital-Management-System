public class doctorVisitOp extends OperationDecorator{

    public doctorVisitOp(IExamination newIExamination) {  // doctorVisitOp class for calculate cost.
        super(newIExamination);
    }

    public String getDescription() {

        return newIExamination.getDescription() + "doctorvisit ";
    }

    public int getCost() {

        return newIExamination.getCost() + 15;
    }
}
