drop table if exists worker;
drop table if exists task;
create table worker (
  id int primary key not null,
  name varchar(64) not null
);
create table task (
  id int primary key not null,
  workerId int not null,
  task varchar(128) not null,
  foreign key(workerId) references worker(id)
);