SET NAMES utf8mb4;

INSERT INTO category (name, type, sort, create_time, update_time, create_user, update_user) VALUES
('动作片', 1, 1, NOW(), NOW(), 1, 1),
('喜剧片', 1, 2, NOW(), NOW(), 1, 1),
('爱情片', 1, 3, NOW(), NOW(), 1, 1),
('科幻片', 1, 4, NOW(), NOW(), 1, 1),
('恐怖片', 1, 5, NOW(), NOW(), 1, 1);

INSERT INTO cinema (name, address, phone, status, create_time, update_time, create_user, update_user) VALUES
('万达影城', '北京市朝阳区建国路93号万达广场', '010-12345678', 1, NOW(), NOW(), 1, 1),
('金逸影城', '北京市海淀区中关村大街19号', '010-87654321', 1, NOW(), NOW(), 1, 1),
('大地影院', '北京市东城区王府井大街255号', '010-11112222', 1, NOW(), NOW(), 1, 1);

INSERT INTO movie (name, category_id, duration, director, actors, description, poster, status, create_time, update_time, create_user, update_user) VALUES
('复仇者联盟4', 1, 181, 'Anthony Russo', 'Robert Downey Jr,Chris Evans,Scarlett Johansson', 'Marvel superheroes assemble for the ultimate battle!', 'https://example.com/poster1.jpg', 1, NOW(), NOW(), 1, 1),
('唐人街探案3', 2, 136, 'Chen Sicheng', 'Wang Baoqiang,Liu Haoran,Zhang Zifeng', 'Comedy detective series part three, full of laughs!', 'https://example.com/poster2.jpg', 1, NOW(), NOW(), 1, 1),
('流浪地球2', 4, 173, 'Guo Fan', 'Wu Jing,Andy Lau,Li Xuejian', 'Chinese sci-fi masterpiece, community of shared future for mankind!', 'https://example.com/poster3.jpg', 1, NOW(), NOW(), 1, 1);

INSERT INTO hall (cinema_id, name, type, rows, cols, capacity, status, create_time, update_time, create_user, update_user) VALUES
(1, '1号厅', 1, 10, 15, 150, 1, NOW(), NOW(), 1, 1),
(1, '2号厅', 2, 12, 18, 216, 1, NOW(), NOW(), 1, 1),
(1, '3号厅', 1, 8, 12, 96, 1, NOW(), NOW(), 1, 1),
(2, '1号厅', 1, 10, 15, 150, 1, NOW(), NOW(), 1, 1),
(2, '2号厅', 2, 12, 18, 216, 1, NOW(), NOW(), 1, 1),
(3, '1号厅', 1, 10, 15, 150, 1, NOW(), NOW(), 1, 1);

INSERT INTO schedule (movie_id, cinema_id, hall_id, show_date, start_time, end_time, price, status, create_time, update_time, create_user, update_user) VALUES
(1, 1, 1, '2026-03-22', '10:00:00', '13:01:00', 45.00, 1, NOW(), NOW(), 1, 1),
(1, 1, 1, '2026-03-22', '14:00:00', '17:01:00', 45.00, 1, NOW(), NOW(), 1, 1),
(1, 1, 2, '2026-03-22', '18:00:00', '21:01:00', 55.00, 1, NOW(), NOW(), 1, 1),
(2, 2, 4, '2026-03-22', '11:00:00', '13:16:00', 40.00, 1, NOW(), NOW(), 1, 1),
(3, 3, 6, '2026-03-22', '13:00:00', '15:53:00', 50.00, 1, NOW(), NOW(), 1, 1);

INSERT INTO seat (hall_id, row_num, col_num, status, create_time, update_time, create_user, update_user) VALUES
(1, 1, 1, 1, NOW(), NOW(), 1, 1), (1, 1, 2, 1, NOW(), NOW(), 1, 1), (1, 1, 3, 1, NOW(), NOW(), 1, 1), (1, 1, 4, 1, NOW(), NOW(), 1, 1), (1, 1, 5, 1, NOW(), NOW(), 1, 1),
(1, 2, 1, 1, NOW(), NOW(), 1, 1), (1, 2, 2, 1, NOW(), NOW(), 1, 1), (1, 2, 3, 1, NOW(), NOW(), 1, 1), (1, 2, 4, 1, NOW(), NOW(), 1, 1), (1, 2, 5, 1, NOW(), NOW(), 1, 1),
(1, 3, 1, 1, NOW(), NOW(), 1, 1), (1, 3, 2, 1, NOW(), NOW(), 1, 1), (1, 3, 3, 1, NOW(), NOW(), 1, 1), (1, 3, 4, 1, NOW(), NOW(), 1, 1), (1, 3, 5, 1, NOW(), NOW(), 1, 1);

INSERT INTO user (openid, name, phone, sex, id_number, avatar, status, create_time, update_time) VALUES
('openid_001', 'Zhang San', '13900139001', '1', '110101199001011234', 'https://example.com/avatar1.jpg', 1, NOW(), NOW()),
('openid_002', 'Li Si', '13900139002', '2', '110101199001011235', 'https://example.com/avatar2.jpg', 1, NOW(), NOW()),
('openid_003', 'Wang Wu', '13900139003', '1', '110101199001011236', 'https://example.com/avatar3.jpg', 1, NOW(), NOW());
