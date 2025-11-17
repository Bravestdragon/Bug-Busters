CREATE TABLE IF NOT EXISTS question_options (
  id INT PRIMARY KEY AUTO_INCREMENT,
  question_id INT NOT NULL,
  option_text TEXT NOT NULL,
  option_label VARCHAR(10),
  sequence_number INT NOT NULL,
  is_correct BOOLEAN DEFAULT FALSE,
  
  INDEX idx_question (question_id),
  INDEX idx_sequence (question_id, sequence_number),
  FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);
