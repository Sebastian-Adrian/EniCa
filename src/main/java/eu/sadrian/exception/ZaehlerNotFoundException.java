package eu.sadrian.exception;

public class ZaehlerNotFoundException extends RuntimeException {
    public ZaehlerNotFoundException(Long id) {
        super("Kann Zähler " + id + " nicht finden.");
    }
}
