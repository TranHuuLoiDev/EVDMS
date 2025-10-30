package com.evdealer.customer.service;

import com.evdealer.customer.entity.TestDrive;
import com.evdealer.customer.repository.TestDriveRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestDriveService {

    private final TestDriveRepository repo;

    public TestDriveService(TestDriveRepository repo) { this.repo = repo; }

    public List<TestDrive> getAll() { return repo.findAll(); }
    public Optional<TestDrive> getById(Long id) { return repo.findById(id); }
    public TestDrive create(TestDrive t) { return repo.save(t); }
    public TestDrive update(Long id, TestDrive t) {
        t.setTest_drive_id(id);
        return repo.save(t);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
