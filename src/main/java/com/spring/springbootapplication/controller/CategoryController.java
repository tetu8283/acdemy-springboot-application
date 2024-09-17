package com.spring.springbootapplication.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.springbootapplication.entity.Category;
import com.spring.springbootapplication.entity.Users;
import com.spring.springbootapplication.mapper.CategoryMapper;
import com.spring.springbootapplication.mapper.UsersMapper;

@Controller
public class CategoryController {

    // データベースにアクセスするためのユーザーマッパー
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 項目新規作成
     * @param id
     * @param categoryType 0: フロントエンド, 1: バックエンド, 2: インフラ
     * @param mav 
     * @return 
     */
    @GetMapping("/users/new/category/{id}")
    public ModelAndView newCategory(@PathVariable Long id, 
                                @RequestParam("categoryType") int categoryType, 
                                ModelAndView mav) {
        Users user = usersMapper.findById(id);
        mav.setViewName("CategoryNew");
        mav.addObject("user", user);
        mav.addObject("user_name", user.getUserName());
        mav.addObject("categoryTyle", categoryType);

        // 0, 1, 2でカテゴリを判定してaddする文字を判定している
        String categoryTypeName;
        switch (categoryType) {
            case 0:
                categoryTypeName = "フロントエンド";
                break;
            case 1:
                categoryTypeName = "バックエンド";
                break;
            case 2:
                categoryTypeName = "インフラ";
                break;
            default:
                categoryTypeName = "カテゴリがありません";
        }
        mav.addObject("categoryTypeName", categoryTypeName); 

        return mav;
    }

    
    @PostMapping("/users/new/category/{id}")
    public ModelAndView createCategory(@PathVariable Long id, 
                                        @RequestParam("categoryName") String categoryName,
                                        @RequestParam("categoryType") int categoryType,
                                        ModelAndView mav) {
        Users user = usersMapper.findById(id);
        
        // 新しいカテゴリの作成
        Category category = new Category();
        category.setCategoryId(categoryType); 
        category.setCategoryName(categoryName);

        categoryMapper.insertCategory(category); 

        mav.setViewName("CategoryNew");
        mav.addObject("user", user);
        mav.addObject(categoryName, category);
        mav.addObject("user_name", user.getUserName());

        return mav;
    }


    /**
     * 項目編集
     * @param id 
     * @param mav
     * @return 
     */
    @GetMapping("/users/edit/category/{id}")
    public ModelAndView categoryeditForm(@PathVariable Long id, ModelAndView mav) {
        Users user = usersMapper.findById(id);
        Calendar calendar = Calendar.getInstance();
        // 今月、先月、先々月の月を取得
        int thisMonth = calendar.get(Calendar.MONTH) + 1; 
        int lastMonth = thisMonth - 1 <= 0 ? 12 : thisMonth - 1; // 1月の前は12月
        int monthBeforeLast = lastMonth - 1 <= 0 ? 12 : lastMonth - 1; // 1月の前は12月

        mav.setViewName("CategoryEdit");
        mav.addObject("user", user);
        mav.addObject("user_name", user.getUserName());
        mav.addObject("thisMonth", thisMonth);
        mav.addObject("lastMonth", lastMonth);
        mav.addObject("monthBeforeLast", monthBeforeLast);
        return mav;
    }

    @GetMapping("users/category/list")
    public ModelAndView categoryList(ModelAndView mav){
        mav.setViewName("CategoryList");
        List<Category> categoryList = categoryMapper.findAll();
        mav.addObject("categoryList", categoryList);
        return mav;
    }
}
