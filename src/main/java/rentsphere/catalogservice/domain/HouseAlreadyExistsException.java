package rentsphere.catalogservice.domain;

public class HouseAlreadyExistsException extends RuntimeException {
    public HouseAlreadyExistsException(String code) {
      super("A house with reference code " + code + " already exists.");
    }
}
