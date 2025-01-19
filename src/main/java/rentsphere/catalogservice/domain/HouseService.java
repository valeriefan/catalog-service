package rentsphere.catalogservice.domain;

import org.springframework.stereotype.Service;

@Service
public class HouseService {
    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public Iterable<House> viewHouseList() {
        return houseRepository.findAll();
    }

    public House viewHouseDetails(String code) {
        return houseRepository.findByCode(code)
                .orElseThrow(() -> new HouseNotFoundException(code));
    }

    public House addHouseToCatalog(House house) {
        if (houseRepository.existsByCode(house.code())) {
            throw new HouseAlreadyExistsException(house.code());
        }
        return houseRepository.save(house);
    }

    public void removeHouseFromCatalog(String code) {
        if (!houseRepository.existsByCode(code)) {
            throw new HouseNotFoundException(code);
        }
        houseRepository.deleteByCode(code);
    }

    public House updateHouseDetails(String code, House house) {
        return houseRepository.findByCode(code)
                .map(existingHouse -> {
                    var houseToUpdate = new House(
                            existingHouse.id(),
                            existingHouse.code(),
                            house.name(),
                            house.city(),
                            house.state(),
                            house.photo(),
                            house.availableUnits(),
                            house.wifi(),
                            house.laundry(),
                            existingHouse.createdDate(),
                            existingHouse.lastModifiedDate(),
                            existingHouse.version());
                    return  houseRepository.save(houseToUpdate);
                }).orElseThrow(() -> new HouseNotFoundException(code));
    }
}
