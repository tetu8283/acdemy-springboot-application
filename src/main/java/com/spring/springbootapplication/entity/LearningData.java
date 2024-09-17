package com.spring.springbootapplication.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LearningData {
    
    private Long id;

    @NotBlank
    private int user_id;


    @NotNull(message = "カテゴリIDは必ず入力してください")
    private Integer categoryId;
    
}
