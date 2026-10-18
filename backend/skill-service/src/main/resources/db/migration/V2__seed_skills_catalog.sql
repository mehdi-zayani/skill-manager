
-- V2__seed_skills_catalog.sql

-- Update the allowed categories to match the SkillCategory Java enum.
ALTER TABLE skills
DROP CONSTRAINT ck_skills_category;

ALTER TABLE skills
    ADD CONSTRAINT ck_skills_category
        CHECK (
            category IN (
                         'LANGUAGE',
                         'FRAMEWORK',
                         'LIBRARY',
                         'DATABASE',
                         'TOOL',
                         'CLOUD',
                         'DEVOPS',
                         'TESTING',
                         'SECURITY',
                         'PLATFORM',
                         'API',
                         'MONITORING',
                         'ARCHITECTURE',
                         'METHODOLOGY',
                         'OTHER'
                )
            );

-- Seed the initial skill catalog.
-- Explicit UUIDs keep the initial catalog identifiers stable across environments.
-- ON CONFLICT avoids inserting a skill that already exists by normalized name.
INSERT INTO skills (id, name, normalized_name, category)
VALUES
    ('10000000-0000-4000-8000-000000000001', 'Java', 'java', 'LANGUAGE'),
    ('10000000-0000-4000-8000-000000000002', 'JavaScript', 'javascript', 'LANGUAGE'),
    ('10000000-0000-4000-8000-000000000003', 'TypeScript', 'typescript', 'LANGUAGE'),
    ('10000000-0000-4000-8000-000000000004', 'Python', 'python', 'LANGUAGE'),
    ('10000000-0000-4000-8000-000000000005', 'C++', 'c++', 'LANGUAGE'),
    ('10000000-0000-4000-8000-000000000006', 'PHP', 'php', 'LANGUAGE'),
    ('10000000-0000-4000-8000-000000000007', 'SQL', 'sql', 'LANGUAGE'),

    ('10000000-0000-4000-8000-000000000008', 'Spring Boot', 'spring boot', 'FRAMEWORK'),
    ('10000000-0000-4000-8000-000000000009', 'Spring Framework', 'spring framework', 'FRAMEWORK'),
    ('10000000-0000-4000-8000-000000000010', 'Angular', 'angular', 'FRAMEWORK'),
    ('10000000-0000-4000-8000-000000000011', 'React', 'react', 'FRAMEWORK'),
    ('10000000-0000-4000-8000-000000000012', 'Vue.js', 'vue.js', 'FRAMEWORK'),
    ('10000000-0000-4000-8000-000000000013', 'Django', 'django', 'FRAMEWORK'),
    ('10000000-0000-4000-8000-000000000014', 'Flask', 'flask', 'FRAMEWORK'),
    ('10000000-0000-4000-8000-000000000015', 'Express.js', 'express.js', 'FRAMEWORK'),

    ('10000000-0000-4000-8000-000000000016', 'Hibernate', 'hibernate', 'LIBRARY'),
    ('10000000-0000-4000-8000-000000000017', 'Spring Data JPA', 'spring data jpa', 'LIBRARY'),
    ('10000000-0000-4000-8000-000000000018', 'Lombok', 'lombok', 'LIBRARY'),
    ('10000000-0000-4000-8000-000000000019', 'RxJS', 'rxjs', 'LIBRARY'),
    ('10000000-0000-4000-8000-000000000020', 'Jackson', 'jackson', 'LIBRARY'),

    ('10000000-0000-4000-8000-000000000021', 'PostgreSQL', 'postgresql', 'DATABASE'),
    ('10000000-0000-4000-8000-000000000022', 'MySQL', 'mysql', 'DATABASE'),
    ('10000000-0000-4000-8000-000000000023', 'MariaDB', 'mariadb', 'DATABASE'),
    ('10000000-0000-4000-8000-000000000024', 'MongoDB', 'mongodb', 'DATABASE'),
    ('10000000-0000-4000-8000-000000000025', 'Redis', 'redis', 'DATABASE'),
    ('10000000-0000-4000-8000-000000000026', 'Oracle Database', 'oracle database', 'DATABASE'),

    ('10000000-0000-4000-8000-000000000027', 'Git', 'git', 'TOOL'),
    ('10000000-0000-4000-8000-000000000028', 'Maven', 'maven', 'TOOL'),
    ('10000000-0000-4000-8000-000000000029', 'Gradle', 'gradle', 'TOOL'),
    ('10000000-0000-4000-8000-000000000030', 'IntelliJ IDEA', 'intellij idea', 'TOOL'),
    ('10000000-0000-4000-8000-000000000031', 'Visual Studio Code', 'visual studio code', 'TOOL'),
    ('10000000-0000-4000-8000-000000000032', 'Postman', 'postman', 'TOOL'),
    ('10000000-0000-4000-8000-000000000033', 'Bruno', 'bruno', 'TOOL'),

    ('10000000-0000-4000-8000-000000000034', 'AWS', 'aws', 'CLOUD'),
    ('10000000-0000-4000-8000-000000000035', 'Google Cloud', 'google cloud', 'CLOUD'),
    ('10000000-0000-4000-8000-000000000036', 'Microsoft Azure', 'microsoft azure', 'CLOUD'),
    ('10000000-0000-4000-8000-000000000037', 'Firebase', 'firebase', 'CLOUD'),

    ('10000000-0000-4000-8000-000000000038', 'Docker', 'docker', 'DEVOPS'),
    ('10000000-0000-4000-8000-000000000039', 'Kubernetes', 'kubernetes', 'DEVOPS'),
    ('10000000-0000-4000-8000-000000000040', 'Jenkins', 'jenkins', 'DEVOPS'),
    ('10000000-0000-4000-8000-000000000041', 'GitHub Actions', 'github actions', 'DEVOPS'),
    ('10000000-0000-4000-8000-000000000042', 'Terraform', 'terraform', 'DEVOPS'),

    ('10000000-0000-4000-8000-000000000043', 'JUnit', 'junit', 'TESTING'),
    ('10000000-0000-4000-8000-000000000044', 'Mockito', 'mockito', 'TESTING'),
    ('10000000-0000-4000-8000-000000000045', 'Testcontainers', 'testcontainers', 'TESTING'),
    ('10000000-0000-4000-8000-000000000046', 'Cypress', 'cypress', 'TESTING'),

    ('10000000-0000-4000-8000-000000000047', 'Spring Security', 'spring security', 'SECURITY'),
    ('10000000-0000-4000-8000-000000000048', 'OAuth 2.0', 'oauth 2.0', 'SECURITY'),
    ('10000000-0000-4000-8000-000000000049', 'JWT', 'jwt', 'SECURITY'),

    ('10000000-0000-4000-8000-000000000050', 'Linux', 'linux', 'PLATFORM'),
    ('10000000-0000-4000-8000-000000000051', 'Android', 'android', 'PLATFORM'),

    ('10000000-0000-4000-8000-000000000052', 'REST API', 'rest api', 'API'),
    ('10000000-0000-4000-8000-000000000053', 'GraphQL', 'graphql', 'API'),
    ('10000000-0000-4000-8000-000000000054', 'OpenAPI', 'openapi', 'API'),

    ('10000000-0000-4000-8000-000000000055', 'Prometheus', 'prometheus', 'MONITORING'),
    ('10000000-0000-4000-8000-000000000056', 'Grafana', 'grafana', 'MONITORING'),

    ('10000000-0000-4000-8000-000000000057', 'Microservices', 'microservices', 'ARCHITECTURE'),
    ('10000000-0000-4000-8000-000000000058', 'Domain-Driven Design', 'domain-driven design', 'ARCHITECTURE'),

    ('10000000-0000-4000-8000-000000000059', 'Scrum', 'scrum', 'METHODOLOGY'),
    ('10000000-0000-4000-8000-000000000060', 'Kanban', 'kanban', 'METHODOLOGY'),

    ('10000000-0000-4000-8000-000000000061', 'Technical Documentation', 'technical documentation', 'OTHER'),
    ('10000000-0000-4000-8000-000000000062', 'UML', 'uml', 'OTHER')

    ON CONFLICT (normalized_name) DO NOTHING;
