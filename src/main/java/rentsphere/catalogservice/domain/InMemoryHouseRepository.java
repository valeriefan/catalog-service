package rentsphere.catalogservice.domain;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryHouseRepository implements HouseRepository {
    private static final Map<String, House> houses =
            new ConcurrentHashMap<>();

    @Override
    public Iterable<House> findAll() {
        return houses.values();
    }

    @Override
    public Optional<House> findByCode(String code) {
        return existsByCode(code) ? Optional.of(houses.get(code)) :
                Optional.empty();
    }

    @Override
    public boolean existsByCode(String code) {
        return houses.get(code) != null;
    }

    @Override
    public House save(House house) {
        houses.put(house.houseCode(), house);
        return house;
    }

    @Override
    public void deleteByCode(String isbn) {
        houses.remove(isbn);
    }
}
