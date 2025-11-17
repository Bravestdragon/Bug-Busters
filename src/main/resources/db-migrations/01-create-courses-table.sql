-- Course Management Module Database Schema

-- Create courses table if it doesn't exist
CREATE TABLE IF NOT EXISTS courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description LONGTEXT,
    category VARCHAR(100),
    instructor_id INT NOT NULL,
    instructor_name VARCHAR(100),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'DRAFT',
    enrollment_count INT DEFAULT 0,
    FOREIGN KEY (instructor_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_category (category),
    INDEX idx_instructor (instructor_id),
    INDEX idx_status (status),
    INDEX idx_created_date (created_date)
);

-- Sample data for testing (optional)
-- INSERT INTO courses (title, description, category, instructor_id, instructor_name, status) VALUES
-- ('Introduction to Java', 'Learn the basics of Java programming', 'Programming', 1, 'instructor1', 'PUBLISHED'),
-- ('Python for Data Science', 'Master Python for data analysis', 'Programming', 1, 'instructor1', 'PUBLISHED'),
-- ('Advanced Mathematics', 'Advanced topics in mathematics', 'Mathematics', 2, 'instructor2', 'DRAFT');
