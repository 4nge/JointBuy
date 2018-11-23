USE jointbuy ;

CREATE TABLE Chat (
  ID INT NOT NULL AUTO_INCREMENT,
  telegramChatId DECIMAL(15),
  addUserMode BOOLEAN,
  PRIMARY KEY (ID));
