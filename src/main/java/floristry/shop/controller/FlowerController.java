package floristry.shop.controller;

import floristry.shop.model.Flower;
import floristry.shop.service.FlowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flowers")
@RequiredArgsConstructor
public class FlowerController {

    private final FlowerService service;

    @GetMapping("/hello")
    public String hello() {
        return "Welcome to the flower shop! Fresh bouquets every day.";
    }

    @GetMapping("/hello/user")
    public String helloUser() {
        return "Welcome to the flower shop, dear customer! Browse our bouquets.";
    }

    @GetMapping("/hello/admin")
    public String helloAdmin() {
        return "Hello, Admin! Fresh roses just arrived, time to update the stock.";
    }

    @GetMapping("/hello/superadmin")
    public String helloSuperadmin() {
        return "Greetings, Superadmin! The whole garden is in your hands.";
    }

    @GetMapping
    public List<Flower> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Flower getOne(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Flower create(@RequestBody Flower flower) {
        return service.create(flower);
    }

    @PutMapping("/{id}")
    public Flower update(@PathVariable String id, @RequestBody Flower flower) {
        return service.update(id, flower);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}