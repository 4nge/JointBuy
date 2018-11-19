USE jointbuy ;

CREATE TABLE Members (
  ID INT NOT NULL AUTO_INCREMENT,
  telegramUserId DECIMAL(15),
  telegramChatId DECIMAL(15),
  firstName VARCHAR(255) NOT NULL,
  lastName VARCHAR(255),
  alias VARCHAR(255),
  PRIMARY KEY (ID));