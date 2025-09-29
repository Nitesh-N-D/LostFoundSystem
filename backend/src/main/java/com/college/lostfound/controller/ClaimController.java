package com.college.lostfound.controller;

import com.college.lostfound.model.Claim;
import com.college.lostfound.model.Item;
import com.college.lostfound.model.User;
import com.college.lostfound.repository.ClaimRepository;
import com.college.lostfound.service.ItemService;
import com.college.lostfound.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    private final ClaimRepository claimRepo;
    private final ItemService itemService;
    private final UserService userService;

    public ClaimController(ClaimRepository claimRepo, ItemService itemService, UserService userService){
        this.claimRepo = claimRepo;
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestClaim(@RequestBody Map<String,String> body){
        Long itemId = Long.parseLong(body.get("itemId"));
        String claimerEmail = body.get("claimerEmail");
        String note = body.getOrDefault("note","");

        Item item = itemService.get(itemId).orElse(null);
        User claimer = userService.findByEmail(claimerEmail).orElse(null);
        if(item==null) return ResponseEntity.badRequest().body(Map.of("error","Item not found"));
        if(claimer==null) return ResponseEntity.badRequest().body(Map.of("error","User not found"));

        Claim c = Claim.builder()
                .item(item)
                .claimer(claimer)
                .status("PENDING")
                .note(note)
                .createdAt(LocalDateTime.now())
                .build();
        claimRepo.save(c);
        return ResponseEntity.ok(Map.of("message","claim requested", "claim", c));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String,String> body){
        return claimRepo.findById(id).map(c -> {
            c.setStatus(body.getOrDefault("status", c.getStatus()));
            claimRepo.save(c);
            return ResponseEntity.ok(Map.of("message","updated","claim",c));
        }).orElse(ResponseEntity.notFound().build());
    }
}
