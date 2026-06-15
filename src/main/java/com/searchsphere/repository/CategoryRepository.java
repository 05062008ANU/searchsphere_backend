package com.searchsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.searchsphere.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
