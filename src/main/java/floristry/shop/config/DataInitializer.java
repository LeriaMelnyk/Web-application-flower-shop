package floristry.shop.config;

import floristry.shop.model.Flower;
import floristry.shop.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FlowerRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                Flower.builder().name("Red Rose").category("ROSE")
                        .price(2.5).stock(120).description("Classic red rose, 60 cm").build(),
                Flower.builder().name("Yellow Tulip").category("TULIP")
                        .price(1.8).stock(200).description("Fresh spring tulip").build(),
                Flower.builder().name("White Lily").category("LILY")
                        .price(3.2).stock(80).description("Fragrant white lily").build(),
                Flower.builder().name("Pink Peony").category("PEONY")
                        .price(4.5).stock(60).description("Large pink peony").build(),
                Flower.builder().name("Spring Bouquet").category("BOUQUET")
                        .price(25.0).stock(15).description("Mixed bouquet of 15 flowers").build(),
                Flower.builder().name("Wedding Bouquet").category("BOUQUET")
                        .price(60.0).stock(5).description("White roses and peonies").build()
        ));
    }
}