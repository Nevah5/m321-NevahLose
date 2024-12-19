USE `player-svc`;

DROP TABLE IF EXISTS `players`;

CREATE TABLE `players` (
  `id` varchar(36) NOT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `x_username_adj`;

CREATE TABLE `x_username_adj` (
  `id` int NOT NULL AUTO_INCREMENT,
  `value` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `x_username_nom`;

CREATE TABLE `x_username_nom` (
  `id` int NOT NULL AUTO_INCREMENT,
  `value` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);
