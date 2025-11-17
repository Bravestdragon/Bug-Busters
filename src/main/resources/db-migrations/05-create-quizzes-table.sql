CREATE TABLE IF NOT EXISTS quizzes (
  id INT PRIMARY KEY AUTO_INCREMENT,
  course_id INT NOT NULL,
  lesson_id INT,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  passing_score INT DEFAULT 70,
  time_limit INT,
  total_questions INT DEFAULT 0,
  status VARCHAR(20) DEFAULT 'DRAFT',
  created_by INT,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  INDEX idx_course (course_id),
  INDEX idx_lesson (lesson_id),
  INDEX idx_status (status),
  FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
  FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
