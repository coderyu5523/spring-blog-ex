insert into user_tb(username, password, email, created_at) values('ssar', '$2a$10$83x9ayD/giNJWIf/KC.Uc.W4cVKqD5KJKugNNqt9hzYdvTFf/B6ny', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at) values('cos', '$2a$10$83x9ayD/giNJWIf/KC.Uc.W4cVKqD5KJKugNNqt9hzYdvTFf/B6ny', 'cos@nate.com', now());

insert into board_tb(title,content,user_id,created_at) values ('제목1','내용1',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목2','내용2',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목3','내용3',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목4','내용4',2,now());


insert into reply_tb(comment, board_id, user_id, created_at) values('댓글1', 1, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at) values('댓글2', 4, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at) values('댓글3', 4, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at) values('댓글4', 4, 2, now());
