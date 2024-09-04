package com.spring.springbootapplication.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.springbootapplication.entity.Users;
import com.spring.springbootapplication.mapper.UsersMapper;

/**
 * ユーザーの認証に使用されるサービスクラス
 * Spring SecurityのUserDetailsServiceを実装
 */
@Service 
public class UsersService implements UserDetailsService {

    @Autowired 
    private UsersMapper usersMapper;

    /**
     * 認証に使用
     * メールアドレスを用いてユーザーをデータベースから取得し、認証情報を返す
     * 
     * @param username 認証に使用するメールアドレス
     * @return UserDetails 認証に使用されるユーザーデータ
     * @throws UsernameNotFoundException ユーザーが見つからない場合にスローされる例外
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // メールアドレスでユーザーをデータベースから取得
        Users user = usersMapper.findByMailAddress(username);
        
        // ユーザーが存在しない場合、例外をスローしてエラーメッセージを表示
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }
        
        // 認証に必要なUserDetailsオブジェクトを生成し返す
        return new org.springframework.security.core.userdetails.User(
            user.getMailAddress(), 
            user.getPassword(), 
            Collections.emptyList()
        );
    }
}
