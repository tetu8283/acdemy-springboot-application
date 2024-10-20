package com.spring.springbootapplication.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.spring.springbootapplication.entity.LearningData;

public interface LearningDataMapper {
    /**
     * 学習データを挿入
     * @param learningData
     */
    void insertLearningData(LearningData learningData);

    /**
     * ユーザーidで学習データを取得
     * @param userId
     * @return
     */
    List<LearningData> findByUserId(Integer userId);

    /**
     * ユーザーid、カテゴリid、学習年、学習月で学習データを取得
     * @param userId
     * @param categoryId
     * @param learningYear
     * @param learningMonth
     * @return
     */
    LearningData findByUserIdAndCategoryIdAndYearAndMonth(@Param("userId") Integer userId, 
                                                        @Param("categoryId") Integer categoryId, 
                                                        @Param("learningYear") Integer learningYear, 
                                                        @Param("learningMonth") Integer learningMonth);

    /**
     * 学習データを更新
     * @param learningData
     */
    void updateLearningData(LearningData learningData);

    /**
     * カテゴリidとユーザーidで学習データを削除
     * @param categoryId
     * @param userId
     */
    void deleteByCategoryIdAndUserId(@Param("categoryId") Integer categoryId, @Param("userId") Integer userId);

    /**
     * カテゴリタイプごとの学習時間の合計を取得します。
     *
     * @param userId        ユーザーID
     * @param learningYear  学習年
     * @param learningMonth 学習月
     * @return カテゴリタイプごとの学習時間の合計リスト（Mapのリスト）
     */
    List<Map<String, Object>> getTotalLearningTimeByCategoryType(
        @Param("userId") Integer userId,
        @Param("learningYear") Integer learningYear,
        @Param("learningMonth") Integer learningMonth);
}
