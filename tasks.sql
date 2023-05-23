-- Adminer 4.8.1 MySQL 10.5.15-MariaDB-0+deb11u1 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `tasks`;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(150) NOT NULL,
  `priority` varchar(8) NOT NULL,
  `status` varchar(10) NOT NULL,
  `due_date` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `tasks` (`id`, `description`, `priority`, `status`, `due_date`) VALUES
(1,	'Janvier',	'Defcon 1',	'A faire',	'16/01/2023'),
(2,	'février',	'Defcon 1',	'A faire',	'16/02/2023'),
(3,	'mars',	'Defcon 1',	'A faire',	'16/03/2023'),
(4,	'avril',	'Defcon 1',	'A faire',	'16/04/2023'),
(5,	'mai',	'Defcon 1',	'A faire',	'16/05/2023'),
(6,	'juin',	'Defcon 1',	'A faire',	'16/06/2023'),
(7,	'juillet',	'Defcon 1',	'A faire',	'16/07/2023'),
(8,	'août',	'Defcon 1',	'A faire',	'16/08/2023'),
(9,	'septembre',	'Defcon 1',	'A faire',	'16/09/2023'),
(10,	'octobre',	'Defcon 1',	'A faire',	'16/10/2023'),
(11,	'novembre',	'Defcon 1',	'A faire',	'16/11/2023');

-- 2023-05-23 07:38:53
