USE jointbuy ;

CREATE TABLE Purchases (
  ID INT NOT NULL AUTO_INCREMENT,
  telegramChatId DECIMAL(15),
  inlineMessageId VARCHAR(50),
  purchaser  INT REFERENCES Members (ID),
  name VARCHAR(255) NOT NULL,
  amount FLOAT NOT NULL,
  purchaseDate DATETIME NOT NULL,

  PRIMARY KEY (ID));
