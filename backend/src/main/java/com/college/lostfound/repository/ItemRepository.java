package com.college.lostfound.repository;

import com.college.lostfound.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByTitleContainingIgnoreCase(String q);
    List<Item> findByCategory(String category);
}
