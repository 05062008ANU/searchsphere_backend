package com.searchsphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.searchsphere.entity.Item;
import com.searchsphere.entity.Category;
import com.searchsphere.repository.ItemRepository;
import com.searchsphere.repository.CategoryRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable Long id) {
        return itemRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestBody Map<String, Object> payload) {
        Item item = new Item();
        item.setTitle((String) payload.get("title"));
        item.setDescription((String) payload.get("description"));
        if (payload.get("price") != null)
            item.setPrice(Double.valueOf(payload.get("price").toString()));
        item.setBrand((String) payload.get("brand"));
        item.setImageUrl((String) payload.get("imageUrl"));

        if (payload.get("categoryId") != null) {
            Long catId = Long.valueOf(payload.get("categoryId").toString());
            categoryRepository.findById(catId).ifPresent(item::setCategory);
        }
        return ResponseEntity.ok(itemRepository.save(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        return itemRepository.findById(id).map(item -> {
            if (payload.get("title") != null) item.setTitle((String) payload.get("title"));
            if (payload.get("description") != null) item.setDescription((String) payload.get("description"));
            if (payload.get("price") != null) item.setPrice(Double.valueOf(payload.get("price").toString()));
            if (payload.get("brand") != null) item.setBrand((String) payload.get("brand"));
            if (payload.get("imageUrl") != null) item.setImageUrl((String) payload.get("imageUrl"));
            if (payload.get("categoryId") != null) {
                Long catId = Long.valueOf(payload.get("categoryId").toString());
                categoryRepository.findById(catId).ifPresent(item::setCategory);
            }
            return ResponseEntity.ok((Object) itemRepository.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        if (!itemRepository.existsById(id)) return ResponseEntity.notFound().build();
        itemRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Item deleted successfully"));
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        return categoryRepository.findById(id).map(category -> {
            if (payload.get("name") != null) category.setName((String) payload.get("name"));
            return ResponseEntity.ok((Object) categoryRepository.save(category));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) return ResponseEntity.notFound().build();
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Category deleted successfully"));
    }
}
