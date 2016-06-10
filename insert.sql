Database Insertion data:

/* Person Table */
INSERT INTO `Person`(`firstname`,`lastname`,`birthdate`,`email`,`gender`) 
VALUES  ('Bob', 'Smith','1987-03-23','bob.smith@gmail.com','M'),
('Andrea', 'Bonte','1990-10-13','andrea.bonte@gmail.com','M'),
('Martina', 'Moss','1992-12-14','martina.moss@libero.it','F'),
('Gaia', 'Baldo','1989-02-15','gaia.baldo@hotmail.com','F') ;


/* Measure Table */
INSERT INTO `Measure`(`idPerson`,`idMeasureDefinition`,`value`,`timestamp`) 
VALUES ('1','1','87','2015-02-01'),
('1','1','79','2015-12-15'),
('1','4','75','2014-05-06'),
('1','4','70','2015-10-01'),
('1','7','13550','2015-11-06'),
('2','2','1.80','2014-02-23'),
('2','2','1.82','2015-01-23'),
('2','8','1','2014-10-25'),
('3','2','1.60','2014-08-24'),
('3','6','60','2013-10-14'),
('3','7','30000','2015-05-07'),
('3','7','70000','2015-10-07'),
('3','8','2','2015-01-10'),
('3','9','6','2015-09-02');


/* MeasureDefinition Table */
INSERT INTO `MeasureDefinition`(`measureName`,`measureType`,`startValue`,`endValue`,`alarmLevel`)
VALUES ('weight','double','40','180','1'),
('height','double','120','220','5'),
('bmi','double','18','25','3'),
('blood pressure minimum','double','70','90','1'),
('blood pressure maximum','double','110','130','1'),
('heart rate','integer','50','90','2'),
('steps','integer','5000','10000','5'),
('water','double','2','3','2'),
('sleep','double','6','8','4');


/* Goal Table */
INSERT INTO `Goal`(`idPerson`,`idMeasureDefinition`,`value`,`type`,`startDateGoal`,`endDateGoal`,`achieved`) 
VALUES ('1','1','70','weight','2015-02-10','2016-01-15','0'),
 ('2','2','1.85','height', '2014-03-16','2015-10-16','1'),
 ('2','8','3','water', '2014-10-29','2014-12-01','1'),
 ('3','7','100000000','steps', '2015-10-10','2015-11-10','0'),
 ('3','9','8','sleep', '2015-09-10','2015-09-20','1');


/* Reminder Table */
INSERT INTO `Reminder`(`idPerson`,`description`,`reminderType`,`weeklyDay`,`monthDay`,`specificDate`,`dateRegistered`) 
VALUES ('1','Go to walk at 5pm', 'daily', NULL, NULL, NULL, '30-01-2016'),
('1','Reminder to check blood presure at 10pm', 'daily', NULL, NULL, NULL, '30-01-2016'),
('1','Reminder to check', 'weekly', "Friday", NULL, NULL, '30-01-2016'),
('1','Go to doctor appointment', 'specific_date', NULL, NULL, '04-06-2016', '30-01-2016'),
('2','Go to walk at 6pm', 'daily', NULL, NULL, NULL, '30-01-2016'),
('2','Reminder to check weight before sleep', 'daily', 'Friday', NULL, NULL, '30-01-2016'),
('2','Go to SPA Beauty', 'monthly', NULL, '27', NULL, '30-01-2016'),
('3','Go to walk at 5pm', 'daily', NULL, NULL, NULL, '30-01-2016'),
('3','Go to doctor appointment', 'specific_date', NULL, NULL, '04-06-2016', '30-01-2016'),
('5','Go to doctor appointment', 'specific_date', NULL, NULL, '04-06-2016', '30-01-2016'),
('5','Reminder to check heart rate at 8am', 'weekly', 'Monday', NULL, NULL, '30-01-2016'),
('6','Go to walk at 5pm', 'daily', NULL, NULL, NULL, '30-01-2016'),
('6','Go to SPA Beauty', 'monthly', NULL, '27', NULL, '30-01-2016');