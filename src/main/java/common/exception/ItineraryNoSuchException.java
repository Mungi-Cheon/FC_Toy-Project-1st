package common.exception;

import java.util.NoSuchElementException;

public class ItineraryNoSuchException extends NoSuchElementException {

    public ItineraryNoSuchException(String msg){
        super(msg);
    }
}
