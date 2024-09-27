package com.spring.springbootapplication.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    // データベースにアクセスするためのマッパー
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private LearningDataMapper learningDataMapper;

    /**
     * 項目編集
     * @param id
     * @param selectedMonth
     * @param mav
     * @return
     */
    @GetMapping("/users/edit/category/{id}")
    public ModelAndView categoryEditForm(@PathVariable Integer id,
                                        @RequestParam(value="selectedMonth", required=false) Integer selectedMonth,
                                        ModelAndView mav) {
        Users dbUser = usersMapper.findById(id);
        Calendar calendar = Calendar.getInstance();

        int learningYear = calendar.get(Calendar.YEAR);  // 現在の年を取得
        // 以下で今月先月先々月を算出して取得
        int thisMonth = calendar.get(Calendar.MONTH) + 1; 
        int lastMonth = thisMonth - 1 <= 0 ? 12 : thisMonth - 1;
        int monthBeforeLast = lastMonth - 1 <= 0 ? 12 : lastMonth - 1;

        // はじめにCategoryEdit.htmlを開いた際に、デフォルトで今月のデータが表示されるようにしている
        if (selectedMonth == null) {
            selectedMonth = thisMonth;
        }

        // カテゴリごとのリストを取得
        List<Category> backEndCategories = categoryMapper.findCategoriesByTypeAndUserId(1, dbUser.getUserId());
        List<Category> frontEndCategories = categoryMapper.findCategoriesByTypeAndUserId(0, dbUser.getUserId());
        List<Category> infraCategories = categoryMapper.findCategoriesByTypeAndUserId(2, dbUser.getUserId());

        // リストがnullの場合は空のリストを設定。(空のリストを設定しないとエラーになった)
        if (backEndCategories == null) {
            backEndCategories = new ArrayList<>();
        }
        if (frontEndCategories == null) {
            frontEndCategories = new ArrayList<>();
        }
        if (infraCategories == null) {
            infraCategories = new ArrayList<>();
        }

        // ユーザーの全学習データを取得
        List<LearningData> learningDataList = learningDataMapper.findByUserId(id);
        if (learningDataList == null) {
            learningDataList = new ArrayList<>();
        }

        // categoryIdをキーに、学習時間を値とするマップを作成
        Map<Integer, Integer> categoryIdToLearningTime = new HashMap<>();
        
        Set<Integer> categoryIdsWithLearningData = new HashSet<>();

        for (LearningData learningData : learningDataList) {
             // 学習年と学習月が現在の年と選択した月と一致する場合に以下の処理を実行
            if (learningData.getLearningYear().equals(learningYear) && learningData.getLearningMonth().equals(selectedMonth)) {
                // カテゴリidをキーにして学習時間をマップに格納
                categoryIdToLearningTime.put(learningData.getCategoryId(), learningData.getLearningTime());
                // 学習データが存在するカテゴリidをセットに追加。(setは重複を許容しない特性がある)
                categoryIdsWithLearningData.add(learningData.getCategoryId());
            }
        }
        
        // バック、フロント、インフラの学習データが存在するカテゴリidでフィルターをかける
        backEndCategories = backEndCategories.stream()
            .filter(category -> categoryIdsWithLearningData.contains(category.getCategoryId()))
            .collect(Collectors.toList());

        frontEndCategories = frontEndCategories.stream()
            .filter(category -> categoryIdsWithLearningData.contains(category.getCategoryId()))
            .collect(Collectors.toList());

        infraCategories = infraCategories.stream()
            .filter(category -> categoryIdsWithLearningData.contains(category.getCategoryId()))
            .collect(Collectors.toList());

        mav.addObject("backEndCategories", backEndCategories);
        mav.addObject("frontEndCategories", frontEndCategories);
        mav.addObject("infraCategories", infraCategories);
        mav.addObject("categoryIdToLearningTime", categoryIdToLearningTime);
        mav.addObject("selectedMonth", selectedMonth);

        mav.setViewName("CategoryEdit");
        mav.addObject("user", dbUser);
        mav.addObject("thisMonth", thisMonth);
        mav.addObject("lastMonth", lastMonth);
        mav.addObject("monthBeforeLast", monthBeforeLast);
        mav.addObject("learningYear", learningYear);
        return mav;
    }

    /**
     * 項目新規作成ページ
     * @param id
     * @param categoryType 0: フロントエンド, 1: バックエンド, 2: インフラ
     * @param learningYear
     * @param selectedMonth
     * @param mav
     * @return
     */
    @GetMapping("/users/new/category/{id}")
    public ModelAndView newCategory(@PathVariable Integer id, 
                                    @RequestParam("categoryType") int categoryType, 
                                    @RequestParam("learningYear") int learningYear,
                                    @RequestParam("selectedMonth") int selectedMonth,
                                    ModelAndView mav) {
        Users dbUser = usersMapper.findById(id);

        mav.setViewName("CategoryNew");
        mav.addObject("user", dbUser);
        mav.addObject("categoryType", categoryType);
        mav.addObject("learningYear", learningYear);
        mav.addObject("selectedMonth", selectedMonth);

        // カテゴリタイプ名を設定
        String categoryTypeName = switch (categoryType) {
            case 0 -> "フロントエンド";
            case 1 -> "バックエンド";
            case 2 -> "インフラ";
            default -> "カテゴリがありません";
        };
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
     * @param learningTime
     * @param mav
     * @return
     */
    @PostMapping("/users/new/category/{id}")
    public ModelAndView createCategory(@PathVariable Integer id,
                                        @RequestParam("categoryName") String categoryName,
                                        @RequestParam("categoryType") String categoryType,
                                        @RequestParam("learningYear") String learningYear,
                                        @RequestParam("learningMonth") String learningMonth,
                                        // 項目のみ作成する際に、学習時間をデフォルトで0にする
                                        @RequestParam(value = "learningTime", defaultValue = "0") String learningTime,
                                        ModelAndView mav) {
        Users dbUser = usersMapper.findById(id);
        int categoryTypeInt = Integer.parseInt(categoryType);

        // カテゴリの存在チェック
        Category existingCategory = categoryMapper.findByCategoryNameAndTypeAndUserId(categoryName, categoryTypeInt, id);

        String categoryTypeName = switch (categoryTypeInt) {
            case 0 -> "フロントエンド";
            case 1 -> "バックエンド";
            case 2 -> "インフラ";
            default -> "カテゴリがありません";
        };

        // 登録しようとしたカテゴリがすでに存在するかどうかを判定
        if (existingCategory != null) {
            mav.addObject("errorMessage", categoryName + "はすでに存在しています");
            mav.setViewName("CategoryNew");
            mav.addObject("user", dbUser);
            mav.addObject("categoryType", categoryTypeInt);
            mav.addObject("learningYear", learningYear);
            mav.addObject("learningMonth", learningMonth);
            mav.addObject("selectedMonth", learningMonth);
            mav.addObject("categoryTypeName", categoryTypeName);
            return mav;
        }

        Category category = new Category();
        LearningData learningData = new LearningData();

        category.setCategoryType(Integer.valueOf(categoryType));
        category.setCategoryName(categoryName);
        category.setUserId(id);

        categoryMapper.insertCategory(category); // カテゴリ追加

        // 自動で設定されたidを取得するために再度カテゴリ取得
        Category insertedCategory = categoryMapper.findCategoryById(category.getCategoryId());

        learningData.setUserId(id);
        learningData.setCategoryId(insertedCategory.getCategoryId());
        learningData.setLearningYear(Integer.valueOf(learningYear)); 
        learningData.setLearningMonth(Integer.valueOf(learningMonth));
        learningData.setLearningTime(Integer.valueOf(learningTime));

        learningDataMapper.insertLearningData(learningData); // 学習データ追加
        // CategoryEditにリダイレクト
        return new ModelAndView("redirect:/users/edit/category/" + id + "?selectedMonth=" + learningMonth);
    }

    /**
     * 学習データ更新
     * @param id
     * @param categoryId
     * @param learningTime
     * @param selectedMonth
     * @param mav
     * @return
     */
    @PostMapping("/users/edit/category/updateTime/{id}")
    public ModelAndView updateLearningTime(@PathVariable Integer id,
                                            @RequestParam("categoryId") Integer categoryId,
                                            @RequestParam("learningTime") Integer learningTime,
                                            @RequestParam("selectedMonth") Integer selectedMonth,
                                            ModelAndView mav) {
        Calendar calendar = Calendar.getInstance();
        int learningYear = calendar.get(Calendar.YEAR);

        // 既存の学習データを取得
        LearningData existingLearningData = learningDataMapper.findByUserIdAndCategoryIdAndYearAndMonth(id, categoryId, learningYear, selectedMonth);

        if (existingLearningData != null) {
            existingLearningData.setLearningTime(learningTime);
            learningDataMapper.updateLearningData(existingLearningData);
        } else {
            LearningData newLearningData = new LearningData();
            newLearningData.setUserId(id);
            newLearningData.setCategoryId(categoryId);
            newLearningData.setLearningYear(learningYear);
            newLearningData.setLearningMonth(selectedMonth);
            newLearningData.setLearningTime(learningTime);
            learningDataMapper.insertLearningData(newLearningData);
        }

        return new ModelAndView("redirect:/users/edit/category/" + id + "?selectedMonth=" + selectedMonth);
    }

    /**
     * カテゴリ削除
     * @param id
     * @param categoryId
     * @param selectedMonth
     * @param mav
     * @return
     */
    @PostMapping("/users/edit/category/delete/{id}")
    public ModelAndView deleteCategory(@PathVariable Integer id,
                                        @RequestParam("categoryId") Integer categoryId,
                                        @RequestParam("selectedMonth") Integer selectedMonth,
                                        ModelAndView mav) {

        learningDataMapper.deleteByCategoryIdAndUserId(categoryId, id); // 学習データ削除

        categoryMapper.deleteByCategoryIdAndUserId(categoryId, id); // カテゴリ削除

        return new ModelAndView("redirect:/users/edit/category/" + id + "?selectedMonth=" + selectedMonth);
    }
}
