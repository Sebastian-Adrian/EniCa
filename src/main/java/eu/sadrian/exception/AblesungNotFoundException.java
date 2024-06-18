package eu.sadrian.exception;

public class AblesungNotFoundException extends RuntimeException {
    public AblesungNotFoundException(Long id) {
        super("Kann Ablesung " + id + " nicht finden.");
    }
}
