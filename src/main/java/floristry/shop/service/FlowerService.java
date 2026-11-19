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
    private final AuditService audit;

    public List<Flower> findAll() {
        audit.log("READ_ALL", null);
        return repository.findAll();
    }

    public Flower findById(String id) {
        audit.log("READ", id);
        return getOrThrow(id);
    }

    public Flower create(Flower flower) {
        flower.setId(null);
        Flower saved = repository.save(flower);
        audit.log("CREATE", saved.getId());
        return saved;
    }

    public Flower update(String id, Flower flower) {
        Flower existing = getOrThrow(id);
        existing.setName(flower.getName());
        existing.setCategory(flower.getCategory());
        existing.setPrice(flower.getPrice());
        existing.setStock(flower.getStock());
        existing.setDescription(flower.getDescription());
        Flower saved = repository.save(existing);
        audit.log("UPDATE", id);
        return saved;
    }

    public void delete(String id) {
        getOrThrow(id);
        repository.deleteById(id);
        audit.log("DELETE", id);
    }

    private Flower getOrThrow(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Flower not found: " + id));
    }
}