CREATE TABLE inventory(
ProductID INTEGER NOT NULL auto_increment,
ProductDesc varchar(30),
ProductPrice numeric(8,2),
ProductStockAmount integer,
PRIMARY KEY (ProductID)
);

 CREATE TABLE orders(
 OrderID INTEGER NOT NULL auto_increment,
 OrderType varchar(30),
 OrderCompleted INTEGER,
 OrderPlaced Date,
 CONSTRAINT orders_chek CHECK (OrderType = 'InStore' OR OrderType = 'Delivery' OR OrderType = 'Collection'),
 CONSTRAINT complet_chek CHECK (OrderCompleted = 0 OR OrderCompleted = 1),
 PRIMARY KEY (OrderID)
 );
 
  CREATE TABLE order_products(
 OrderID INTEGER,
 ProductID INTEGER,
 ProductQuantity INTEGER,
 CONSTRAINT op_id
 FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 ON DELETE CASCADE
 ON UPDATE CASCADE,
 CONSTRAINT prod_id
 FOREIGN KEY (ProductID) REFERENCES inventory(ProductID)
 ON DELETE CASCADE
 ON UPDATE CASCADE
 );
 
  CREATE TABLE deliveries(
 OrderID INTEGER,
 FName varchar(30),
 LName varchar(30),
 House varchar(30),
 Street varchar(30),
 City varchar(30),
 DeliveryDate Date,
 CONSTRAINT de_id
 FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 ON DELETE CASCADE
 ON UPDATE CASCADE
 );
 
  CREATE TABLE collections(
 OrderID INTEGER,
 FName varchar(30),
 LName varchar(30),
 CollectionDate Date,
 CONSTRAINT co_id
 FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 ON DELETE CASCADE
 ON UPDATE CASCADE
 );
 
  CREATE TABLE staff(
 StaffID INTEGER NOT NULL auto_increment,
 FName varchar(30),
 LName varchar(30),
 PRIMARY KEY (StaffID)
 );
 
  CREATE TABLE staff_orders(
 StaffID INTEGER,
 OrderID INTEGER,
 CONSTRAINT s_id
 FOREIGN KEY (StaffID) REFERENCES staff(staffID)
 ON DELETE CASCADE
 ON UPDATE CASCADE,
 CONSTRAINT sO_id
 FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 ON DELETE CASCADE
 ON UPDATE CASCADE
 );
 