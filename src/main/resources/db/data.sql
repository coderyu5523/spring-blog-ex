insert into user_tb(username, password, email, created_at) values('ssar', '$2a$10$uMbm/TjTZAO8sUZ8GJW5uOjChA6mQWTxm6TtCybLEasGcGRjW4cfS', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at) values('cos', '$2a$10$uMbm/TjTZAO8sUZ8GJW5uOjChA6mQWTxm6TtCybLEasGcGRjW4cfS', 'cos@nate.com', now());

insert into board_tb(title,content,user_id,created_at) values ('제목1','내용1',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목2','내용2',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목3','내용3',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목4','내용4',2,now());

insert into board_tb(title,content,user_id,created_at) values ('keyword','내용1',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목2','내용2',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목3','내용3',1,now());
insert into board_tb(title,content,user_id,created_at) values ('keyword','내용4',2,now());
insert into board_tb(title,content,user_id,created_at) values ('123','내용1',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목2','내용2',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목3','내용3',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목4','내용4',2,now());
insert into board_tb(title,content,user_id,created_at) values ('제목1','내용1',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목2','내용2',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목3','내용3',1,now());
insert into board_tb(title,content,user_id,created_at) values ('제목4','내용4',2,now());


insert into reply_tb(comment, board_id, user_id, created_at) values('댓글1', 1, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at) values('댓글2', 4, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at) values('댓글3', 4, 1, now());
insert into reply_tb(comment, board_id, user_id, created_at) values('댓글4', 4, 2, now());

