createTable:

    CREATE OR REPLACE PROCEDURE createTable(t_name varchar)
    LANGUAGE plpgsql AS
    $func$
    BEGIN
    EXECUTE format('
        CREATE TABLE IF NOT EXISTS %I (
        id serial PRIMARY KEY
        )', t_name);
    END
    $func$;

addColumn:

    CREATE OR REPLACE PROCEDURE addColumn(t_name varchar, c_name varchar, c_type varchar)
        LANGUAGE plpgsql AS
    $func$
    BEGIN
    EXECUTE format('
        ALTER TABLE %I
            ADD COLUMN %I %I', t_name, c_name, c_type);
    END
    $func$;