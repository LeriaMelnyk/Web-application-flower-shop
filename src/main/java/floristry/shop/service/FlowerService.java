package floristry.shop.service;

import floristry.shop.model.Flower;
import floristry.shop.repository.FlowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowerService {

    private final FlowerRepository repository;

    public List<Flower> findAll() {
        return repository.findAll();
    }

    public Flower findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Flower not found: " + id));
    }

    public Flower create(Flower flower) {
        flower.setId(null);
        return repository.save(flower);
    }

    public Flower update(String id, Flower flower) {
        Flower existing = findById(id);
        existing.setName(flower.getName());
        existing.setCategory(flower.getCategory());
        existing.setPrice(flower.getPrice());
        existing.setStock(flower.getStock());
        existing.setDescription(flower.getDescription());
        return repository.save(existing);
    }

    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }
}