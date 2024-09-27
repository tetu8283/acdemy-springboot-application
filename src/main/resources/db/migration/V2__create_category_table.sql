CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,            
    user_id INT NOT NULL,
    category_type INT NOT NULL,         
    category_name VARCHAR(20) NOT NULL 
);