USE jointbuy ;

CREATE TABLE MessageTypes (
  ID INT NOT NULL AUTO_INCREMENT,
  type VARCHAR(255),
  PRIMARY KEY (ID));

CREATE TABLE Messages (
  ID INT NOT NULL AUTO_INCREMENT,
  telegramChatId DECIMAL(15),
  telegramMsgId DECIMAL(15),
  typeID INT REFERENCES MessageTypes (ID),
  PRIMARY KEY (ID));

insert into MessageTypes values(
  1, 'start message'
);