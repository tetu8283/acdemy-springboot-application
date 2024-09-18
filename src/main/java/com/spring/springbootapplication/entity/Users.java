package com.spring.springbootapplication.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Users {
    private Integer userId;  

    // その他のフィールドも合わせてキャメルケースに修正
    @NotBlank(message = "氏名は必ず入力してください")
    @Size(max = 50, message = "氏名は50文字以内で入力してください")
    private String userName; 

    @NotBlank(message = "メールアドレスは必ず入力してください")
    @Email(message = "メールアドレスが正しい形式ではありません")
    @Size(max = 50, message = "メールアドレスは50文字以内で入力してください")
    private String mailAddress; 

    @NotBlank(message = "パスワードは必ず入力してください")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    private String password;

    @Size(min = 50, max = 200, message = "プロフィール文は50文字以上200文字以下にしてください")
    private String selfIntroduction = "This is default saltIntroduction message. Please enter over 50 charecters.";

    // プロフィール画像をバイト配列として保持
    private byte[] profileImageData; 

    // ゲッターとセッター
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public byte[] getProfileImageData() {
        return profileImageData;
    }

    public void setProfileImageData(byte[] profileImageData) {
        this.profileImageData = profileImageData;
    }
}
