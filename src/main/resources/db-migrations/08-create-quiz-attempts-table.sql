CREATE TABLE IF NOT EXISTS quiz_attempts (
  id INT PRIMARY KEY AUTO_INCREMENT,
  quiz_id INT NOT NULL,
  enrollment_id INT NOT NULL,
  user_id INT NOT NULL,
  score INT DEFAULT 0,
  total_points INT DEFAULT 0,
  percentage DOUBLE DEFAULT 0,
  status VARCHAR(20) DEFAULT 'IN_PROGRESS',
  start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  end_time TIMESTAMP NULL,
  time_spent_seconds INT DEFAULT 0,
  
  INDEX idx_quiz (quiz_id),
  INDEX idx_user (user_id),
  INDEX idx_enrollment (enrollment_id),
  INDEX idx_status (status),
  INDEX idx_start_time (start_time),
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
  FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
