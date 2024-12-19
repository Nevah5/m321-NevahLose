USE `player-svc`;

DROP TABLE IF EXISTS `players`;

CREATE TABLE `players` (
  `id` varchar(36) NOT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);
