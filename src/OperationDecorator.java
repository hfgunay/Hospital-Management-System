public abstract class OperationDecorator implements IExamination{

    protected IExamination newIExamination;

    public OperationDecorator(IExamination newIExamination) {   // This abstract class for wrapping all operations.
        this.newIExamination = newIExamination;
    }

    public String getDescription() {

        return newIExamination.getDescription();
    }

    public int getCost() {

        return newIExamination.getCost();
    }
}
