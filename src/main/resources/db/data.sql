insert into MEMBER
(CREATE_DATE, UPDATE_DATE, AGE, AUTHORITY, EMAIL, GENDER, LEVEL, NAME, PASSWORD, TEL)
VALUES (now(),
        now(),
        32,
        'ROLE_NORMAL',
        'gogoy2643@naver.com',
        'male',
        'high',
        'syh',
        '{bcrypt}$2a$10$LDwzHdFsoeeo0CjXoYdmwelLK4CjdiMtGvPHDYPQ039JEx19L7C8e',
        '010-6330-2643');

insert into POST
    (CREATE_DATE, UPDATE_DATE, CONTENTS, MEMBERS_ID, CNT)
VALUES (now(),
        now(),
        'contents',
        1,
        0);

INSERT INTO BOARD_TYPE
    (CREATE_DATE, UPDATE_DATE, TYPE_NAME)
VALUES
    (now(), now(), '공지사항'),
    (now(), now(), '자유게시판'),
    (now(), now(), '운영게시판');
