USE jointbuy ;

CREATE TABLE Members (
  ID        INT NOT NULL AUTO_INCREMENT,
  telUserID DECIMAL(15),
  telChatID DECIMAL(15),
  firstName VARCHAR(255) NOT NULL,
  lastName  VARCHAR(255),
  alias     VARCHAR(255),

  PRIMARY KEY (ID));