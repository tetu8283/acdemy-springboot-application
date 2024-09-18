CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,            
    category_type INT NOT NULL,         
    category_name VARCHAR(20) NOT NULL 
);