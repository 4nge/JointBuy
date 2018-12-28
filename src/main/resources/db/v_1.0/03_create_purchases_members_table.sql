USE jointbuy ;

CREATE TABLE PurchaseMembers (
  ID          INT NOT NULL AUTO_INCREMENT,
  purchaseID  INT REFERENCES Purchases (ID),
  memberID    INT REFERENCES Members (ID),

  PRIMARY KEY (ID));
