package floristry.shop.controller;

import floristry.shop.model.Flower;
import floristry.shop.service.FlowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/flowers")
@RequiredArgsConstructor
public class FlowerController {

    private final FlowerService service;

    @GetMapping("/hello")
    @PreAuthorize("isAuthenticated()")
    public String hello() {
        return "Welcome to the flower shop! Fresh bouquets every day.";
    }

    @GetMapping("/hello/user")
    @PreAuthorize("hasRole('USER')")
    public String helloUser() {
        return "Welcome to the flower shop, dear customer! Browse our bouquets.";
    }

    @GetMapping("/hello/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "Hello, Admin! Fresh roses just arrived, time to update the stock.";
    }

    @GetMapping("/hello/superadmin")
    @Secured("ROLE_SUPERADMIN")
    public String helloSuperadmin() {
        return "Greetings, Superadmin! The whole garden is in your hands.";
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> me(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public List<Flower> lowStock() {
        return service.findAll().stream()
                .filter(f -> f.getStock() < 20)
                .toList();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPERADMIN')")
    public List<Flower> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPERADMIN')")
    public Flower getOne(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public Flower create(@RequestBody Flower flower) {
        return service.create(flower);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public Flower update(@PathVariable String id, @RequestBody Flower flower) {
        return service.update(id, flower);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_SUPERADMIN")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}