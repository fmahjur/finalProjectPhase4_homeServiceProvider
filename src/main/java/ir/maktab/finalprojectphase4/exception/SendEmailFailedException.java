package ir.maktab.finalprojectphase4.exception;

public class SendEmailFailedException extends RuntimeException{
    public SendEmailFailedException(String message) {
        super(message);
    }
}
