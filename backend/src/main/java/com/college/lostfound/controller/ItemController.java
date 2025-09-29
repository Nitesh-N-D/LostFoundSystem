package com.college.lostfound.controller;

import com.college.lostfound.model.Item;
import com.college.lostfound.model.User;
import com.college.lostfound.service.ItemService;
import com.college.lostfound.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public ItemController(ItemService itemService, UserService userService){
        this.itemService = itemService;
        this.userService = userService;
    }

    @GetMapping
    public List<Item> all(){ return itemService.all(); }

    @GetMapping("/search")
    public List<Item> search(@RequestParam(value="q", required=false) String q){
        if(q==null || q.isBlank()) return itemService.all();
        return itemService.search(q);
    }

    @PostMapping("/report")
    public ResponseEntity<?> report(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam String location,
            @RequestParam(defaultValue = "false") boolean found,
            @RequestParam String reporterEmail,
            @RequestPart(required=false) MultipartFile[] images
    ) throws Exception {
        User reporter = userService.findByEmail(reporterEmail).orElse(null);
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setCategory(category);
        item.setLocation(location);
        item.setFound(found);
        item.setCreatedAt(LocalDateTime.now());
        if(reporter != null) item.setReporter(reporter);

        // handle images
        List<String> imageUrls = new ArrayList<>();
        if(images != null){
            Files.createDirectories(Paths.get(uploadDir));
            for(MultipartFile m : images){
                if(m.isEmpty()) continue;
                String original = StringUtils.cleanPath(Objects.requireNonNull(m.getOriginalFilename()));
                String ext = "";
                int idx = original.lastIndexOf('.');
                if(idx>0) ext = original.substring(idx);
                String fname = UUID.randomUUID().toString() + ext;
                Path target = Paths.get(uploadDir).resolve(fname);
                Files.copy(m.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
                // In production, return CDN or absolute URL; here we return path
                imageUrls.add("/uploads/" + fname);
            }
        }
        item.setImages(imageUrls);
        itemService.save(item);
        return ResponseEntity.ok(Map.of("message","reported","item",item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        return itemService.get(id)
                .map(i -> ResponseEntity.ok(i))
                .orElse(ResponseEntity.notFound().build());
    }
}
