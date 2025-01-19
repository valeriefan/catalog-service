package rentsphere.catalogservice.domain;

public class HouseNotFoundException extends RuntimeException {
  public HouseNotFoundException(String message) {
    super(message);
  }
}
