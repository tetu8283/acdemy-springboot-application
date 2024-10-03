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

/**
 * カテゴリ管理のコントローラー
 */
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
     * カテゴリ編集ページ表示
     * @param id
     * @param selectedMonth
     * @param mav 
     * @return 
     */
    @GetMapping("/users/edit/category/{id}")
    public ModelAndView categoryEditForm(@PathVariable Integer id,
                                        @RequestParam(value="selectedMonth", required=false) Integer selectedMonth,
                                        @RequestParam(value="deleteModal", required=false, defaultValue="false") boolean deleteModal, // 削除モーダル表示のフラグ
                                        @RequestParam(value="updateModal", required=false, defaultValue="false") boolean updateModal, // 更新モーダル表示のフラグ
                                        @RequestParam(value="deleteCategory", required=false) String deleteCategory, // 削除したカテゴリ名
                                        @RequestParam(value="updateCategory", required=false) String updateCategory, // 更新したカテゴリ名
                                        ModelAndView mav) {
        
        Users dbUser = usersMapper.findById(id);
        Calendar calendar = Calendar.getInstance();

        int learningYear = calendar.get(Calendar.YEAR);  // 現在の年を取得
        // 現在の月、先月、先々月を計算
        int thisMonth = calendar.get(Calendar.MONTH) + 1; 
        int lastMonth = thisMonth - 1 <= 0 ? 12 : thisMonth - 1;
        int monthBeforeLast = lastMonth - 1 <= 0 ? 12 : lastMonth - 1;

        // デフォルトで今月を選択
        // デフォルトで月をセットしないと、ログイン後に月情報がnullとなっているためデータが何も表示されない
        if (selectedMonth == null) {
            selectedMonth = thisMonth;
        }

        // カテゴリタイプごとにカテゴリリストを取得
        List<Category> backEndCategories = categoryMapper.findCategoriesByTypeAndUserId(1, dbUser.getUserId());
        List<Category> frontEndCategories = categoryMapper.findCategoriesByTypeAndUserId(0, dbUser.getUserId());
        List<Category> infraCategories = categoryMapper.findCategoriesByTypeAndUserId(2, dbUser.getUserId());

        // 各カテゴリリストがnullの場合は空リストを設定(これを書かないとエラーになる。からのリストはダメみたい)
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

        // カテゴリidをキーに、学習時間を値とするマップを作成(mapを使うと効率的)
        Map<Integer, Integer> categoryIdToLearningTime = new HashMap<>();
        
        // 学習データが存在するカテゴリidを保持するセットを作成
        Set<Integer> categoryIdsWithLearningData = new HashSet<>();

        for (LearningData learningData : learningDataList) {
            // 学習年と学習月が現在の年と選択した月と一致する場合に処理を実行
            if (learningData.getLearningYear().equals(learningYear) && learningData.getLearningMonth().equals(selectedMonth)) {
                // カテゴリidをキーにして学習時間をマップに格納
                categoryIdToLearningTime.put(learningData.getCategoryId(), learningData.getLearningTime());
                // 学習データが存在するカテゴリidをセットに追加（setは重複を許容しない）
                categoryIdsWithLearningData.add(learningData.getCategoryId());
            }
        }
        
        // 各カテゴリを学習データが存在するカテゴリidでフィルター
        backEndCategories = backEndCategories.stream()
            .filter(category -> categoryIdsWithLearningData.contains(category.getCategoryId()))
            .collect(Collectors.toList());

        frontEndCategories = frontEndCategories.stream()
            .filter(category -> categoryIdsWithLearningData.contains(category.getCategoryId()))
            .collect(Collectors.toList());

        infraCategories = infraCategories.stream()
            .filter(category -> categoryIdsWithLearningData.contains(category.getCategoryId()))
            .collect(Collectors.toList());

        // バック、フロント、インフラのデータを追加
        mav.addObject("backEndCategories", backEndCategories);
        mav.addObject("frontEndCategories", frontEndCategories);
        mav.addObject("infraCategories", infraCategories);

        // カテゴリidと学習時間のmapを追加
        mav.addObject("categoryIdToLearningTime", categoryIdToLearningTime);
        mav.addObject("selectedMonth", selectedMonth);

        mav.setViewName("CategoryEdit");
        mav.addObject("user", dbUser);
        mav.addObject("thisMonth", thisMonth);
        mav.addObject("lastMonth", lastMonth);
        mav.addObject("monthBeforeLast", monthBeforeLast);
        mav.addObject("learningYear", learningYear);
        mav.addObject("deleteModal", deleteModal); // 削除用モーダルを表示するためのフラグ
        mav.addObject("updateModal", updateModal); // 更新用モーダルを表示するためのフラグ
        
        // 削除されたカテゴリ名をモデルに追加
        if (deleteCategory != null) {
            mav.addObject("deletedCategoryName", deleteCategory);
        }

        // 更新されたカテゴリ名をモデルに追加
        if (updateCategory != null) {
            mav.addObject("updatedCategoryName", updateCategory);
        }

        return mav;
    }

    /**
     * 項目新規作成ページ表示
     * @param id 
     * @param categoryType カテゴリタイプ（0: フロントエンド, 1: バックエンド, 2: インフラ）
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
                                    @RequestParam(value="createModal", required=false, defaultValue="false") boolean createModal, // カテゴリ追加モーダル表示のフラグ
                                    @RequestParam(value="createdCategoryName", required=false) String createdCategoryName, // 追加したカテゴリ名
                                    @RequestParam(value="learningTime", required=false) String learningTime, // 追加した学習時間
                                    ModelAndView mav) {
        
        Users dbUser = usersMapper.findById(id);

        // カテゴリタイプ名を設定
        String categoryTypeName = switch (categoryType) {
            case 0 -> "フロントエンド";
            case 1 -> "バックエンド";
            case 2 -> "インフラ";
            default -> "カテゴリがありません";
        };

        mav.setViewName("CategoryNew");
        mav.addObject("user", dbUser);
        mav.addObject("categoryType", categoryType);
        mav.addObject("learningYear", learningYear);
        mav.addObject("selectedMonth", selectedMonth);
        mav.addObject("categoryTypeName", categoryTypeName); 
        mav.addObject("createModal", createModal); // 登録用モーダルを表示するためのフラグ

        // モーダルに表示する情報をモデルに追加
        if (createModal) {
            mav.addObject("createdCategoryName", createdCategoryName);
            mav.addObject("learningTime", learningTime);
        }

        return mav;
    }

    /**
     * カテゴリ新規作成
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
                                        @RequestParam(value = "learningTime", defaultValue = "0") String learningTime,
                                        ModelAndView mav) {
        Users dbUser = usersMapper.findById(id);
        int categoryTypeInt = Integer.parseInt(categoryType);

        // カテゴリの存在チェック（カテゴリ名、タイプ、ユーザーidで検索）
        Category existingCategory = categoryMapper.findByCategoryNameAndTypeAndUserId(categoryName, categoryTypeInt, id);

        if (existingCategory != null) {
            // 既存のカテゴリがある場合、指定された年と月に学習データが存在するか確認
            LearningData existingLearningData = learningDataMapper
                .findByUserIdAndCategoryIdAndYearAndMonth(id, existingCategory.getCategoryId(), Integer.valueOf(learningYear), Integer.valueOf(learningMonth));

            if (existingLearningData != null) {
                // 学習データが既に存在する場合、エラーメッセージを表示
                mav.addObject("errorMessage", categoryName + "はすでに" + learningMonth + "月に存在しています");
                mav.setViewName("CategoryNew"); 
                mav.addObject("user", dbUser);
                mav.addObject("categoryType", categoryTypeInt);
                mav.addObject("learningYear", learningYear);
                mav.addObject("learningMonth", learningMonth);
                mav.addObject("selectedMonth", learningMonth);
                // カテゴリタイプ名を設定
                String categoryTypeName = switch (categoryTypeInt) {
                    case 0 -> "フロントエンド";
                    case 1 -> "バックエンド";
                    case 2 -> "インフラ";
                    default -> "カテゴリがありません";
                };
                mav.addObject("categoryTypeName", categoryTypeName);
                return mav;
            } else { 
                // 学習データが存在しない場合、新たに学習データを追加
                LearningData newLearningData = new LearningData();

                newLearningData.setUserId(id);
                newLearningData.setCategoryId(existingCategory.getCategoryId());
                newLearningData.setLearningYear(Integer.valueOf(learningYear));
                newLearningData.setLearningMonth(Integer.valueOf(learningMonth));
                newLearningData.setLearningTime(Integer.valueOf(learningTime));

                learningDataMapper.insertLearningData(newLearningData); // 学習データ追加

                // CategoryNewにリダイレクトして必要な情報とフラグを渡す
                return new ModelAndView("redirect:/users/new/category/" + id + 
                    "?categoryType=" + categoryType + 
                    "&learningYear=" + learningYear + 
                    "&selectedMonth=" + learningMonth + 
                    "&createModal=true" + 
                    "&createdCategoryName=" + categoryName + 
                    "&learningTime=" + learningTime
                );
            }
        }

        // カテゴリが存在しない場合、新規にカテゴリと学習データを追加
        Category category = new Category();
        LearningData learningData = new LearningData();

        category.setCategoryType(Integer.valueOf(categoryType)); 
        category.setCategoryName(categoryName); 
        category.setUserId(id); 

        categoryMapper.insertCategory(category); // カテゴリを追加

        // 自動で設定されたカテゴリidを取得するために再度カテゴリを取得
        Category insertedCategory = categoryMapper.findCategoryById(category.getCategoryId());

        learningData.setUserId(id);
        learningData.setCategoryId(insertedCategory.getCategoryId()); 
        learningData.setLearningYear(Integer.valueOf(learningYear));  
        learningData.setLearningMonth(Integer.valueOf(learningMonth));
        learningData.setLearningTime(Integer.valueOf(learningTime)); 

        learningDataMapper.insertLearningData(learningData); // 学習データを追加

        // CategoryNewにリダイレクトして必要な情報とフラグを渡す
        return new ModelAndView("redirect:/users/new/category/" + id + 
            "?categoryType=" + categoryType + 
            "&learningYear=" + learningYear + 
            "&selectedMonth=" + learningMonth + 
            "&createModal=true" + 
            "&createdCategoryName=" + categoryName + 
            "&learningTime=" + learningTime
        );
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

        // カテゴリidとユーザidでカテゴリを取得
        Category category = categoryMapper.findByCategoryIdAndUserId(categoryId, id);
        String deleteCategory = category.getCategoryName();

        // 学習データをカテゴリidとユーザーidで削除
        learningDataMapper.deleteByCategoryIdAndUserId(categoryId, id);

        // カテゴリをカテゴリidとユーザーidで削除
        categoryMapper.deleteByCategoryIdAndUserId(categoryId, id);

        // CategoryEditページにリダイレクト(削除モーダル表示フラグと削除したカテゴリ名を付加)
        return new ModelAndView("redirect:/users/edit/category/" + id +
                                "?selectedMonth=" + selectedMonth +
                                "&deleteCategory=" + deleteCategory +
                                "&deleteModal=true");
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
        Category category = categoryMapper.findByCategoryIdAndUserId(categoryId, id);
        int learningYear = calendar.get(Calendar.YEAR);

        // カテゴリidとユーザidで検索してカテゴリ名をgetする
        String updateCategory = category.getCategoryName();

        // 既存の学習データを取得（ユーザーid、カテゴリid、年、月で検索）
        LearningData existingLearningData = learningDataMapper.findByUserIdAndCategoryIdAndYearAndMonth(id, categoryId, learningYear, selectedMonth);

        // 既存の学習データが存在する場合、学習時間を更新
        existingLearningData.setLearningTime(learningTime);
        learningDataMapper.updateLearningData(existingLearningData); // 学習データを更新
        

        // CategoryEditページにリダイレクト(更新用フラグと更新したカテゴリ名を付加)
        return new ModelAndView("redirect:/users/edit/category/" + id + "?selectedMonth=" + selectedMonth + "&updateCategory=" + updateCategory + "&updateModal=true");
    }

    /**
     * モーダル内の「カテゴリ一覧」ボタンのリダイレクト処理
     * @param userId
     * @param selectedMonth
     * @param mav 
     * @return 
     */
    @GetMapping("/users/edit/category/redirect")
    public ModelAndView redirectToCategoryEdit(@RequestParam("userId") Integer userId,
                                                @RequestParam("selectedMonth") Integer selectedMonth,
                                                ModelAndView mav) {
        
        return new ModelAndView("redirect:/users/edit/category/" + userId + "?selectedMonth=" + selectedMonth);
    }
}