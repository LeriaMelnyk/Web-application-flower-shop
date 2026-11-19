package floristry.shop.controller;

import floristry.shop.model.AuditLog;
import floristry.shop.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService service;

    @GetMapping
    public List<AuditLog> getAll() {
        return service.findAll();
    }
}