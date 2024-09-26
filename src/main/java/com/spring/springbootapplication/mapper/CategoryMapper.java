package com.spring.springbootapplication.mapper;

import java.util.List;

import com.spring.springbootapplication.entity.Category;


public interface CategoryMapper {
    /**
     * 
     * @param category
     */
    void insertCategory(Category category);

    /**
     * 
     * @param categoryId
     * @return
     */
    Category findCategoryById(Integer categoryId); 

    /**
     * カテゴリタイプでデータを取得
     * @param categoryType
     * @return
     */
    List<Category> findCategoriesByType(int categoryType);

    List<Category> findAll();
}
