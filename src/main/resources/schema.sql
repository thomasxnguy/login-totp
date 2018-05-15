CREATE  TABLE users (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(200) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));

CREATE TABLE user_roles (
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (username),
  UNIQUE KEY uni_username_role (role,username));


CREATE  TABLE users_totp (
  username VARCHAR(45) NOT NULL ,
  key VARCHAR(200) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 0 ,
  PRIMARY KEY (username));