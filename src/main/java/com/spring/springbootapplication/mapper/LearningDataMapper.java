package com.spring.springbootapplication.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.springbootapplication.entity.LearningData;

public interface LearningDataMapper {
/**
 * 
 * @param learningData
 */
    void insertLearningData(LearningData learningData);

    // バックエンド、フロント、インフラをwhere categoryTyle = 0, 1, 2で判別してlistに格納
    /**
     * ユーザーIDで学習データを取得
     */
    List<LearningData> findByUserId(Integer userId);

    /**
     * ユーザーID、カテゴリID、学習年、学習月で学習データを取得
     */
    LearningData findByUserIdAndCategoryIdAndYearAndMonth(Integer userId, Integer categoryId, Integer learningYear, Integer learningMonth);

    /**
     * 学習データ更新
     */
    void updateLearningData(LearningData learningData);

    /**
     * カテゴリIDとユーザーIDで学習データを削除
     * @param categoryId
     * @param userId
     */
    void deleteByCategoryIdAndUserId(@Param("categoryId") Integer categoryId, @Param("userId") Integer userId);
} 