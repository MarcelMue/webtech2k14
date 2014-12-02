
CREATE TABLE `cinemas_movies` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `movie_id` int(11) NOT NULL,
  `cinema` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `movie_id` (`movie_id`,`cinema`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `cinemovies_seats` (
  `cinemovie_id` int(11) NOT NULL,
  `reservation_id` int(11) NOT NULL,
  `seat` int(11) NOT NULL,
  UNIQUE KEY `cinemovie_id` (`cinemovie_id`,`seat`),
  KEY `reservation_id` (`reservation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `movies` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `movies` WRITE;

INSERT INTO `movies` (`id`, `name`)
VALUES
	(1,'The Fith Element'),
	(2,'Fight Club'),
	(3,'Office Space'),
	(4,'Goodfellas'),
	(5,'The Shawshank Redemption'),
	(6,'Aliens'),
	(7,'Avatar'),
	(8,'Pandorum'),
	(9,'The Departed'),
	(10,'The Dark Knight Rises'),
	(11,'The Lord of the Rings: The Two Towers'),
	(12,'The Matrix'),
	(13,'Star Wars: Episode VI - Return of the Jedi'),
	(14,'The Hitchhiker\'s Guide to the Galaxy'),
	(15,'Interstellar'),
	(16,'Winnie-the-Pooh');

UNLOCK TABLES;

CREATE TABLE `reservations` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `cinemovie_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cinemovie_id` (`cinemovie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
