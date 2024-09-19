package com.spring.springbootapplication.mapper;

import java.util.List;

import com.spring.springbootapplication.entity.LearningData;

public interface LearningDataMapper {
/**
 * 
 * @param learningData
 */
    void insertLearningData(LearningData learningData);

    List<LearningData> findAll();
} 