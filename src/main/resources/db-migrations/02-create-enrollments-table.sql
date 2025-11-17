CREATE TABLE IF NOT EXISTS enrollments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  course_id INT NOT NULL,
  student_id INT NOT NULL,
  student_name VARCHAR(100) NOT NULL,
  course_name VARCHAR(255) NOT NULL,
  enrollment_status VARCHAR(20) DEFAULT 'ACTIVE',
  enrolled_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_date TIMESTAMP NULL,
  progress_percentage DECIMAL(5,2) DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  
  UNIQUE KEY unique_enrollment (course_id, student_id),
  INDEX idx_student (student_id),
  INDEX idx_course (course_id),
  INDEX idx_status (enrollment_status)
);
