package com.searchsphere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.searchsphere.entity.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);

    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId AND c.item.id = :itemId")
    CartItem findByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
