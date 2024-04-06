package common.exception;

public class TripIdNotFoundException extends RuntimeException{
	public TripIdNotFoundException(String message) {
		super(message);
	}
}
