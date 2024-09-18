package com.spring.springbootapplication.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Category {

    private Integer categoryId;

    @NotBlank
    private Integer categoryType;

    @NotBlank(message = "カテゴリ名は必ず入力してください")
    @Size(max = 20, message = "カテゴリ名は20文字以内で入力してください")
    private String categoryName;

    // ゲッターとセッター
    public Integer getCategoryId() {
        return categoryId;
    }

    //これでカテゴリがバックエンド、フロント、インフラかを判定
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

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
