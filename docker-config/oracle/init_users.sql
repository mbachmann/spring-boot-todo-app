
ALTER SESSION SET container=freepdb1;
GRANT ALL PRIVILEGES TO demouser identified by password;

GRANT DB_DEVELOPER_ROLE TO demouser;
GRANT CREATE SESSION TO demouser;
GRANT UNLIMITED TABLESPACE TO demouser;
