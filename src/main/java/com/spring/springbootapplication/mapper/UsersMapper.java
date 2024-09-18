package com.spring.springbootapplication.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.springbootapplication.entity.Users;

/**
 * ユーザーに関連するデータベース操作を行うマッパーインターフェース
 */
public interface UsersMapper {

    /**
     * すべてのユーザーを取得
     *
     * @return List<Users> ユーザーのリスト
     */
    List<Users> findAll();

    /**
     * 指定されたIDのユーザーを取得
     *
     * @param userId ユーザーのID
     * @return Users 取得されたユーザー
     */
    Users findById(Integer userId); 

    /**
     * メールアドレスとパスワードで一致するユーザーを取得
     * 
     * @param mailAddress メールアドレス
     * @param password パスワード
     * @return Users 一致するユーザー
     */
    Users findByMailAddressAndPassword(@Param("mailAddress") String mailAddress, @Param("password") String password); 

    /**
     * 新しいユーザーを挿入
     *
     * @param user 挿入するユーザー
     */
    void insert(Users user);

    /**
     * ユーザー情報を更新
     *
     * @param user 更新するユーザー
     */
    void update(Users user);

    /**
     * 指定されたIDのユーザーを削除
     *
     * @param userId 削除するユーザーのID
     */
    void delete(Integer userId); 

    /**
     * メールアドレスでユーザーを検索
     *
     * @param mailAddress メールアドレス
     * @return Users 一致するユーザー
     */
    Users findByMailAddress(String mailAddress); 
}
