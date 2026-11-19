package floristry.shop.repository;

import floristry.shop.model.Flower;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlowerRepository extends MongoRepository<Flower, String> {
}