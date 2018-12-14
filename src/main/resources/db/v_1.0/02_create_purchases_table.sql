USE jointbuy ;

CREATE TABLE Purchases (
  ID INT NOT NULL AUTO_INCREMENT,
  telChatID DECIMAL(15),
  telInlineMsgID VARCHAR(50),
  purchaserID  INT REFERENCES Members (ID),
  name VARCHAR(255) NOT NULL,
  amount FLOAT NOT NULL,
  purchaseDate DATETIME NOT NULL,
  active BOOLEAN,

  PRIMARY KEY (ID));
