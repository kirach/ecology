# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table substance (
  id                        bigint not null,
  name                      varchar(255),
  s_pdk                     float,
  k                         float,
  constraint pk_substance primary key (id))
;

create sequence substance_seq;




# --- !Downs

drop table if exists substance cascade;

drop sequence if exists substance_seq;

