package com.evdealer.customer.service;

import com.evdealer.customer.entity.Feedback;
import com.evdealer.customer.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository repo;

    public FeedbackService(FeedbackRepository repo) { this.repo = repo; }

    public List<Feedback> getAll() { return repo.findAll(); }
    public Optional<Feedback> getById(Long id) { return repo.findById(id); }
    public Feedback create(Feedback f) { return repo.save(f); }
    public Feedback update(Long id, Feedback f) {
        f.setFeedback_id(id);
        return repo.save(f);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
