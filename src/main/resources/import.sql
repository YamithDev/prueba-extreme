INSERT INTO usuarios (username, password, enabled, nombre_completo, identificacion, celular, email) VALUES ('yamid','$2a$10$BQTIic/3WycpYZnjZtCCcOqvCjpsYtyZsa3q20HpDPKlRXzBNiXoa',1,'yamid cueto','1102825900','3002792493','yamidcuetomazo@gmail.com');
INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO usuarios_roles (usuario_id, roles_id) VALUES (1, 1);
INSERT INTO usuarios_roles (usuario_id, roles_id) VALUES (1, 2);