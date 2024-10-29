package LD01.springboot_thymeleaf.Repository;

import LD01.springboot_thymeleaf.Entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByNameContaining(String name);

    Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);
}
