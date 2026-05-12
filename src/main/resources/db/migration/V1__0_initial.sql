-- =====================================================
-- 1. Create the Category Table
-- =====================================================
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  `name` VARCHAR(255) NOT NULL UNIQUE,
  `description` TEXT,
  `image_link` VARCHAR(255),

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Insert Predefined Categories
-- -----------------------------------------------------
INSERT INTO `category` (`id`, `name`, `description`, `image_link`) VALUES
  (1, 'Crime', 'Incidents related to crimes and illegal activities.', 'http://localhost:8080/category-images/crime.jpg'),
  (2, 'Workplace', 'Issues related to workplace misconduct and violations.', 'http://localhost:8080/category-images/workplace.jpg'),
  (3, 'Public Infrastructure', 'Incidents concerning public utilities and infrastructure failures.', 'http://localhost:8080/category-images/public_infrastructure.jpg'),
  (4, 'Environment', 'Environmental issues such as pollution, wildlife poaching, and deforestation.', 'http://localhost:8080/category-images/environment.jpg'),
  (5, 'Health and Safety', 'Matters related to health, safety, and public well-being.', 'http://localhost:8080/category-images/health_safety.jpg'),
  (6, 'Consumer Rights', 'Complaints regarding consumer issues, including overcharging and faulty products.', 'http://localhost:8080/category-images/consumer_rights.jpg'),
  (7, 'Traffic and Transportation', 'Traffic incidents and transportation-related problems.', 'http://localhost:8080/category-images/traffic.jpg')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`),
  `description` = VALUES(`description`),
  `image_link` = VALUES(`image_link`);

-- =====================================================
-- 2. Create the Subcategory Table
-- =====================================================
CREATE TABLE IF NOT EXISTS `subcategory` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  `category_id` BIGINT NOT NULL,
  `name` VARCHAR(255) NOT NULL,

  PRIMARY KEY (`id`),

  CONSTRAINT `FK_subcategory_category_id` FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Insert Predefined Subcategories
-- -----------------------------------------------------
INSERT INTO `subcategory` (`id`, `category_id`, `name`) VALUES
  -- Crime
  (1, 1, 'Theft'),
  (2, 1, 'Cybercrime'),
  (3, 1, 'Physical Assault'),
  (4, 1, 'Fraud'),

  -- Workplace
  (5, 2, 'Harassment'),
  (6, 2, 'Unsafe Conditions'),
  (7, 2, 'Unpaid Wages'),

  -- Public Infrastructure
  (8, 3, 'Broken Streetlights'),
  (9, 3, 'Road Damage'),
  (10, 3, 'Power Outages'),
  (11, 3, 'Building Fires'),

  -- Environment
  (12, 4, 'Pollution'),
  (13, 4, 'Wildlife Poaching'),
  (14, 4, 'Deforestation'),

  -- Health and Safety
  (15, 5, 'Medical Malpractice'),
  (16, 5, 'Unsafe School Conditions'),

  -- Consumer Rights
  (17, 6, 'Overcharging'),
  (18, 6, 'Faulty Products'),
  (19, 6, 'Fraudulent Services'),

  -- Traffic and Transportation
  (20, 7, 'Road Accidents'),
  (21, 7, 'Traffic Violations'),
  (22, 7, 'Broken Traffic Signals')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`);

-- =====================================================
-- 3. Create the Department Table
-- =====================================================
CREATE TABLE IF NOT EXISTS `department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  `department_code` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Insert Sample Departments
-- -----------------------------------------------------
INSERT INTO `department` (`id`, `department_code`, `name`) VALUES
  (1, 'POLICE', 'Police Department'),
  (2, 'FIRE', 'Fire Department'),
  (3, 'ENV', 'Environmental Department'),
  (4, 'TRANSPORT', 'Transport Department'),
  (5, 'HEALTH', 'Health Department'),
  (6, 'CONSUMER', 'Consumer Affairs Department'),
  (7, 'LABOUR', 'Labour Department'),
  (8, 'TRAFFIC', 'Traffic Police'),
  (9, 'WILD', 'Wildlife Conservation Department'),
  (10, 'MUNICIPAL', 'Municipal Corporation'),
  (11, 'EDUCATION', 'Education Department')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`);

-- =====================================================
-- 4. Create the App_User Table
-- =====================================================
CREATE TABLE `app_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  -- ENUM for user role
  `role` ENUM(
    'PUBLIC',
    'DEPARTMENT_INCHARGE',
    'DEPARTMENT_ADMIN',
    'ADMIN'
  ) NOT NULL,

  `department_id` BIGINT,
  `full_name` VARCHAR(255) NOT NULL,
  `user_name` VARCHAR(255) UNIQUE,
  `email` VARCHAR(255) UNIQUE NOT NULL,

  -- Password with CHECK constraint ensuring length between 8 and 255 characters
  `password` VARCHAR(255) NOT NULL
  CHECK (CHAR_LENGTH(`password`) >= 8 AND CHAR_LENGTH(`password`) <= 255),

  `phone` VARCHAR(255),

  -- ENUM for user status
  `status` ENUM(
    'PENDING',
    'ACTIVE',
    'REJECTED',
    'SUSPENDED'
  ) NOT NULL,

  `approved_by` BIGINT,
  `approved_date` TIMESTAMP,

  -- Email verification fields
  `verified` BOOLEAN NOT NULL DEFAULT FALSE,
  `verification_token` VARCHAR(255) UNIQUE,

  -- Reset token fields
  `reset_token` VARCHAR(255) UNIQUE,
  `reset_token_expiry` TIMESTAMP,

  PRIMARY KEY (`id`),

  CONSTRAINT `FK_app_user_department_id` FOREIGN KEY (`department_id`)
    REFERENCES `department`(`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_app_user_approved_by` FOREIGN KEY (`approved_by`)
    REFERENCES `app_user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Insert Department Admins
-- -----------------------------------------------------
-- Insert Department Admins for ALL Departments
INSERT INTO `spoiler_alert_db`.`app_user`
(`role`, `department_id`, `full_name`, `user_name`, `email`, `password`, `phone`, `status`, `verified`)
VALUES

-- Police (already exists - skip if duplicate)
('DEPARTMENT_ADMIN', '1', 'Department Admin Police', 'admin_police', 'admin_police@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000001', 'ACTIVE', '1'),

-- Fire
('DEPARTMENT_ADMIN', '2', 'Department Admin Fire', 'admin_fire', 'admin_fire@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000002', 'ACTIVE', '1'),

-- Environment
('DEPARTMENT_ADMIN', '3', 'Department Admin Environment', 'admin_env', 'admin_env@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000003', 'ACTIVE', '1'),

-- Transport
('DEPARTMENT_ADMIN', '4', 'Department Admin Transport', 'admin_transport', 'admin_transport@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000004', 'ACTIVE', '1'),

-- Health
('DEPARTMENT_ADMIN', '5', 'Department Admin Health', 'admin_health', 'admin_health@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000005', 'ACTIVE', '1'),

-- Consumer
('DEPARTMENT_ADMIN', '6', 'Department Admin Consumer', 'admin_consumer', 'admin_consumer@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000006', 'ACTIVE', '1'),

-- Labour
('DEPARTMENT_ADMIN', '7', 'Department Admin Labour', 'admin_labour', 'admin_labour@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000007', 'ACTIVE', '1'),

-- Traffic
('DEPARTMENT_ADMIN', '8', 'Department Admin Traffic', 'admin_traffic', 'admin_traffic@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000008', 'ACTIVE', '1'),

-- Wildlife
('DEPARTMENT_ADMIN', '9', 'Department Admin Wildlife', 'admin_wildlife', 'admin_wildlife@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000009', 'ACTIVE', '1'),

-- Municipal (already exists - optional)
('DEPARTMENT_ADMIN', '10', 'Department Admin Municipal', 'admin_municipal', 'admin_municipal@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000010', 'ACTIVE', '1'),

-- Education
('DEPARTMENT_ADMIN', '11', 'Department Admin Education', 'admin_education', 'admin_education@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '9000000011', 'ACTIVE', '1');



-- -----------------------------------------------------
-- Insert Admin
-- -----------------------------------------------------
INSERT INTO `spoiler_alert_db`.`app_user` (`role`, `full_name`, `user_name`, `email`, `password`, `phone`, `status`, `verified`) VALUES
 ('ADMIN', 'Amal A', 'admin@gmail.com', 'admin@gmail.com', '$2a$10$jjAMrErVy3hCgfRwVKHP/OhlZS5S6XSIkw2yIcCGpQirm6VkPu2Nu', '09847339957', 'ACTIVE', '1');

-- =====================================================
-- 5. Create the Incident Table
-- =====================================================
CREATE TABLE IF NOT EXISTS `incident_report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  `title` VARCHAR(255) NOT NULL,
  `description` TEXT NOT NULL,
  `media_urls` TEXT DEFAULT NULL,
  `latitude` VARCHAR(255) DEFAULT NULL,
  `longitude` VARCHAR(255) DEFAULT NULL,
  `location` VARCHAR(255) DEFAULT NULL,
  `subcategory_id` BIGINT NOT NULL,

  `severity` ENUM(
    'INFO',
    'WARNING',
    'URGENT',
    'EMERGENCY'
  ) NOT NULL,

  `status` ENUM(
    'REPORTED',
    'UNDER_REVIEW',
    'ASSIGNED',
    'IN_PROGRESS',
    'ON_HOLD',
    'ESCALATED',
    'RESOLVED',
    'VERIFIED',
    'CLOSED',
    'REJECTED',
    'DUPLICATE',
    'CANCELLED'
  ) NOT NULL DEFAULT 'REPORTED',

  `assigned_department_id` BIGINT NOT NULL,
  `assigned_user_id` BIGINT DEFAULT NULL,
  `reported_by` BIGINT DEFAULT NULL,

  PRIMARY KEY (`id`),

  -- Foreign keys
  CONSTRAINT `FK_incident_report_subcategory_id` FOREIGN KEY (`subcategory_id`)
    REFERENCES `subcategory` (`id`),
  CONSTRAINT `FK_incident_report_assigned_department_id` FOREIGN KEY (`assigned_department_id`)
    REFERENCES `department` (`id`),
  CONSTRAINT `FK_incident_report_assigned_user_id` FOREIGN KEY (`assigned_user_id`)
    REFERENCES `app_user` (`id`),
  CONSTRAINT `FK_incident_report_reported_by` FOREIGN KEY (`reported_by`)
    REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================================================
-- 6. Create the Incident Status Transition Table
-- =====================================================
CREATE TABLE IF NOT EXISTS `incident_status_transition` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  `from_status` ENUM(
    'REPORTED',
    'UNDER_REVIEW',
    'ASSIGNED',
    'IN_PROGRESS',
    'ON_HOLD',
    'ESCALATED',
    'RESOLVED',
    'VERIFIED',
    'CLOSED',
    'REJECTED',
    'DUPLICATE',
    'CANCELLED'
  ) NOT NULL,

  `to_status` ENUM(
    'REPORTED',
    'UNDER_REVIEW',
    'ASSIGNED',
    'IN_PROGRESS',
    'ON_HOLD',
    'ESCALATED',
    'RESOLVED',
    'VERIFIED',
    'CLOSED',
    'REJECTED',
    'DUPLICATE',
    'CANCELLED'
  ) NOT NULL,

  `is_dep_admin_updatable` BOOLEAN NOT NULL DEFAULT FALSE,
  `is_dep_incharge_updatable` BOOLEAN NOT NULL DEFAULT FALSE,

  UNIQUE (`from_status`, `to_status`),
  PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Insert Incident Status Transition
-- -----------------------------------------------------
INSERT INTO `incident_status_transition` (`id`, `from_status`, `to_status`, `is_dep_admin_updatable`, `is_dep_incharge_updatable`) VALUES
-- Admin only
(1, 'REPORTED', 'UNDER_REVIEW', true, false),
(2, 'UNDER_REVIEW', 'ASSIGNED', true, false),
(3, 'UNDER_REVIEW', 'REJECTED', true, false),
(10, 'RESOLVED', 'VERIFIED', true, false),
(11, 'VERIFIED', 'CLOSED', true, false),
(12, 'REPORTED', 'CANCELLED', true, false),
(13, 'UNDER_REVIEW', 'CANCELLED', true, false),
(14, 'REPORTED', 'DUPLICATE', true, false),
(15, 'UNDER_REVIEW', 'DUPLICATE', true, false),
(16, 'ESCALATED', 'ASSIGNED', true, false),
(17, 'ESCALATED', 'REJECTED', true, false),
(18, 'UNDER_REVIEW', 'ESCALATED', true, false),
(19, 'ASSIGNED', 'ESCALATED', true, false),
(20, 'IN_PROGRESS', 'ESCALATED', true, false),
(21, 'ON_HOLD', 'ESCALATED', true, false),
(22, 'RESOLVED', 'ESCALATED', true, false),

-- In-Charge only
(4, 'ASSIGNED', 'IN_PROGRESS', false, true),
(5, 'ASSIGNED', 'ON_HOLD', false, true),
(6, 'IN_PROGRESS', 'RESOLVED', false, true),
(7, 'IN_PROGRESS', 'ON_HOLD', false, true),
(8, 'ON_HOLD', 'IN_PROGRESS', false, true),
(9, 'ON_HOLD', 'RESOLVED', false, true)

ON DUPLICATE KEY UPDATE
  `from_status` = VALUES(`from_status`),
  `to_status` = VALUES(`to_status`),
  `is_dep_admin_updatable` = VALUES(`is_dep_admin_updatable`),
  `is_dep_incharge_updatable` = VALUES(`is_dep_incharge_updatable`);

-- =====================================================
-- 7. Create Incident Escalation Table
-- =====================================================
CREATE TABLE IF NOT EXISTS `incident_escalation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,            -- Incident escalation ID
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  `incident_report_id` BIGINT NOT NULL,           -- Reference to the incident report
  `from_user_id` BIGINT,                          -- Reference to the previous assignee (may be null if no assignee)
  `escalation_reason` VARCHAR(255) NOT NULL,      -- Reason for the escalation
  `initiated_by_id` BIGINT,                       -- Optional reference to who initiated the escalation

  PRIMARY KEY (`id`),

  -- Foreign keys
  CONSTRAINT `FK_incident_escalation_incident_report_id` FOREIGN KEY (`incident_report_id`)
    REFERENCES `incident_report` (`id`) ON DELETE CASCADE,  -- Foreign key to the IncidentReport table
  CONSTRAINT `FK_incident_escalation_from_user_id` FOREIGN KEY (`from_user_id`)
    REFERENCES `app_user` (`id`) ON DELETE CASCADE,         -- Foreign key to the User table (previous assignee, nullable)
  CONSTRAINT `FK_incident_escalation_initiated_by_id` FOREIGN KEY (`initiated_by_id`)
    REFERENCES `app_user` (`id`) ON DELETE SET NULL         -- Foreign key to the User table (initiator of the escalation)
);

-- =====================================================
-- 8. Create the Subcategory_Department Mapping Table
-- =====================================================
CREATE TABLE IF NOT EXISTS `subcategory_department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,  -- Auto incremented ID for the mapping
  `created_date` TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date` TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

  `subcategory_id` BIGINT NOT NULL,
  `department_id` BIGINT NOT NULL,

  PRIMARY KEY (`id`),

  CONSTRAINT `FK_subcategory_department_subcategory_id` FOREIGN KEY (`subcategory_id`)
    REFERENCES `subcategory`(`id`),
  CONSTRAINT `FK_subcategory_department_department_id` FOREIGN KEY (`department_id`)
    REFERENCES `department`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Insert Sample Mappings into Subcategory_Department
-- -----------------------------------------------------
INSERT INTO `subcategory_department` (`id`, `subcategory_id`, `department_id`) VALUES
  -- Crime
  (1, 1, 1),  -- Theft              -> Police Department
  (2, 2, 1),  -- Cybercrime         -> Police Department
  (3, 3, 1),  -- Physical Assault   -> Police Department
  (4, 4, 6),  -- Fraud              -> Consumer Affairs Department

  -- Workplace
  (5, 5, 7),  -- Harassment         -> Labour Department
  (6, 6, 7),  -- Unsafe Conditions  -> Labour Department
  (7, 7, 7),  -- Unpaid Wages       -> Labour Department

  -- Public Infrastructure
  (8, 8, 10), -- Broken Streetlights -> Municipal Corporation
  (9, 9, 4),  -- Road Damage        -> Transport Department
  (10, 10, 10),-- Power Outages      -> Municipal Corporation
  (11, 11, 2), -- Building Fires     -> Fire Department

  -- Environment
  (12, 12, 3), -- Pollution          -> Environmental Department
  (13, 13, 9), -- Wildlife Poaching  -> Wildlife Conservation Department
  (14, 14, 3), -- Deforestation      -> Environmental Department

  -- Health and Safety
  (15, 15, 5), -- Medical Malpractice        -> Health Department
  (16, 16, 11),-- Unsafe School Conditions   -> Education Department

  -- Consumer Rights
  (17, 17, 6), -- Overcharging       -> Consumer Affairs Department
  (18, 18, 6), -- Faulty Products    -> Consumer Affairs Department
  (19, 19, 6), -- Fraudulent Services-> Consumer Affairs Department

  -- Traffic and Transportation
  (20, 20, 8), -- Road Accidents     -> Traffic Police
  (21, 21, 8), -- Traffic Violations -> Traffic Police
  (22, 22, 4)  -- Broken Traffic Signals -> Transport Department
ON DUPLICATE KEY UPDATE
  `subcategory_id` = VALUES(`subcategory_id`),
  `department_id` = VALUES(`department_id`);
