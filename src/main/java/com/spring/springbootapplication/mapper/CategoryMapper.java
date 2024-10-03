package com.spring.springbootapplication.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.springbootapplication.entity.Category;

public interface CategoryMapper {
    /**
     * カテゴリを挿入
     * @param category
     */
    void insertCategory(Category category);

    /**
     * カテゴリidでカテゴリを検索
     * @param categoryId
     * @return
     */
    Category findCategoryById(Integer categoryId); 


    Category findByCategoryIdAndUserId(Integer categoryId, Integer userId); 

    /**
     * カテゴリタイプとユーザーidでカテゴリを取得
     * @param categoryType
     * @param userId
     * @return
     */
    List<Category> findCategoriesByTypeAndUserId(@Param("categoryType") int categoryType, @Param("userId") Integer userId);

    /**
     * カテゴリ名、カテゴリタイプ、ユーザーidでカテゴリを検索
     * @param categoryName
     * @param categoryType
     * @param userId
     * @return
     */
    Category findByCategoryNameAndTypeAndUserId(@Param("categoryName") String categoryName, 
                                                @Param("categoryType") Integer categoryType, 
                                                @Param("userId") Integer userId);

    /**
     * カテゴリを削除（カテゴリidとユーザーidで確認）
     * @param categoryId
     * @param userId
     */
    void deleteByCategoryIdAndUserId(@Param("categoryId") Integer categoryId, @Param("userId") Integer userId);

    /**
     * ユーザーidでカテゴリを取得
     * @param userId
     * @return
     */
    List<Category> findCategoriesByUserId(Integer userId);
}
