-- password 1234 (bcrypt)
INSERT INTO users(username,password,role) VALUES
('admin','{bcrypt}$2a$10$E8XyN0XQe1PrQfI6m3vI0e0v1qj3r4u8t2jC0m7x1S7E2X2qCqZ.O','ADMIN');
INSERT INTO pets(name,species,age,adopted) VALUES
('Luna','cat',2,false),('Rocky','dog',4,true),('Niebla','cat',1,false);
