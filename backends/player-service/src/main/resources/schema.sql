USE `player-svc`;

DROP TABLE IF EXISTS `players`;
DROP TABLE IF EXISTS `player_authorities`;

CREATE TABLE `players` (
  `id` varchar(36) NOT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `player_authorities` (
  `player_id` CHAR(36) NOT NULL,
  `authority` VARCHAR(255) NOT NULL,
  FOREIGN KEY (`player_id`) REFERENCES `players`(`id`)
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
