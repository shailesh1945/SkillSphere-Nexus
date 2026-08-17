

-- Employee
INSERT INTO employees (
    employee_id,
    email,
    first_name,
    last_name,
    role,
    department
)
VALUES (
    gen_random_uuid(),
    'john.smith@example.com',
    'John',
    'Smith',
    'DEVELOPER',
    'Engineering'
);

-- Skills
INSERT INTO skills (
    skill_id,
    skill_name,
    category
)
VALUES
    (gen_random_uuid(), 'Java', 'TECHNICAL'),
    (gen_random_uuid(), 'Spring Boot', 'TECHNICAL'),
    (gen_random_uuid(), 'Angular', 'TECHNICAL');

-- Employee <-> Skills
INSERT INTO skill_competencies (
    competency_id,
    employee_id,
    skill_id,
    proficiency_level,
    years_of_experience
)
SELECT
    gen_random_uuid(),
    e.employee_id,
    s.skill_id,
    8,
    3
FROM employees e
CROSS JOIN skills s
WHERE e.email = 'john.smith@example.com'
  AND s.skill_name IN ('Java', 'Spring Boot', 'Angular');