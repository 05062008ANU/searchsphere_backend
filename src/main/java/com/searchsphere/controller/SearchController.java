package com.searchsphere.controller;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.searchsphere.entity.Item;
import com.searchsphere.entity.SearchLog;
import com.searchsphere.repository.ItemRepository;
import com.searchsphere.repository.SearchLogRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SearchLogRepository searchLogRepository;

    @GetMapping("/search")
    public List<Item> search(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Long categoryId,
            HttpServletRequest request
    ) {
        List<Item> all = itemRepository.findAll();
        String q = (query == null ? "" : query.trim().toLowerCase(Locale.ROOT));

        List<Item> results = all.stream().filter(item -> {
            if (!q.isEmpty()) {
                String haystack = (
                    safe(item.getTitle()) + " " +
                    safe(item.getDescription()) + " " +
                    safe(item.getBrand()) + " " +
                    (item.getCategory() != null ? safe(item.getCategory().getName()) : "")
                ).toLowerCase(Locale.ROOT);
                boolean matches = false;
                for (String word : q.split("\\s+")) {
                    if (!word.isEmpty() && haystack.contains(word)) { matches = true; break; }
                }
                if (!matches) return false;
            }
            if (minPrice != null && item.getPrice() != null && item.getPrice() < minPrice) return false;
            if (maxPrice != null && item.getPrice() != null && item.getPrice() > maxPrice) return false;
            if (brand != null && !brand.isEmpty()) {
                if (item.getBrand() == null || !item.getBrand().equalsIgnoreCase(brand)) return false;
            }
            if (categoryId != null) {
                if (item.getCategory() == null || !item.getCategory().getId().equals(categoryId)) return false;
            }
            return true;
        }).collect(Collectors.toList());

        // ✅ Log search to MongoDB (only if user typed something)
        if (!q.isEmpty()) {
            String username = request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName() : "anonymous";
            searchLogRepository.save(new SearchLog(q, results.size(), username));
        }

        return results;
    }

    private String safe(String s) { return s == null ? "" : s; }
}