// Oracleのコーディング規約を使用

package com.spring.springbootapplication.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.springbootapplication.entity.Users;
import com.spring.springbootapplication.mapper.UsersMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * ユーザー関連の処理をハンドリングする
 * ログイン、サインイン、ユーザー一覧の表示機能
 */
@Controller
public class UsersController {

    // ログを出力するためのロガー (開発中に動作確認のために使用。開発が終了したら削除)
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    // パスワードのエンコードを行うエンコーダー 
    @Autowired
    private PasswordEncoder passwordEncoder;

    // データベースにアクセスするためのユーザーマッパー
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Validator validator; // SpringのValidatorをインジェクション

    // バリデーションの設定
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    // デフォルト画像パス
    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/static/imgs/no-img.jpeg"; 

    /**
     * ログインページを表示
     * @param mav 
     * @param error ログインエラーが発生したかどうかのフラグ
     * @return 
     */
    @GetMapping("/users/login")
    public ModelAndView login(ModelAndView mav, boolean error) {
        mav.setViewName("UsersLogin");
        // errorのTFはconfigファイルで判定、格納をしている
        if (error) {
            mav.addObject("loginError", "メールアドレスまたはパスワードが正しくありません。");
        }
        return mav;
    }

    /**
     * ログイン処理
     * @param user ログイン情報を持つユーザーオブジェクト
     * @param result バリデーション結果
     * @param request HTTPリクエストオブジェクト
     * @param mav 
     * @return 
     */
    @PostMapping("/users/login")
    @Transactional
    public ModelAndView doLogin(
        @Valid @ModelAttribute("user") Users user,
        BindingResult result,
        HttpServletRequest request,
        ModelAndView mav
    ) {
        // メールアドレスとパスワードでユーザーを検索
        Users dbUser = usersMapper.findByMailAddressAndPassword(user.getMailAddress(), user.getPassword());

        // ユーザーが見つからない、またはバリデーションエラーが発生した場合
        if (dbUser == null || result.hasErrors()) {
            mav.setViewName("UsersLogin");
            mav.addObject("user", user);  // 入力したユーザー情報を保持
            return mav;
        }

        // ユーザー名をセッションに保存
        request.getSession().setAttribute("userName", dbUser.getUserName());

        // ログイン成功時にトップページにリダイレクト
        return new ModelAndView("redirect:/users/top");
    }


    /**
     * サインインページを表示
     *
     * @param mav 
     * @return 
     */
    @GetMapping("/users/signin")
    public ModelAndView signin(ModelAndView mav) {
        mav.setViewName("UsersSignin");
        mav.addObject("user", new Users());  // フォーム用の空のユーザーオブジェクトを渡す
        return mav;
    }

    /**
     * サインイン処理
     * @param user サインイン情報を持つユーザーオブジェクト
     * @param result バリデーション結果
     * @param mav 
     * @param request HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @return 
     */
    @PostMapping("/users/signin")
    @Transactional
    public ModelAndView create(
        @Valid @ModelAttribute("user") Users user,
        BindingResult result,
        ModelAndView mav,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // resultがエラーを保持していればサインインページを再表示
        if (result.hasErrors()) {
            mav.setViewName("UsersSignin");
            mav.addObject("user", user);
            return mav;
        }

        // パスワードをハッシュ化して保存
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        // デフォルト画像をバイト配列として読み込み、ユーザーオブジェクトに設定
        try {
            byte[] defaultImage = Files.readAllBytes(Paths.get(DEFAULT_IMAGE_PATH));
            user.setProfileImageData(defaultImage);
        } catch (IOException e) {
            logger.error("デフォルト画像の読み込みに失敗しました", e);
        }

        usersMapper.insert(user);

        // 登録後、自動でログイン処理を行う
        try {
            request.login(user.getMailAddress(), rawPassword);

            // セッションにユーザー名を保存
            request.getSession().setAttribute("userName", user.getUserName());

            // トップページにリダイレクト
            return new ModelAndView("redirect:/users/top");
        } catch (ServletException e) {
            logger.error("ログイン処理に失敗しました: {}", e);
            mav.setViewName("UsersSignin");
            mav.addObject("errorMessage", "ログインに失敗しました");
            return mav;
        }
    }

    /**
     * ログイン後のトップページを表示
     * @param mav 
     * @param request HTTPリクエストオブジェクト
     * @return 
     */

    @GetMapping("/users/top")
    public ModelAndView top(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("UsersTop");
        // ユーザの認証情報を保持
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // メールアドレス取得
        Users dbUser = usersMapper.findByMailAddress(email); // メールアドレスで検索

        String profileImage;
        // ユーザが画像データを保持しているかを判定
        if (dbUser.getProfileImageData() == null) {
            profileImage = encodeImage(DEFAULT_IMAGE_PATH); // 画像データを保持していないと、no-image.jpegを指定
        } else {
            // 画像をエンコードしてhtmlで表示できるようにしている
            profileImage = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(dbUser.getProfileImageData());
        }
        mav.addObject("user", dbUser);
        mav.addObject("profileImage", profileImage);
        mav.addObject("user_name", dbUser.getUserName());
        mav.addObject("self_introduction", dbUser.getSelfIntroduction());

        return mav;
    }

    /**
      * ユーザ編集ページへ遷移
      * @param id urlから取得した値(UserId)をlongで保持
      * @param mav 
      * @return 
      */
    @GetMapping("/users/edit/{id}")
    public ModelAndView editForm(@PathVariable Long id, ModelAndView mav) {
        Users user = usersMapper.findById(id);
        mav.setViewName("UsersEdit");
        mav.addObject("user", user);
        mav.addObject("user_name", user.getUserName());
        return mav;
    }

    /**
     * 
     * @param id
     * @param file
     * @param user
     * @param result
     * @param mav
     * @return
     * @throws IOException
     */

    @PostMapping("/users/update/{id}")
    @Transactional
    public ModelAndView update(@PathVariable Long id,
                                @RequestParam("file") MultipartFile file,
                                @ModelAttribute Users user, // @Validを外して手動でバリデーションする
                                BindingResult result,
                                ModelAndView mav) throws IOException {

        // データベースから現在のユーザー情報を取得
        Users dbUser = usersMapper.findById(id);

        // フォームから送信されるuserにnullのフィールドがある場合、dbUserから値をuserに追加する
        if (user.getUserName() == null) user.setUserName(dbUser.getUserName());
        if (user.getMailAddress() == null) user.setMailAddress(dbUser.getMailAddress()); 
        if (user.getPassword() == null) user.setPassword(dbUser.getPassword());

        // 自己紹介文の手動バリデーション（50文字以上200文字以下かをチェック）
        String selfIntroduction = user.getSelfIntroduction();  // 入力された自己紹介文を保持
        if (selfIntroduction.length() < 50 || selfIntroduction.length() > 200) {
            mav.setViewName("UsersEdit");
            mav.addObject("user", user);
            mav.addObject("user_name", user.getUserName());
            mav.addObject("errorMessage", "自己紹介文は50文字以上200文字以下で入力してください。");
            return mav;
        }

        try {
            // ファイルが空でない場合、新しい画像データを設定
            if (!file.isEmpty()) {
                user.setProfileImageData(file.getBytes());
            } else {
                // 画像ファイルが空の場合、既存の画像データを保持
                user.setProfileImageData(dbUser.getProfileImageData());
            }
        } catch (IOException e) {
            // 画像の読み込みに失敗した場合のエラーハンドリング
            logger.error("画像の読み込みに失敗しました", e);
            mav.addObject("errorMessage", "画像のアップロードに失敗しました");
            mav.addObject("user", user);
            mav.addObject("user_name", user.getUserName());
            mav.setViewName("UsersEdit");
            return mav;
        }

        // データベースのユーザー情報を更新
        usersMapper.update(user);
        return new ModelAndView("redirect:/users/top");
    }

    // 画像をエンコードして文字列として返すメソッド
    private String encodeImage(String path) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(path));
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            logger.error("画像のエンコードに失敗しました: {}", e);
            return "";
        }
    }


    // これはDBの中身を確認するために作成。
    // SecurituConfigでログインしなくてもDBの中身が確認できるようにアクセス許可を与えている

    /**
     * ユーザー一覧を表示する
     * @return ModelAndView ユーザー一覧ページ
     */
    @GetMapping("/users/list")
    public ModelAndView getUsersList() {
        ModelAndView mav = new ModelAndView("UsersList");
        List<Users> userList = usersMapper.findAll(); // ユーザーのリストを取得
        mav.addObject("userList", userList); // ユーザーリストをモデルに追加
        return mav;
    }
}
