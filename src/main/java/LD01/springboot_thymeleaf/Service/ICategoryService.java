package LD01.springboot_thymeleaf.Service;

import LD01.springboot_thymeleaf.Entity.CategoryEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    void delete(CategoryEntity entity);
    void deleteById(Long id);
    long count();
    <S extends CategoryEntity> Optional<S> findOne(Example<S> example);
    Optional<CategoryEntity> findById(Long id);
    List<CategoryEntity> findAllById(Iterable<Long> ids);
    List<CategoryEntity> findAll(Sort sort);
    Page<CategoryEntity> findAll(Pageable pageable);
    List<CategoryEntity> findAll();
    <S extends CategoryEntity> S save(S entity);
    Page<CategoryEntity> findByCategoryNameContaining(String name, Pageable pageable);
    List<CategoryEntity> findByCategoryNameContaining(String name);
    public void deleteALl();
}
