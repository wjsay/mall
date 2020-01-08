create table miaoshauser (
  id int auto_increment primary key,
  phoneno varcahr(20),
  nickname varchar(20),
  password varchar(64),
  salt varchar(16),
  head varchar(120),
  registerDate datetime,
  lastLoginDate datetime
);