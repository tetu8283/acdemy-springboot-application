
CREATE TABLE learning_data (
    learning_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,  
    learning_year INT NOT NULL,
    learning_month INT NOT NULL,
    learning_time INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);
