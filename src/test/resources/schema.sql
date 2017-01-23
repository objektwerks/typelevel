drop table if exists worker;
drop table if exists task;
create table worker (
  id int primary key not null,
  name varchar(64) not null
);
create table task (
  id int primary key not null,
  task varchar(128) not null,
  foreign key ( worker_id ) references worker ( id )
);