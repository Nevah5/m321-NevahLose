USE `card-svc`;

DROP TABLE IF EXISTS `cards`;

CREATE TABLE `cards` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `backgroundFilename` varchar(255) NOT NULL,
  `subjectFilename` varchar(255) NOT NULL,
  `cardType` int NOT NULL,
  `amountInDeck` int NOT NULL,
  PRIMARY KEY (`id`)
);
