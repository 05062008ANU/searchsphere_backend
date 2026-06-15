package com.searchsphere.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.searchsphere.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Use a separate LEFT JOIN (not FETCH) for the WHERE clause filtering,
    // and LEFT JOIN FETCH only for loading the category data.
    // This avoids Hibernate's restriction on using FETCH-join aliases in WHERE.
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.category cat WHERE " +
        "(:query = '' OR LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
        "  OR LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
        "  OR LOWER(i.brand) LIKE LOWER(CONCAT('%', :query, '%')) " +
        "  OR LOWER(cat.name) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
        "(:minPrice IS NULL OR i.price >= :minPrice) AND " +
        "(:maxPrice IS NULL OR i.price <= :maxPrice) AND " +
        "(:brand IS NULL OR LOWER(i.brand) = LOWER(:brand)) AND " +
        "(:categoryId IS NULL OR i.category.id = :categoryId)")
    List<Item> searchWithFilters(
        @Param("query") String query,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("brand") String brand,
        @Param("categoryId") Long categoryId
    );
}
