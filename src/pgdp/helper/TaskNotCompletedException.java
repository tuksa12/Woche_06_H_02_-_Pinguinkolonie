package pgdp.helper;

public class TaskNotCompletedException extends RuntimeException {
    public TaskNotCompletedException(String errorMsg) {
        super(errorMsg);
    }
}
