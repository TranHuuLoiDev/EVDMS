package com.evdealer.customer.controller;

import com.evdealer.customer.entity.TestDrive;
import com.evdealer.customer.service.TestDriveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testdrives")
@CrossOrigin
public class TestDriveController {

    private final TestDriveService service;

    public TestDriveController(TestDriveService service) { this.service = service; }

    @GetMapping
    public List<TestDrive> all() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<TestDrive> get(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TestDrive> create(@RequestBody TestDrive t) {
        TestDrive created = service.create(t);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestDrive> update(@PathVariable Long id, @RequestBody TestDrive t) {
        if (!service.getById(id).isPresent()) return ResponseEntity.notFound().build();
        TestDrive updated = service.update(id, t);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
