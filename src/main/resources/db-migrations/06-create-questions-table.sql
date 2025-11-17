CREATE TABLE IF NOT EXISTS questions (
  id INT PRIMARY KEY AUTO_INCREMENT,
  quiz_id INT NOT NULL,
  question_text LONGTEXT NOT NULL,
  question_type VARCHAR(50) DEFAULT 'MULTIPLE_CHOICE',
  points INT DEFAULT 1,
  sequence_number INT NOT NULL,
  correct_answer VARCHAR(500),
  explanation TEXT,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  INDEX idx_quiz (quiz_id),
  INDEX idx_sequence (quiz_id, sequence_number),
  FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);
