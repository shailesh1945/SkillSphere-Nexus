-- 1. Insert Employee (using fields from your active DB schema)
INSERT INTO employees (employee_id, first_name, last_name, department, email) 
VALUES (gen_random_uuid(), 'John', 'Smith', 'Engineering', 'john.smith@example.com');

-- 2. Insert Skills (using skill_name from your Skill entity)
INSERT INTO skills (skill_id, skill_name, category) 
VALUES 
    (gen_random_uuid(), 'Java', 'TECHNICAL'),
    (gen_random_uuid(), 'Spring Boot', 'TECHNICAL'),
    (gen_random_uuid(), 'Angular', 'TECHNICAL');

-- 3. Link Employee to Skills (using id from your EmployeeSkill entity)
INSERT INTO employee_skills (id, employee_id, skill_id, proficiency)
VALUES 
    (
        gen_random_uuid(), 
        (SELECT employee_id FROM employees WHERE email = 'john.smith@example.com' LIMIT 1),
        (SELECT skill_id FROM skills WHERE skill_name = 'Java' LIMIT 1),
        8
    ),
    (
        gen_random_uuid(), 
        (SELECT employee_id FROM employees WHERE email = 'john.smith@example.com' LIMIT 1),
        (SELECT skill_id FROM skills WHERE skill_name = 'Spring Boot' LIMIT 1),
        9
    ),
    (
        gen_random_uuid(), 
        (SELECT employee_id FROM employees WHERE email = 'john.smith@example.com' LIMIT 1),
        (SELECT skill_id FROM skills WHERE skill_name = 'Angular' LIMIT 1),
        7
    );
