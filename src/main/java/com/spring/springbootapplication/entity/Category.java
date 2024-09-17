package com.spring.springbootapplication.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Category {

    private Long id;

    @NotNull(message = "カテゴリIDは必ず入力してください")
    private Integer category_id;

    @NotBlank(message = "カテゴリ名は必ず入力してください")
    @Size(max = 20, message = "カテゴリ名は20文字以内で入力してください")
    private String category_name;

    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return category_id;
    }

    public void setCategoryId(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

}
