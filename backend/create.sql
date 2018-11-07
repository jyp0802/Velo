SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Owner;
DROP TABLE IF EXISTS Borrow;
DROP TABLE IF EXISTS Bike;
DROP TABLE IF EXISTS Velow;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE User(
  id bigint(20) unsigned not null auto_increment,
  user_name varchar(127) not null,
  user_id varchar(127) not null,
  user_pw varchar(127) not null,
  user_email varchar(127) not null,
  user_phone varchar(20) not null,
  user_studentid int(10) not null,
  PRIMARY KEY(id)
) charset=utf8;

CREATE TABLE Bike(
  id bigint(20) unsigned not null auto_increment,
  bike_type int not null,
  bike_size int not null,
  bike_gender int not null,
  bike_pw varchar(10) not null,
  bike_x float(24) not null,
  bike_y float(24) not null,
  bike_photo text not null,
  bike_status int(10) not null,
  PRIMARY KEY (id),
  check(bike_type >= 0),
  check(bike_type <= 3)
)charset=utf8;

CREATE TABLE Owner(
  owner_uid bigint(20) unsigned not null,
  owner_bid bigint(20) unsigned not null,
  PRIMARY KEY(owner_uid, owner_bid),
  FOREIGN KEY(owner_uid) REFERENCES User(id) ON DELETE CASCADE,
  FOREIGN KEY(owner_bid) REFERENCES Bike(id) ON DELETE CASCADE
)charset=utf8;

CREATE TABLE Borrow(
  borrow_uid bigint(20) unsigned not null,
  borrow_bid bigint(20) unsigned not null,
  PRIMARY KEY(borrow_uid, borrow_bid),
  FOREIGN KEY(borrow_uid) REFERENCES User(id) ON DELETE CASCADE,
  FOREIGN KEY(borrow_bid) REFERENCES Bike(id) ON DELETE CASCADE
) charset=utf8;

CREATE TABLE Velow(
  velower_id bigint(20) unsigned not null,
  velowee_id bigint(20) unsigned not null,
  PRIMARY KEY(velower_id, velowee_id),
  FOREIGN KEY(velower_id) REFERENCES User(id) ON DELETE CASCADE,
  FOREIGN KEY(velowee_id) REFERENCES User(id) ON DELETE CASCADE
) charset=utf8;
