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
     * カテゴリIDでカテゴリを検索
     * @param categoryId
     * @return
     */
    Category findCategoryById(Integer categoryId); 

    /**
     * カテゴリタイプとユーザIDでデータを取得
     * @param categoryType
     * @param userId
     * @return
     */
    List<Category> findCategoriesByTypeAndUserId(@Param("categoryType") int categoryType, @Param("userId") Integer userId);

    /**
     * カテゴリ名、タイプ、ユーザIDでカテゴリ検索
     * @param categoryName
     * @param categoryType
     * @param userId
     * @return
     */
    Category findByCategoryNameAndTypeAndUserId(@Param("categoryName") String categoryName, @Param("categoryType") Integer categoryType, @Param("userId") Integer userId);

    /**
     * カテゴリを削除（ユーザIDで確認）
     * @param categoryId
     * @param userId
     */
    void deleteByCategoryIdAndUserId(@Param("categoryId") Integer categoryId, @Param("userId") Integer userId);

    /**
     * ユーザーIDでカテゴリを取得
     * @param userId
     * @return
     */
    List<Category> findCategoriesByUserId(Integer userId);

}
