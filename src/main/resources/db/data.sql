insert into MEMBER
(CREATE_DATE, UPDATE_DATE, AGE, AUTHORITY, EMAIL, GENDER, LEVEL, NAME, PASSWORD, TEL)
VALUES (now(),
        now(),
        32,
        'ROLE_ADMIN',
        'gogoy2643@naver.com',
        'male',
        'high',
        'syh',
        '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e',
        '010-6330-2643');

INSERT INTO BOARD_TYPE
    (CREATE_DATE, UPDATE_DATE, TYPE_NAME)
VALUES (now(), now(), 'notice'),
       (now(), now(), 'free'),
       (now(), now(), 'admin');

insert into POST
    (CREATE_DATE, UPDATE_DATE, CONTENTS, MEMBERS_ID, CNT, BOARD_TYPE)
VALUES (now(), now(), 'contents1', 1, 0, 'notice'),
       (now(), now(), 'contents2', 1, 0, 'notice'),
       (now(), now(), 'contents3', 1, 0, 'notice'),
       (now(), now(), 'contents4', 1, 0, 'notice'),
       (now(), now(), 'contents5', 1, 0, 'notice'),
       (now(), now(), 'contents6', 1, 0, 'notice'),
       (now(), now(), 'contents7', 1, 0, 'notice'),
       (now(), now(), 'contents8', 1, 0, 'notice'),
       (now(), now(), 'contents9', 1, 0, 'notice'),
       (now(), now(), 'contents10', 1, 0, 'notice'),
       (now(), now(), 'contents11', 1, 0, 'notice'),
       (now(), now(), 'contents12', 1, 0, 'notice'),
       (now(), now(), 'contents13', 1, 0, 'free'),
       (now(), now(), 'contents14', 1, 0, 'free'),
       (now(), now(), 'contents15', 1, 0, 'free'),
       (now(), now(), 'contents16', 1, 0, 'admin');
