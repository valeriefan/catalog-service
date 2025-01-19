package rentsphere.catalogservice.demo;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rentsphere.catalogservice.domain.House;
import rentsphere.catalogservice.domain.HouseRepository;

import java.util.List;

@Component
@Profile("testdata")
public class HouseDataLoader {
    private final HouseRepository houseRepository;
    public HouseDataLoader(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadHouseTestData() {
        houseRepository.deleteAll();
        var house1 = House.of("123456789", "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        var house2 = House.of("223456789", "A113 Transitional Housing",
                "Santa Monica",
                "CA",
                "https://angular.dev/assets/images/tutorials/common/brandon-griggs-" +
                        "wR11KBaB86U-unsplash.jpg",
                0,
                false,
                true
        );
        houseRepository.saveAll(List.of(house1, house2));
    }
}
