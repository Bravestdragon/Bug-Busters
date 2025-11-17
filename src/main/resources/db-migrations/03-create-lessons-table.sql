CREATE TABLE IF NOT EXISTS lessons (
  id INT PRIMARY KEY AUTO_INCREMENT,
  course_id INT NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  content LONGTEXT,
  video_url VARCHAR(500),
  sequence_number INT NOT NULL,
  duration_minutes INT DEFAULT 0,
  status VARCHAR(20) DEFAULT 'DRAFT',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  INDEX idx_course (course_id),
  INDEX idx_status (status),
  INDEX idx_sequence (course_id, sequence_number)
);
