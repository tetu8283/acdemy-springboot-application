package com.spring.springbootapplication.entity;

import jakarta.validation.constraints.NotNull;

public class LearningData {
    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer learningYear;

    @NotNull
    private Integer learningMonth;

    @NotNull
    private Integer learningTime;


    // ゲッターとセッター
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLearningYear() {
        return learningYear;
    }

    public void setLearningYear(Integer learningYear) {
        this.learningYear = learningYear;
    }

    public Integer getLearningMonth() {
        return learningMonth;
    }

    public void setLearningMonth(Integer learningMonth) {
        this.learningMonth = learningMonth;
    }

    public Integer getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(Integer learningTime) {
        this.learningTime = learningTime;
    }
}
