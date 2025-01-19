package rentsphere.catalogservice.demo;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rentsphere.catalogservice.domain.House;
import rentsphere.catalogservice.domain.HouseRepository;

@Component
@Profile("testdata")
public class BookDataLoader {
    private final HouseRepository houseRepository;
    public BookDataLoader(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadHouseTestData() {
        var house1 = new House("123456789", "Acme Fresh Start Housing",
                "Chicago",
                "IL",
                "https://angular.dev/assets/images/tutorials/common/b" +
                        "ernard-hermant-CLKGGwIBTaY-unsplash.jpg",
                4,
                true,
                true
        );
        var house2 = new House("223456789", "A113 Transitional Housing",
                "Santa Monica",
                "CA",
                "https://angular.dev/assets/images/tutorials/common/brandon-griggs-" +
                        "wR11KBaB86U-unsplash.jpg",
                0,
                false,
                true
        );
        houseRepository.save(house1);
        houseRepository.save(house2);
    }
}
