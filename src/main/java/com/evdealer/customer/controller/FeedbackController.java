package com.evdealer.customer.controller;

import com.evdealer.customer.entity.Feedback;
import com.evdealer.customer.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) { this.service = service; }

    @GetMapping
    public List<Feedback> all() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> get(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Feedback> create(@RequestBody Feedback f) {
        Feedback created = service.create(f);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> update(@PathVariable Long id, @RequestBody Feedback f) {
        if (!service.getById(id).isPresent()) return ResponseEntity.notFound().build();
        Feedback updated = service.update(id, f);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
