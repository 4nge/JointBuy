USE jointbuy ;

CREATE TABLE Members (
  ID INT NOT NULL AUTO_INCREMENT,
  telegramUserId INT,
  telegramChatId INT,
  firstName VARCHAR(255) NOT NULL,
  lastName VARCHAR(255),
  alias VARCHAR(255),
  PRIMARY KEY (ID));