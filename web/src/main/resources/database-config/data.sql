INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) values ('Beauty-certificate', 'The best place in the world', 100.00, 31, '2021-11-20T01:02:03',  '2021-11-20T01:02:03');
INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) values ('AutoSalon-certificate', 'The best salon in the world', 120.00, 31, '2021-11-20T01:02:03',  '2021-11-20T01:02:03');
INSERT INTO tags (name) values ('#Beauty');
INSERT INTO tags (name) values ('#Salon');
INSERT INTO m2m_gift_certificates_tags (gift_certificate_id, tag_id) values (1,1);
INSERT INTO m2m_gift_certificates_tags (gift_certificate_id, tag_id) values (1,2);
INSERT INTO m2m_gift_certificates_tags (gift_certificate_id, tag_id) values (2,2);
INSERT INTO m2m_gift_certificates_tags (gift_certificate_id, tag_id) values (2,1);
INSERT INTO users (username, password, role) values ('Nikitaa', '$2a$12$rNeauejmCsaJ/fxxA0X2qu9imRoc84ftAzZFxQ1JDQ59omelUJSfm', 'ADMIN');
INSERT INTO orders (price, purchase_time, user_id, gift_certificate_id) values (152.33, '2021-11-20T01:02:03', 1,1)