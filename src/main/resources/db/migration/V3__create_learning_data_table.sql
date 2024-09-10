CREATE TABLE learning_data (
    id SERIAL,                       
    user_id INT NOT NULL,            
    category_id INT NOT NULL,        
    learning_year CHAR(5) NOT NULL,  
    learning_month CHAR(5) NOT NULL, 
    learning_time CHAR(5) NOT NULL,  
    PRIMARY KEY (id, user_id, category_id), 
    FOREIGN KEY (category_id) REFERENCES category(id)
);