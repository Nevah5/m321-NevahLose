/* Create users */
CREATE USER 'game-svc' @'%' IDENTIFIED BY 'Rockslide0-Stress0-Agnostic7-Although0';
CREATE USER 'player-svc' @'%' IDENTIFIED BY 'Five0-Deepen7-Trowel4-Uncaring7';
CREATE USER 'card-svc' @'%' IDENTIFIED BY 'Module0-Cauterize9-Gothic8-Karaoke7';

/* Create databases */
CREATE DATABASE IF NOT EXISTS `game-svc`;
CREATE DATABASE IF NOT EXISTS `player-svc`;
CREATE DATABASE IF NOT EXISTS `card-svc`;

/* Update privileges */
GRANT ALL PRIVILEGES ON `game-svc`.* TO 'game-svc' @'%';
GRANT ALL PRIVILEGES ON `player-svc`.* TO 'player-svc' @'%';
GRANT ALL PRIVILEGES ON `card-svc`.* TO 'card-svc' @'%';
