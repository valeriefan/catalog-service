package rentsphere.catalogservice.domain;

public class HouseNotFoundException extends RuntimeException {
    public HouseNotFoundException(String code) {
        super("The house with reference code " + code + " was not found.");
    }
}
