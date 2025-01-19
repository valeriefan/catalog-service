package rentsphere.catalogservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rentsphere.catalogservice.config.Properties;

@RestController
@RequestMapping
public class HomeController {

    private final Properties properties;

    public HomeController(Properties properties) {
        this.properties = properties;
    }

    @GetMapping("/")
    public String getGreeting() {
        return properties.getGreeting();
    }
}
