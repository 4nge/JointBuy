USE jointbuy ;

CREATE TABLE Remittances (
  ID              INT NOT NULL AUTO_INCREMENT,
  telChatID       DECIMAL(15),
  telInlineMsgID  VARCHAR(50),
  name            VARCHAR(255),
  amount          FLOAT NOT NULL,
  senderID        INT REFERENCES Members (ID),
  recipientID     INT REFERENCES Members (ID),
  remittanceDate  DATETIME NOT NULL,
  active          BOOLEAN,

  PRIMARY KEY (ID));


