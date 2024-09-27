package com.spring.springbootapplication.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Category {

    private Integer categoryId;

    @NotNull
    private Integer userId;

    @NotBlank
    private Integer categoryType;

    @NotBlank(message = "カテゴリ名は必ず入力してください")
    @Size(max = 20, message = "カテゴリ名は20文字以内で入力してください")
    private String categoryName;

    // ゲッターとセッター
    public Integer getCategoryId() {
        return categoryId;
    }

    
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    //これでカテゴリがバックエンド、フロント、インフラかを判定
    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
