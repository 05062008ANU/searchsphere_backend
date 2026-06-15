package com.searchsphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.searchsphere.entity.CartItem;
import com.searchsphere.entity.Item;
import com.searchsphere.repository.CartRepository;
import com.searchsphere.repository.ItemRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long itemId = Long.valueOf(payload.get("itemId").toString());
        int qty = payload.get("quantity") != null ? Integer.parseInt(payload.get("quantity").toString()) : 1;

        CartItem existing = cartRepository.findByUserIdAndItemId(userId, itemId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
            return ResponseEntity.ok(cartRepository.save(existing));
        }

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) return ResponseEntity.notFound().build();

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setItem(item);
        cartItem.setQuantity(qty);
        return ResponseEntity.ok(cartRepository.save(cartItem));
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<?> updateQty(@PathVariable Long cartItemId, @RequestBody Map<String, Object> payload) {
        return cartRepository.findById(cartItemId).map(ci -> {
            ci.setQuantity(Integer.parseInt(payload.get("quantity").toString()));
            return ResponseEntity.ok((Object) cartRepository.save(ci));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartItemId) {
        if (!cartRepository.existsById(cartItemId)) return ResponseEntity.notFound().build();
        cartRepository.deleteById(cartItemId);
        return ResponseEntity.ok(Map.of("message", "Removed from cart"));
    }
}
