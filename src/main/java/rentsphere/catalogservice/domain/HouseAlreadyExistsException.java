package rentsphere.catalogservice.domain;

public class HouseAlreadyExistsException extends RuntimeException {
  public HouseAlreadyExistsException(String message) {
    super(message);
  }
}
