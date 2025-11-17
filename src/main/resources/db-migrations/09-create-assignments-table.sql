CREATE TABLE IF NOT EXISTS assignments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    lesson_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description LONGTEXT,
    due_date DATETIME,
    max_score INT DEFAULT 100,
    requirements LONGTEXT,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE SET NULL,
    INDEX idx_course_id (course_id),
    INDEX idx_lesson_id (lesson_id),
    INDEX idx_status (status)
);
