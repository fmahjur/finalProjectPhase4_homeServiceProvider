package ir.maktab.finalprojectphase4.exception;

public class CustomerDoesNotHaveRegisteredOrder extends RuntimeException {
    public CustomerDoesNotHaveRegisteredOrder(String message) {
        super(message);
    }
}
