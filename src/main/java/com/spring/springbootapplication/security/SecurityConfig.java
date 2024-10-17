package com.spring.springbootapplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// import com.spring.springbootapplication.service.UsersService;

@Configuration 
@EnableWebSecurity 
public class SecurityConfig {

    @Autowired
    // private UsersService usersService; // 認証処理を担当するサービス

    /**
     * パスワードを暗号化するためのエンコーダーをBeanとして登録
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 認証マネージャーの設定
     * @param authenticationConfiguration 認証設定
     * @return 認証マネージャー
     * @throws Exception 認証に失敗した場合の例外
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * セキュリティフィルタチェーンの設定
     * 各種URLの認可設定やフォームログイン、ログアウトの設定を行う
     * @param http HttpSecurity オブジェクト
     * @return セキュリティフィルタチェーン
     * @throws Exception セキュリティ設定に失敗した場合の例外
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // サインインやログインページは認証なしでアクセス可能
                .requestMatchers("/users/signin", "/users/login", "/users/list").permitAll()
                // 静的リソース（CSS、JS、画像）は認証なしでアクセス可能
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // それ以外のURLは認証が必要
                .anyRequest().authenticated()
            )
            // フォームログインの設定
            .formLogin(login -> login
                .loginPage("/users/login") // ログインページのURLを指定
                .loginProcessingUrl("/users/login") // ログイン処理を行うURL
                .defaultSuccessUrl("/users/top", true) // ログイン成功時にリダイレクトするURL
                .failureUrl("/users/login?error=true") // ログイン失敗時にエラーを表示するURL
                .permitAll()
            )
            // ログアウトの設定
            .logout(logout -> logout
                .logoutUrl("/users/logout") // ログアウト処理のURL
                .logoutSuccessUrl("/users/login") // ログアウト成功時にリダイレクトするURL
                .permitAll()
            );

        return http.build();
    }
}
