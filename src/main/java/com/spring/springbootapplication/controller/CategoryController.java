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
import com.spring.springbootapplication.entity.LearningData;
import com.spring.springbootapplication.entity.Users;
import com.spring.springbootapplication.mapper.CategoryMapper;
import com.spring.springbootapplication.mapper.LearningDataMapper;
import com.spring.springbootapplication.mapper.UsersMapper;

@Controller
public class CategoryController {

    // データベースにアクセスするためのユーザーマッパー
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private LearningDataMapper learningDataMapper;

    /**
     * 項目編集
     * @param id 
     * @param mav
     * @return 
     */
    @GetMapping("/users/edit/category/{id}")
    public ModelAndView categoryeditForm(@PathVariable Integer id, ModelAndView mav) {
        Users user = usersMapper.findById(id);
        Calendar calendar = Calendar.getInstance();

        // CategoryNewにパッピングする現在の年の情報
        int learningYear = calendar.get(Calendar.YEAR);

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

    /**
     * 項目新規作成ページ
     * @param id
     * @param categoryType 0: フロントエンド, 1: バックエンド, 2: インフラ
     * @param mav 
     * @return 
     */
    @GetMapping("/users/new/category/{id}")
    public ModelAndView newCategory(@PathVariable Integer id, 
                                @RequestParam("categoryType") int categoryType, 
                                ModelAndView mav) {
        Users user = usersMapper.findById(id);
        mav.setViewName("CategoryNew");
        mav.addObject("user", user);
        mav.addObject("user_name", user.getUserName());
        mav.addObject("categoryTyle", categoryType);

        // 0, 1, 2でカテゴリを判定後、addする
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
        // categoryTypeNameにはフロント、バック、インフラのどれかが入る
        // categoryNameにはJavaやAWSなどが入る
        mav.addObject("categoryTypeName", categoryTypeName); 

        return mav;
    }

    /**
     * 新規項目作成
     * @param id
     * @param categoryName
     * @param categoryType
     * @param learningYear
     * @param learningMonth
     * @param mav
     * @return
     */
    @PostMapping("/users/new/category/{id}")
    public ModelAndView createCategory(@PathVariable Integer id, 
                                        @RequestParam("categoryName") String categoryName, // カテゴリ名受け取り
                                        @RequestParam("categoryType") int categoryType, // カテゴリタイプ受け取り
                                        @RequestParam("learningYear") int learningYear, // 学習年受け取り
                                        @RequestParam("learningMonth") int learningMonth, // プルダウンで選択された月を受け取り
                                        @RequestParam("learningTime") int learningTime, //入力された学習時間受け取り
                                        ModelAndView mav) {
        Users user = usersMapper.findById(id);

        Category category = new Category();
        LearningData learningData = new LearningData();

        category.setCategoryType(categoryType); // カテゴリタイプセット
        category.setCategoryName(categoryName); // カテゴリ名セット

        learningData.setUserId(id);
        learningData.setCategoryId(category.getCategoryId());
        learningData.setLearningYear(learningYear);
        learningData.setLearningMonth(learningMonth);
        learningData.setLearningTime(learningTime);

        categoryMapper.insertCategory(category); // insert実行

        /* 以下のインサート文を実行できるようにする */

        // learningDataMapper.insertLearningData();

        mav.setViewName("CategoryNew");
        mav.addObject("user", user);
        mav.addObject(categoryName, category);
        mav.addObject("user_name", user.getUserName());
        mav.addObject("selectedMonth", learningMonth);
        return mav;
    }

}
