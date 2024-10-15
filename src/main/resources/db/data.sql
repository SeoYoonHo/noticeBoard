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

insert into NODE
    (CREATE_DATE, UPDATE_DATE, ID, NODE_TYPE, NAME)
VALUES (now(), now(), 1, 'SERVER', 'server1'),
       (now(), now(), 2, 'SERVER', 'server2'),
       (now(), now(), 3, 'SERVER', 'server3'),
       (now(), now(), 4, 'SERVER', 'server4'),
       (now(), now(), 5, 'SERVER', 'server5'),
       (now(), now(), 6, 'LOAD_BALANCER', 'lb1');

insert into LOAD_BALANCER
(ID)
VALUES (6);

insert into SERVER
    (IP_ADDRESS, ID, LB_ID)
VALUES ('100.100.100.101', 1, 6),
       ('100.100.100.102', 2, 6),
       ('100.100.100.103', 3, null),
       ('100.100.100.104', 4, null),
       ('100.100.100.105', 5, null);

