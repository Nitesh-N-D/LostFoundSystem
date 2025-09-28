package com.college.lostfound.controller;

import com.college.lostfound.model.Claim;
import com.college.lostfound.repository.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin
public class ClaimController {

    private final ClaimRepository repo;

    @Autowired
    private JavaMailSender mailSender;

    public ClaimController(ClaimRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Claim create(@RequestBody Claim claim) {
        claim.setStatus("PENDING");
        return repo.save(claim);
    }

    @PutMapping("/{id}/approve")
    public Claim approve(@PathVariable Long id) {
        Claim claim = repo.findById(id).orElseThrow();
        claim.setStatus("APPROVED");

        // Send email
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(claim.getClaimer().getEmail());
        msg.setSubject("Claim Approved");
        msg.setText("Your claim for item '" + claim.getItem().getTitle() + "' is approved.");
        mailSender.send(msg);

        return repo.save(claim);
    }

    @GetMapping
    public List<Claim> all() {
        return repo.findAll();
    }
}
