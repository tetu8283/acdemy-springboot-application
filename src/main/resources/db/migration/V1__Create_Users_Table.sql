
CREATE TABLE Users (
    userId SERIAL PRIMARY KEY,
    userName VARCHAR(50) NOT NULL,
    mailAddress VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL CHECK (CHAR_LENGTH(password) >= 8),
    selfIntroduction VARCHAR(200) CHECK (CHAR_LENGTH(selfIntroduction) >= 50 AND CHAR_LENGTH(selfIntroduction) <= 200),
    profileImageData BYTEA  -- 画像データをBase64エンコードした文字列として格納
);


