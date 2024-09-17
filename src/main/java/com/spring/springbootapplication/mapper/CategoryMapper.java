package com.spring.springbootapplication.mapper;

import java.util.List;

import com.spring.springbootapplication.entity.Category;


public interface CategoryMapper {
    /**
     * 
     * @param category
     */
    void insertCategory(Category category);

    List<Category> findAll();
}
