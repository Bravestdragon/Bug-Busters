CREATE TABLE IF NOT EXISTS lesson_progress (
  id INT PRIMARY KEY AUTO_INCREMENT,
  enrollment_id INT NOT NULL,
  lesson_id INT NOT NULL,
  course_id INT NOT NULL,
  student_id INT NOT NULL,
  is_completed BOOLEAN DEFAULT FALSE,
  completed_date TIMESTAMP NULL,
  time_spent_minutes INT DEFAULT 0,
  last_accessed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  UNIQUE KEY unique_progress (enrollment_id, lesson_id),
  INDEX idx_student (student_id),
  INDEX idx_lesson (lesson_id),
  INDEX idx_completed (is_completed)
);
