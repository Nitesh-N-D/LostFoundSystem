package com.college.lostfound.service;

import com.college.lostfound.model.Item;
import com.college.lostfound.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository repo;
    public ItemService(ItemRepository repo){ this.repo = repo; }

    public Item save(Item i){ return repo.save(i); }
    public Optional<Item> get(Long id){ return repo.findById(id); }
    public List<Item> search(String q){ return repo.findByTitleContainingIgnoreCase(q); }
    public List<Item> all(){ return repo.findAll(); }
}
