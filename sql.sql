SELECT 8 FROM flyway_schema_history;
DELETE FROM flyway_schema_history WHERE version = '1';
SELECT 8 FROM flyway_schema_history

SELECT * FROM users