# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table substance (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_substance primary key (id))
;

create sequence substance_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists substance;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists substance_seq;

