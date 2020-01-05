create table miaoshaouser (
  id int auto_increment primary key,
  nickname varchar(20),
  password varchar(64),
  salt varchar(16),
  head varchar(120),
  registerDate datetime,
  lastLoginDate datetime
);