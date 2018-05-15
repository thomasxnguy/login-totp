INSERT INTO users(username,password,enabled)
VALUES ('admin','$2a$10$nliECRWpVralMdY13EKV7u.xAkZ2WkdNJdnAAIxZiXLs2o1iVc0xm', true);
INSERT INTO users(username,password,enabled)
VALUES ('login','$2a$10$nliECRWpVralMdY13EKV7u.xAkZ2WkdNJdnAAIxZiXLs2o1iVc0xm', true);

INSERT INTO user_roles (username, role)
VALUES ('admin', 'ADMIN');
INSERT INTO user_roles (username, role)
VALUES ('login', 'USER');