insert into MEMBER
(CREATE_DATE, UPDATE_DATE, AGE, AUTHORITY, EMAIL, GENDER, LEVEL, NAME, PASSWORD, TEL)
VALUES
    (now(),
     now(),
     32,
     'ROLE_ADMIN',
     'gogoy2643@naver.com',
     'male',
     'high',
     'syh',
     '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e',
     '010-6330-2643'
    );