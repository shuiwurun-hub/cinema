-- =====================================================
-- Clean expired schedules and generate new schedules for next 7 days
-- Each movie will have schedules in selected cinemas only
-- =====================================================

USE cinema_ticketing;

-- 1. Delete all schedules before today
DELETE FROM schedule 
WHERE show_date < CURDATE();

-- Check deletion result
SELECT CONCAT('Deleted ', ROW_COUNT(), ' expired schedules') AS result;

-- 2. Delete related ticket data (if exists)
DELETE FROM ticket_detail 
WHERE ticket_id IN (
    SELECT id FROM ticket 
    WHERE schedule_id IN (
        SELECT id FROM schedule 
        WHERE show_date < CURDATE()
    )
);

DELETE FROM ticket 
WHERE schedule_id NOT IN (
    SELECT id FROM schedule
);

-- 3. Generate schedule data for today to next 7 days (8 days total)
-- Get all active movies
DROP TEMPORARY TABLE IF EXISTS active_movies;
CREATE TEMPORARY TABLE active_movies AS
SELECT id, name, duration 
FROM movie 
WHERE status = 1 
AND release_date <= CURDATE();

-- Get all cinemas and halls
DROP TEMPORARY TABLE IF EXISTS cinema_halls;
CREATE TEMPORARY TABLE cinema_halls AS
SELECT c.id AS cinema_id, c.name AS cinema_name, h.id AS hall_id, h.name AS hall_name
FROM cinema c
JOIN hall h ON c.id = h.cinema_id
WHERE c.status = 1 AND h.status = 1;

-- Generate schedule data for each movie in selected cinemas
-- Each movie will have schedules in 1-2 cinemas
INSERT INTO schedule (movie_id, cinema_id, hall_id, show_date, show_time, end_time, price, status, create_time, update_time)
SELECT 
    m.id AS movie_id,
    ch.cinema_id,
    ch.hall_id,
    DATE_ADD(CURDATE(), INTERVAL n.day_offset DAY) AS show_date,
    CASE 
        WHEN n.show_index = 1 THEN '10:00:00'
        WHEN n.show_index = 2 THEN '13:30:00'
        WHEN n.show_index = 3 THEN '16:00:00'
        WHEN n.show_index = 4 THEN '19:00:00'
        WHEN n.show_index = 5 THEN '21:30:00'
    END AS show_time,
    CASE 
        WHEN n.show_index = 1 THEN '12:00:00'
        WHEN n.show_index = 2 THEN '15:30:00'
        WHEN n.show_index = 3 THEN '18:00:00'
        WHEN n.show_index = 4 THEN '21:00:00'
        WHEN n.show_index = 5 THEN '23:30:00'
    END AS end_time,
    CASE 
        WHEN n.show_index IN (1, 2) THEN 35.00
        WHEN n.show_index IN (3, 4) THEN 45.00
        ELSE 40.00
    END AS price,
    1 AS status,
    NOW() AS create_time,
    NOW() AS update_time
FROM active_movies m
CROSS JOIN (
    -- Select 1-2 cinemas for each movie based on movie ID
    SELECT 
        cinema_id, hall_id, cinema_name, hall_name,
        (ROW_NUMBER() OVER (ORDER BY cinema_id)) AS cinema_rank
    FROM cinema_halls
    WHERE cinema_id % 2 = 1 OR cinema_id % 3 = 0
) ch
CROSS JOIN (
    SELECT 0 AS day_offset, 1 AS show_index UNION ALL
    SELECT 0, 2 UNION ALL
    SELECT 0, 3 UNION ALL
    SELECT 0, 4 UNION ALL
    SELECT 0, 5 UNION ALL
    SELECT 1, 1 UNION ALL SELECT 1, 2 UNION ALL SELECT 1, 3 UNION ALL SELECT 1, 4 UNION ALL SELECT 1, 5 UNION ALL
    SELECT 2, 1 UNION ALL SELECT 2, 2 UNION ALL SELECT 2, 3 UNION ALL SELECT 2, 4 UNION ALL SELECT 2, 5 UNION ALL
    SELECT 3, 1 UNION ALL SELECT 3, 2 UNION ALL SELECT 3, 3 UNION ALL SELECT 3, 4 UNION ALL SELECT 3, 5 UNION ALL
    SELECT 4, 1 UNION ALL SELECT 4, 2 UNION ALL SELECT 4, 3 UNION ALL SELECT 4, 4 UNION ALL SELECT 4, 5 UNION ALL
    SELECT 5, 1 UNION ALL SELECT 5, 2 UNION ALL SELECT 5, 3 UNION ALL SELECT 5, 4 UNION ALL SELECT 5, 5 UNION ALL
    SELECT 6, 1 UNION ALL SELECT 6, 2 UNION ALL SELECT 6, 3 UNION ALL SELECT 6, 4 UNION ALL SELECT 6, 5 UNION ALL
    SELECT 7, 1 UNION ALL SELECT 7, 2 UNION ALL SELECT 7, 3 UNION ALL SELECT 7, 4 UNION ALL SELECT 7, 5
) n
WHERE NOT EXISTS (
    -- Avoid duplicate inserts
    SELECT 1 FROM schedule s
    WHERE s.movie_id = m.id
    AND s.cinema_id = ch.cinema_id
    AND s.hall_id = ch.hall_id
    AND s.show_date = DATE_ADD(CURDATE(), INTERVAL n.day_offset DAY)
    AND s.show_time = CASE 
        WHEN n.show_index = 1 THEN '10:00:00'
        WHEN n.show_index = 2 THEN '13:30:00'
        WHEN n.show_index = 3 THEN '16:00:00'
        WHEN n.show_index = 4 THEN '19:00:00'
        WHEN n.show_index = 5 THEN '21:30:00'
    END
);

-- Check generation result by movie
SELECT 
    m.name AS movie_name,
    COUNT(*) AS schedule_count
FROM schedule s
JOIN movie m ON s.movie_id = m.id
WHERE s.show_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
GROUP BY m.id, m.name
ORDER BY schedule_count DESC;

-- Check generation result by date
SELECT 
    DATE_ADD(CURDATE(), INTERVAL n DAY) AS date,
    COUNT(*) AS schedule_count
FROM schedule
CROSS JOIN (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7) days
WHERE show_date = DATE_ADD(CURDATE(), INTERVAL n DAY)
GROUP BY n
ORDER BY n;

-- Drop temporary tables
DROP TEMPORARY TABLE IF EXISTS active_movies;
DROP TEMPORARY TABLE IF EXISTS cinema_halls;

-- Display final statistics
SELECT 'Schedule update completed!' AS message;
SELECT 
    COUNT(*) AS total_schedules,
    MIN(show_date) AS earliest_date,
    MAX(show_date) AS latest_date,
    COUNT(DISTINCT movie_id) AS movie_count,
    COUNT(DISTINCT cinema_id) AS cinema_count
FROM schedule
WHERE status = 1;
