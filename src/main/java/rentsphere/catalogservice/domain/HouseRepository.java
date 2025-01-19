package rentsphere.catalogservice.domain;

import java.util.Optional;

public interface HouseRepository {
    Iterable<House> findAll();
    Optional<House> findByCode(String code);
    boolean existsByCode(String code);
    House save(House house);
    void deleteByCode(String code);
}
