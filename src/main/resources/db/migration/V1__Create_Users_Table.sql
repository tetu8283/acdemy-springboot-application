
CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    mail_address VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL CHECK (CHAR_LENGTH(password) >= 8),
    self_introduction VARCHAR(200) CHECK (CHAR_LENGTH(self_introduction) >= 50 AND CHAR_LENGTH(self_introduction) <= 200),
    profile_image_data BYTEA  -- 画像データをBase64エンコードした文字列として格納
);
