CREATE TABLE accidents (
  id serial primary key not null,
  name varchar(2000),
  description varchar(8000),
  address varchar(1000)
);

CREATE TABLE participant_status (
  id serial primary key not null,
  name varchar(100)
);

CREATE TABLE participants (
  id serial primary key not null,
  name varchar(200),
  address varchar(1000),
  passportData varchar(100),
  phone varchar(50),
  CONSTRAINT passport_data_unique UNIQUE (passportData)
);

CREATE TABLE accidents_participants (
  id_accident int,
  id_participant int,
  id_status int,
  primary key (id_accident, id_participant),
  foreign key (id_accident) references accidents(id),
  foreign key (id_participant) references participants(id),
  foreign key (id_status) references participant_status(id)
);