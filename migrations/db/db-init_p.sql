CREATE USER liquibase_migration WITH PASSWORD 'password';
GRANT CONNECT ON DATABASE postgres TO liquibase_migration;
GRANT CREATE, USAGE ON SCHEMA public TO liquibase_migration;
GRANT ALL PRIVILEGES ON DATABASE postgres TO liquibase_migration;