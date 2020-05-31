/**
 * @author Abdullah Muhammad-Kamal
**/
// CREATE TABLE inventory(
// ProductID INTEGER NOT NULL auto_increment,
// ProductDesc varchar(30),
// ProductPrice numeric(8,2),
// ProductStockAmount integer,
// PRIMARY KEY (ProductID)
// );

 // CREATE TABLE orders(
 // OrderID INTEGER NOT NULL auto_increment,
 // OrderType varchar(30),
 // OrderCompleted INTEGER,
 // OrderPlaced Date,
 // CONSTRAINT orders_chek CHECK (OrderType = 'InStore' OR OrderType = 'Delivery' OR OrderType = 'Collection'),
 // CONSTRAINT complet_chek CHECK (OrderCompleted = 0 OR OrderCompleted = 1),
 // PRIMARY KEY (OrderID)
 // );

 // CREATE TABLE order_products(
 // OrderID INTEGER,
 // ProductID INTEGER,
 // ProductQuantity INTEGER,
 // CONSTRAINT op_id
 // FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 // ON DELETE CASCADE
 // ON UPDATE CASCADE,
 // CONSTRAINT prod_id
 // FOREIGN KEY (ProductID) REFERENCES inventory(ProductID)
 // ON DELETE CASCADE
 // ON UPDATE CASCADE
 // );
//
 // CREATE TABLE deliveries(
 // OrderID INTEGER,
 // FName varchar(30),
 // LName varchar(30),
 // House varchar(30),
 // Street varchar(30),
 // City varchar(30),
 // DeliveryDate Date,
 // CONSTRAINT de_id
 // FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 // ON DELETE CASCADE
 // ON UPDATE CASCADE
 // );
//
 // CREATE TABLE collections(
 // OrderID INTEGER,
 // FName varchar(30),
 // LName varchar(30),
 // CollectionDate Date,
 // CONSTRAINT co_id
 // FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 // ON DELETE CASCADE
 // ON UPDATE CASCADE
 // );
//
 // CREATE TABLE staff(
 // StaffID INTEGER NOT NULL auto_increment,
 // FName varchar(30),
 // LName varchar(30),
 // PRIMARY KEY (StaffID)
 // );

 // CREATE TABLE staff_orders(
 // StaffID INTEGER,
 // OrderID INTEGER,
 // CONSTRAINT s_id  ////Constraint So that when a Staff(parent) is deleted then the Staff orders(child) tuples of that staff is also deleted.
 // FOREIGN KEY (StaffID) REFERENCES staff(staffID)
 // ON DELETE CASCADE
 // ON UPDATE CASCADE,
 // CONSTRAINT sO_id
 // FOREIGN KEY (OrderID) REFERENCES orders(OrderID)
 // ON DELETE CASCADE
 // ON UPDATE CASCADE
 // );


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


class Assignment {
 final static int uncompleted = 0;
 final static int completed = 1;
 final static int PRODUCT_SALE_THESHOLD = 100;
 final static int STAFF_SALE_THRESHOLD = 100;

 private static String readEntry(String prompt) {
  try {
   StringBuffer buffer = new StringBuffer();
   System.out.print(prompt);
   System.out.flush();
   int c = System.in.read();
   while (c != '\n' && c != -1) {
    buffer.append((char) c);
    c = System.in.read();
   }
   return buffer.toString().trim();
  } catch (IOException e) {
   return "";
  }
 }
 //Creating a method to create orders, this method is used multiple times: in Option one, two and three. so in order to improve readability and the size
 //of the file it is best to make it into a function.
 /**
  * [create_orders description]
  * @param  conn         An open database connection.
  * @param  type         order type, either 'InStore', 'delivery' or 'collection'.
  * @param  status       status of the order, either uncompleted or completed.
  * @param  orderDate    date in which the order took place.
  */
 public static void create_orders(Connection conn, String type, int status, String orderDate) throws SQLException {
  PreparedStatement p = null;

  String order_ins = "insert into orders" + " values(null,?,?,?)";
  p = conn.prepareStatement(order_ins);
  p.clearParameters();
  p.setString(1, type);
  p.setInt(2, status);
  p.setString(3, orderDate);
  p.executeUpdate();

  if (p != null) p.close();


 }
 //Creating a method to get the most recent OrderID inserted, this method is used multiple times: in Option one, two and three. so in order to improve readability and the size
 //of the file it is best to make it into a function.
 /**
  * [getmrecent description]
  * @param  conn         An open database connection
  */
 public static int getmrecent(Connection conn) throws SQLException {
  PreparedStatement opt1_st = null;
  ResultSet rset1 = null;
  int o_id = 0;
  String order_que = "SELECT OrderID" + " FROM orders" + " ORDER BY OrderID" + " DESC LIMIT 1";
  opt1_st = conn.prepareStatement(order_que);
  opt1_st.clearParameters();
  rset1 = opt1_st.executeQuery();
  while (rset1.next()) {
   o_id = rset1.getInt(1);
  }

  if (opt1_st != null) opt1_st.close();
  return o_id;
 }
 //Creating a method to create staff_orders, this method is used multiple times: in Option one, two and three. so in order to improve readability and the size
 //of the file it is best to make it into a function.
 /**
  * [ins_staffo description]
  * @param  conn         An open database connection.
  * @param  staffID      The staffID associated with the order to be inserted into staff_orders.
  * @param  orderID      The orderID of the order to be inserted into staff_orders.
  */
 public static void ins_staffo(Connection conn, int staffID, int orderID) throws SQLException {
  PreparedStatement st_or = null;
  String orderst_ins = "INSERT INTO staff_orders VALUES(?,?)";
  st_or = conn.prepareStatement(orderst_ins);
  st_or.clearParameters();
  st_or.setInt(1, staffID);
  st_or.setInt(2, orderID);
  st_or.executeUpdate();
  if (st_or != null) st_or.close();


 }
 //Creating a method to insert into order_products, this method is used multiple times: in Option one, two and three. so in order to improve readability and the size
 //of the file it is best to make it into a function.
 /**
  * [ins_orderp description]
  * @param  conn         An open database connection
  * @param  o_id         The orderID of the product we are going to insert into order_products.
  * @param  productID    The productID of the product we are going to inset into irder_products.
  * @param  quantity     The quantity of the product we are inserting into order_products.
  */
 public static void ins_orderp(Connection conn, int o_id, int productID, int quantity) throws SQLException {
  PreparedStatement op_ps = null;

  String orderp_ins = "insert into order_products " + "values(?,?,?)";
  op_ps = conn.prepareStatement(orderp_ins);
  op_ps.clearParameters();
  op_ps.setInt(1, o_id);
  op_ps.setInt(2, productID);
  op_ps.setInt(3, quantity);
  op_ps.executeUpdate();

  if (op_ps != null) op_ps.close();

 }
 //Creating a method to update the stock amount for products in the inventory, this method is used multiple times: in Option one, two and three. so in order to improve readability and the size
 //of the file it is best to make it into a function.
 /**
  * [update_inv description]
  * @param  conn         An open database connection.
  * @param  quantity     This is the quantity of the product we want to take from the inventory stock amount for that product as we update inventory.
  * @param  productID    The productID of the product in the inventory whose stock amount needs to be updated.
  */
 public static void update_inv(Connection conn, int quantity, int productID) throws SQLException {
  PreparedStatement inv_ps = null;
  String inv_upd = "UPDATE inventory" + " SET ProductStockAmount = ProductStockAmount - ?" + " WHERE ProductID = ?";
  inv_ps = conn.prepareStatement(inv_upd);
  inv_ps.clearParameters();
  inv_ps.setInt(1, quantity);
  inv_ps.setInt(2, productID);
  inv_ps.executeUpdate();

  if (inv_ps != null) inv_ps.close();


 }
 //Creating a method to print the stock amount for a specified product, this method is used multiple times: in Option one, two and three. so in order to improve readability and the size
 //of the file it is best to make it into a function.
 /**
  * [print_stock description]
  * @param  conn         An open database connection.
  * @param  productID    This is the productID of the product we want to print its stock amount.
  */
 public static void print_stock(Connection conn, int productID) throws SQLException {
  PreparedStatement stk_ps = null;
  ResultSet rset2 = null;
  String prod_print = "SELECT ProductStockAmount FROM inventory WHERE ProductID = ?";
  stk_ps = conn.prepareStatement(prod_print);
  stk_ps.clearParameters();
  stk_ps.setInt(1, productID);
  rset2 = stk_ps.executeQuery();
  while (rset2.next()) {
   System.out.println("Product ID " + productID + " stock is now at " + rset2.getInt(1));
  }

  if (stk_ps != null) stk_ps.close();
  if (rset2 != null) rset2.close();


 }
 /**
  * @param conn An open database connection
  * @param productIDs An array of productIDs associated with an order
  * @param quantities An array of quantities of a product. The index of a quantity correspeonds with an index in productIDs
  * @param orderDate A string in the form of 'DD-Mon-YY' that represents the date the order was made
  * @param staffID The id of the staff member who sold the order
  */
 public static void option1(Connection conn, int[] productIDs, int[] quantities, String orderDate, int staffID) {
  // complete - Code for option 1 goes here
  int o_id = 0;
  try {
   //need to create the order, inserting into orders table, usnig the values taken in as parameters.
   create_orders(conn, "InStore", completed, orderDate);
   //get the orderID of the most recently inserted order.
   o_id = getmrecent(conn);
   //with this orderID insert it into staff orders with the staffID entered as parameters.
   ins_staffo(conn, staffID, o_id);
   //for each of of the product associated with the quantities ass them to order_products, update the inventory and print the updated stock.
   for (int i = 0; i < productIDs.length; i++) {
    ins_orderp(conn, o_id, productIDs[i], quantities[i]);
    update_inv(conn, quantities[i], productIDs[i]);
    print_stock(conn, productIDs[i]);
   }
  } catch (SQLException e) {
   e.printStackTrace();
   return;
  }
 }

 /**
  * @param conn An open database connection
  * @param productIDs An array of productIDs associated with an order
  * @param quantities An array of quantities of a product. The index of a quantity correspeonds with an index in productIDs
  * @param orderDate A string in the form of 'DD-Mon-YY' that represents the date the order was made
  * @param collectionDate A string in the form of 'DD-Mon-YY' that represents the date the order will be collected
  * @param fName The first name of the customer who will collect the order
  * @param LName The last name of the customer who will collect the order
  * @param staffID The id of the staff member who sold the order
  */
 public static void option2(Connection conn, int[] productIDs, int[] quantities, String orderDate, String collectionDate, String fName, String LName, int staffID) {
  // complete - Code for option 2 goes here
  int o_id = 0;
  PreparedStatement ci_ps = null;
  try {
   //create a new order, inserting into the orders table, with the values entered into parameters.
   create_orders(conn, "Collection", uncompleted, orderDate);
   //get the orderID of the just inserted order.
   o_id = getmrecent(conn);
   //with this most recent orderID enter it into staff_orders with the entered staffID as parameters.
   ins_staffo(conn, staffID, o_id);
   //insert this into collections table with parameters entered into this function.
   String coll_ins = "insert into collections " + "values(?,?,?,?)";
   ci_ps = conn.prepareStatement(coll_ins);
   ci_ps.clearParameters();
   ci_ps.setInt(1, o_id);
   ci_ps.setString(2, fName);
   ci_ps.setString(3, LName);
   ci_ps.setString(4, collectionDate);

   ci_ps.executeUpdate();
   //for all the products entered for this order and their relative quantities, enter this into order_products and update inventory and print the inventory.
   for (int i = 0; i < productIDs.length; i++) {
    ins_orderp(conn, o_id, productIDs[i], quantities[i]);
    update_inv(conn, quantities[i], productIDs[i]);
    print_stock(conn, productIDs[i]);
   }
  } catch (SQLException e) {
   e.printStackTrace();
   try {
    if (ci_ps != null) ci_ps.close();
   } catch (SQLException ex) {
    ex.printStackTrace();
    return;
   }
   return;
  } finally {
   try {
    if (ci_ps != null) ci_ps.close();
   } catch (SQLException ef) {
    ef.printStackTrace();
    return;
   }
  }
 }



 /**
  * @param conn An open database connection
  * @param productIDs An array of productIDs associated with an order
  * @param quantities An array of quantities of a product. The index of a quantity correspeonds with an index in productIDs
  * @param orderDate A string in the form of 'DD-Mon-YY' that represents the date the order was made
  * @param deliveryDate A string in the form of 'DD-Mon-YY' that represents the date the order will be delivered
  * @param fName The first name of the customer who will receive the order
  * @param LName The last name of the customer who will receive the order
  * @param house The house name or number of the delivery address
  * @param street The street name of the delivery address
  * @param city The city name of the delivery address
  * @param staffID The id of the staff member who sold the order
  */
 public static void option3(Connection conn, int[] productIDs, int[] quantities, String orderDate, String deliveryDate, String fName, String LName,
  String house, String street, String city, int staffID) {
  // complete - Code for option 3 goes here
  int o_id = 0;
  PreparedStatement de_ps = null;
  try {
   //create a new order that is of type delivery from the entered parameters.
   create_orders(conn, "Delivery", uncompleted, orderDate);
   //get the orderID of this order.
   o_id = getmrecent(conn);
   if (o_id < 0) return;
   //with this orderID inser the order into staff_orders with the staffID parameter.
   ins_staffo(conn, staffID, o_id);
   //This is a delivery order so enter this order into deliveries with the relevent information.
   String deli_ins = "insert into deliveries " + "values(?,?,?,?,?,?,?)";
   de_ps = conn.prepareStatement(deli_ins);
   de_ps.clearParameters();
   de_ps.setInt(1, o_id);
   de_ps.setString(2, fName);
   de_ps.setString(3, LName);
   de_ps.setString(4, house);
   de_ps.setString(5, street);
   de_ps.setString(6, city);
   de_ps.setString(7, deliveryDate);

   de_ps.executeUpdate();
   // for all products in this order with the quantities enter this into order_products. update the inventory and print the updated inventory.
   for (int i = 0; i < productIDs.length; i++) {
    ins_orderp(conn, o_id, productIDs[i], quantities[i]);
    update_inv(conn, quantities[i], productIDs[i]);
    print_stock(conn, productIDs[i]);
   }
  } catch (SQLException e) {
   e.printStackTrace();
   try {
    if (de_ps != null) de_ps.close();
   } catch (SQLException ex) {
    ex.printStackTrace();
    return;
   }
   return;
  } finally {
   try {
    if (de_ps != null) de_ps.close();
   } catch (SQLException ef) {
    ef.printStackTrace();
    return;
   }
  }
 }

 /**
  * @param conn An open database connection
  */
 public static void option4(Connection conn) {
  // complete - Code for option 4 goes here
  PreparedStatement tot_ps = null;
  try {
   //query will return the products and the total value sold of products in descending order.
   String totval_print = "select order_products.ProductID, ProductDesc, ProductPrice * SUM(ProductQuantity)" +
    " as t from order_products inner join inventory on inventory.ProductID=order_products.ProductID" +
    " group by order_products.ProductID, ProductDesc, ProductPrice order by t desc";
   System.out.println("set String");
   tot_ps = conn.prepareStatement(totval_print);
   System.out.println("Connected statement");
   ResultSet rset2;
   System.out.println("executing query");
   rset2 = tot_ps.executeQuery();
   System.out.println("executed");
   System.out.format("%-15s%-35s%-15s\n", "ProductID", "ProductDesc", "TotalValueSold");
   //for each of the results print them. used System.out.format() so that the result will come back in the correct way.
   while (rset2.next()) {
    System.out.format("%-15d,%-35s,\u00a3%-15.2f\n", rset2.getInt(1), rset2.getString(2), rset2.getFloat(3));
   }
  } catch (SQLException t) {
   t.printStackTrace();
   try {
    if (tot_ps != null) tot_ps.close();
   } catch (SQLException tx) {
    tx.printStackTrace();
    return;
   }
   return;
  } finally {
   try {
    if (tot_ps != null) tot_ps.close();
   } catch (SQLException tf) {
    tf.printStackTrace();
    return;
   }
  }
 }

 /**
  * @param conn An open database connection
  * @param date The target date to test collection deliveries against
  */
 public static void option5(Connection conn, String date) {
  // Incomplete - Code for option 5 goes here
  ResultSet rset2 = null;
  PreparedStatement quer_ps, inv_ps, delc_ps, delop_ps, delo_ps;
  quer_ps = inv_ps = delc_ps = delop_ps = delo_ps = null;
  try {
   ArrayList < Integer > orderIDs = new ArrayList < Integer > ();
   //will return all of the products where the date they were placed is 8 days before the entered date.
   String quer_dat = "select * from order_products where OrderID in" +
    " (select OrderID from orders where OrderPlaced < str_to_date(?, '%Y-%m-%d')- 8"+
    " and OrderCompleted = 0 and OrderType = 'Collection')";
   quer_ps = conn.prepareStatement(quer_dat);
   quer_ps.clearParameters();
   quer_ps.setString(1, date);
   rset2 = quer_ps.executeQuery();
   while (rset2.next()) {
    if (!orderIDs.contains(rset2.getInt(1))) {
     orderIDs.add(rset2.getInt(1));
    }
    //add the quantity back to the inventory.
    String inv_upd = "UPDATE inventory" + " SET ProductStockAmount = ProductStockAmount + ?" + " WHERE ProductID = ?";
    inv_ps = conn.prepareStatement(inv_upd);
    inv_ps.clearParameters();
    inv_ps.setInt(1, rset2.getInt(3));
    inv_ps.setInt(2, rset2.getInt(2));

    inv_ps.executeUpdate();
   }
   //delete the order from the relevent tables.
   for (int i = 0; i < orderIDs.size(); i++) {
    String delc_st = "delete from collections where OrderID = ?";
    delc_ps = conn.prepareStatement(delc_st);
    delc_ps.clearParameters();
    delc_ps.setInt(1, orderIDs.get(i));

    delc_ps.executeUpdate();

    String delop_st = "delete from order_products where OrderID = ?";
    delop_ps = conn.prepareStatement(delop_st);
    delop_ps.clearParameters();
    delop_ps.setInt(1, orderIDs.get(i));

    delop_ps.executeUpdate();

    String delo_st = "delete from orders where OrderID = ?";
    delo_ps = conn.prepareStatement(delo_st);
    delo_ps.clearParameters();
    delo_ps.setInt(1, orderIDs.get(i));

    delo_ps.executeUpdate();
   }
   //notify that the order has been cancelled.
   for (int i = 0; i < orderIDs.size(); i++) {
    System.out.println("Order " + orderIDs.get(i) + " has been cancelled");
   }
  } catch (SQLException re) {
   re.printStackTrace();
   try {
    if (quer_ps != null) quer_ps.close();
    if (rset2 != null) rset2.close();
    if (inv_ps != null) inv_ps.close();
    if (delc_ps != null) delc_ps.close();
    if (delop_ps != null) delop_ps.close();
    if (delo_ps != null) delo_ps.close();
   } catch (SQLException rex) {
    rex.printStackTrace();
    return;
   }
   return;
  } finally {
   try {
    if (quer_ps != null) quer_ps.close();
    if (rset2 != null) rset2.close();
    if (inv_ps != null) inv_ps.close();
    if (delc_ps != null) delc_ps.close();
    if (delop_ps != null) delop_ps.close();
    if (delo_ps != null) delo_ps.close();
   } catch (SQLException ref) {
    ref.printStackTrace();
    return;
   }

  }
 }

 /**
  * @param conn An open database connection
  */
 public static void option6(Connection conn) {
  // Incomplete - Code for option 6 goes here
  PreparedStatement stff_ps = null;
  ResultSet rset2 = null;
  try {
   //query to return all of the employyes that have sold over £50000 worth of products.
   String stff_sal = "select staff.StaffID, FName, LName, SUM(ProductQuantity*ProductPrice)" +
    " as TotalSold FROM staff JOIN staff_orders on Staff.StaffID = staff_orders.StaffID" +
    " JOIN order_products ON staff_orders.OrderID = order_products.OrderID" +
    " JOIN inventory ON inventory.ProductID = order_products.ProductID" +
    " group by staff.StaffID,FName,LName having SUM(ProductQuantity*ProductPrice) >= "+ STAFF_SALE_THRESHOLD+" order by TotalSold desc";
   stff_ps = conn.prepareStatement(stff_sal);
   stff_ps.clearParameters();
   rset2 = stff_ps.executeQuery();
   System.out.format("%-20s, %-10s\n", "Employee Name", "TotalValueSold");
   while (rset2.next()) {
    //print all of these employees and the corresponding amount they have sold.
    System.out.format("%-20s, \u00a3%-10d\n", rset2.getString(2) + " " + rset2.getString(3), rset2.getInt(4));
   }
  } catch (SQLException ri) {
   ri.printStackTrace();
   try {
    if (stff_ps != null) stff_ps.close();
    if (rset2 != null) rset2.close();
   } catch (SQLException rix) {
    rix.printStackTrace();
    return;
   }
   return;
  } finally {
   try {
    if (stff_ps != null) stff_ps.close();
    if (rset2 != null) rset2.close();
   } catch (SQLException rif) {
    rif.printStackTrace();
    return;
   }
  }
 }

 /**
  * @param conn An open database connection
  */
 public static void option7(Connection conn) {
  // Incomplete - Code for option 7 goes here
  PreparedStatement pop_ps, relv_ps, incl_ps, getp_ps, getr_ps, getr2_ps;
  pop_ps = relv_ps = incl_ps = getp_ps = getr_ps = getr2_ps = null;
  ResultSet rset1, rset2, rset3, rset4;
  rset1 = rset2 = rset3 = rset4 = null;
  ArrayList < Integer > pop_productIDs = new ArrayList < Integer > ();
  ArrayList < Integer > relv_staffIDs = new ArrayList < Integer > ();
  try {
   //query that creates a view of products that have sold ove £20000 in value.
   String pop_products = "create or replace view pop_products as" +
    " select order_products.ProductID as P_ID, SUM(ProductPrice * ProductQuantity) as TotalValue" +
    " from order_products" +
    " join inventory on inventory.ProductID = order_products.ProductID" +
    " group by order_products.ProductID having SUM(ProductPrice * ProductQuantity) > "+PRODUCT_SALE_THESHOLD+
    " order by P_ID desc";

   pop_ps = conn.prepareStatement(pop_products);
   pop_ps.clearParameters();
   pop_ps.executeUpdate();
   //get all of the product ids from these views and add it to pop_productIDs ArrayList.
   String get_pop = "Select * from pop_products order by P_ID asc";
   getp_ps = conn.prepareStatement(get_pop);
   getp_ps.clearParameters();
   rset1 = getp_ps.executeQuery();


   while (rset1.next()) {
    pop_productIDs.add(rset1.getInt(1));
   }
   //print all of the produts that have sold over £20000 in the way wanted.
   System.out.printf("%-19s", "EmployeeName, ");
   for (int x = 0; x < pop_productIDs.size(); x++) {
    System.out.printf("%-4d", pop_productIDs.get(x));
    if (x == pop_productIDs.size() - 1) System.out.print("\n");
   }
   //all of the staff that have sold products that are in the pop_tables view.
   String relv_staffID = "create or replace view relv_table as" +
    " select staff_orders.StaffID, FName, LName, P_ID, SUM(ProductQuantity) AS Quantity" +
    " from staff_orders" +
    " join order_products on order_products.OrderID = staff_orders.OrderID join" +
    " staff on staff.StaffID = staff_orders.StaffID" +
    " join pop_products on pop_products.P_ID = order_products.ProductID" +
    " where ProductID in" +
    " (select P_ID from pop_products)" +
    " group by staff_orders.StaffID, FName, LName, pop_products.P_ID" +
    " order by staff_orders.StaffID, P_ID";

   relv_ps = conn.prepareStatement(relv_staffID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
   relv_ps.clearParameters();
   relv_ps.executeUpdate();
   //get all of the distinct staff and add it to relv_staffIDs ArrayList.
   String get_relv = "Select distinct StaffID, FName, LName from relv_table";
   getr_ps = conn.prepareStatement(get_relv);
   getr_ps.clearParameters();
   rset2 = getr_ps.executeQuery();

   while (rset2.next()) {
    relv_staffIDs.add(rset2.getInt(1));
   }
   String get2_relv = "Select distinct StaffID, FName, LName from relv_table";
   getr2_ps = conn.prepareStatement(get2_relv);
   getr2_ps.clearParameters();
   rset4 = getr2_ps.executeQuery();

   while (rset4.next()) {
    //for all of the distinct staffIDs get the amount sold of each of the products in pop_products.
    //if the staff hasnt sold the product then print zero, else print the amount that they have sold.
    System.out.printf("%-19s", rset4.getString(2) + " " + rset4.getString(3) + "  ");
    for (int j = 0; j < pop_productIDs.size(); j++) {
     String incl = "Select * from relv_table where StaffID = ? and P_ID = ?";
     incl_ps = conn.prepareStatement(incl);
     incl_ps.clearParameters();
     incl_ps.setInt(1, rset4.getInt(1));
     incl_ps.setInt(2, pop_productIDs.get(j));

     rset3 = incl_ps.executeQuery();
     boolean empty = true;
     while (rset3.next()) {
      System.out.printf("%-4d", rset3.getInt(5));
      empty = false;
     }
     if (empty) System.out.printf("%-4s", "0");
     rset3.close();
    }
    System.out.print("\n");
   }
  } catch (SQLException e) {
   e.printStackTrace();
   try {
    if (pop_ps != null) pop_ps.close();
    if (relv_ps != null) relv_ps.close();
    if (incl_ps != null) incl_ps.close();
    if (getp_ps != null) getp_ps.close();
    if (getr_ps != null) getr_ps.close();
    if (getr2_ps != null) getr2_ps.close();
    if (rset1 != null) rset1.close();
    if (rset2 != null) rset2.close();
    if (rset3 != null) rset3.close();
    if (rset4 != null) rset4.close();
   } catch (SQLException ex) {
    ex.printStackTrace();
    return;
   }
   return;
  } finally {
   try {
    if (pop_ps != null) pop_ps.close();
    if (relv_ps != null) relv_ps.close();
    if (incl_ps != null) incl_ps.close();
    if (getp_ps != null) getp_ps.close();
    if (getr_ps != null) getr_ps.close();
    if (getr2_ps != null) getr2_ps.close();
    if (rset1 != null) rset1.close();
    if (rset2 != null) rset2.close();
    if (rset3 != null) rset3.close();
    if (rset4 != null) rset4.close();
   } catch (SQLException ef) {
    ef.printStackTrace();
    return;
   }
  }
 }
 /**
  * @param conn An open database connection
  * @param year The target year we match employee and product sales against
  */
 public static void option8(Connection conn, int year) {
  // Incomplete - Code for option 8 goes here
  PreparedStatement pop_ps, corres_ps, stff_ps, fin_ps;
  pop_ps = corres_ps = stff_ps = fin_ps = null;
  ResultSet rset1 = null;
  //knowing the orders will have to be greater than the start of the enterd date and less than the end of the entered
  //date so create String variables for these.
  String sDate = year + "-01-01";
  String eDate = year + "-12-31";

  try {
   //create a view holding all of the products that have sold a value greater than 20000.
   String pop_products = "create or replace view pop_products as" +
    " select order_products.ProductID as P_ID, SUM(ProductPrice * ProductQuantity) as TotalValue" +
    " from order_products" +
    " join inventory on inventory.ProductID = order_products.ProductID" +
    " group by order_products.ProductID having SUM(ProductPrice * ProductQuantity) > "+PRODUCT_SALE_THESHOLD+
    " order by totalvalue desc";
   pop_ps = conn.prepareStatement(pop_products);
   pop_ps.clearParameters();
   pop_ps.executeUpdate();
   //create a view of all of the orders in that year span that also have products in pop_products view.
   String corres_table = "create or replace view corres_table as" +
    " select order_products.OrderID, OrderPlaced, SUM(ProductQuantity * ProductPrice) AS OrderValue" +
    " from order_products" +
    " join inventory on inventory.ProductID = order_products.ProductID" +
    " join orders on order_products.OrderID = orders.OrderID" +
    " where OrderPlaced > str_to_date('" + sDate + "', '%Y-%m-%d') and OrderPlaced < str_to_date('" + eDate + "', '%Y-%m-%d')" +
    " and inventory.ProductID in (Select P_ID from pop_products)" +
    " group by order_products.OrderID, OrderPlaced" +
    " order by OrderValue desc";
   corres_ps = conn.prepareStatement(corres_table);
   corres_ps.executeUpdate();
   //get all the staff that have sold over £30000 worth of products.
   String stff_tbl = "create or replace view staff_tbl as" +
    " select staff.StaffID, FName, LName, SUM(ProductQuantity*ProductPrice) as TotalSold FROM staff JOIN staff_orders on Staff.StaffID = staff_orders.StaffID" +
    " JOIN order_products ON staff_orders.OrderID = order_products.OrderID" +
    " JOIN inventory ON inventory.ProductID = order_products.ProductID" +
    " group by staff.StaffID,FName,LName having SUM(ProductQuantity*ProductPrice) >= "+STAFF_SALE_THRESHOLD+" order by TotalSold desc";
   stff_ps = conn.prepareStatement(stff_tbl);
   stff_ps.clearParameters();
   stff_ps.executeUpdate();
   //get all of the staff that have orders in the view of staff and have orders in the corres_table view.
   String fin_quer = "select FName, LName" +
    " from staff" +
    " join staff_orders on staff.StaffID = staff_orders.StaffID" +
    " where OrderID in (select OrderID from Corres_table) and staff.StaffID in (select StaffID from staff_tbl)" +
    " group by FName,LName";
   fin_ps = conn.prepareStatement(fin_quer);
   fin_ps.clearParameters();
   rset1 = fin_ps.executeQuery();
   //print all these staff.
   while (rset1.next()) {
    System.out.println(rset1.getString(1) + " " + rset1.getString(2));
   }
  } catch (SQLException e) {
   System.out.println("SQL Exception found at option 8");
   e.printStackTrace();
   try {
    if (pop_ps != null) pop_ps.close();
    if (corres_ps != null) corres_ps.close();
    if (stff_ps != null) stff_ps.close();
    if (fin_ps != null) fin_ps.close();
    if (rset1 != null) rset1.close();
   } catch (SQLException ex) {
    ex.printStackTrace();
    return;
   }
   return;
  } finally {
   try {
    if (pop_ps != null) pop_ps.close();
    if (corres_ps != null) corres_ps.close();
    if (stff_ps != null) stff_ps.close();
    if (fin_ps != null) fin_ps.close();
    if (rset1 != null) rset1.close();
    return;
   } catch (SQLException ef) {
    ef.printStackTrace();
    return;
   }
  }
 }

 public static Connection getConnection() {
  String user;
  String passwrd;
  Connection conn;

  try {
   Class.forName("com.mysql.cj.jdbc.Driver");
  } catch (ClassNotFoundException x) {
   System.out.println("Driver could not be loaded");
  }

  user = readEntry("Enter database account: ");
  passwrd = readEntry("Enter a password: ");
  try {
   conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/store?serverTimezone=GMT", user, passwrd);
   return conn;
  } catch (SQLException e) {
    e.printStackTrace();
   System.out.println("Error retrieving connection");
   return null;
  }
 }

 public static void main(String args[]) throws SQLException, IOException {
  // You should only need to fetch the connection details once
  Connection conn = getConnection();
  String pattern = "YYYY-MM-DD";
  SimpleDateFormat formatter = new SimpleDateFormat(pattern);
  String pattern2 = "YYYY";
  SimpleDateFormat formatter2 = new SimpleDateFormat(pattern2);
  // Incomplete
  // Code to present a looping menu, read in input data and call the appropriate option menu goes here
  // You may use readEntry to retrieve input data
  // i decided to use a switch statement on the option picked from the rolling menu.
  String prompt = "MENU: \n\n (1)In-Store Purchases\n (2)Collection\n (3)Delivery\n (4)Biggest Sellers\n (5)Reserved Stock\n (6)Staff Life-Time Success\n (7)Staff Contribution\n (8)Employee of the Year \n (0)Quit \n\nEnter Your choice: ";
  while (true) {
   String option = readEntry(prompt);
   if (option.equals("0")) break;
   outerloop:
    switch (option) {
     case "1":
     case "2":
     case "3":
      char cont = 'y';
      ArrayList < Integer > productIDs = new ArrayList < Integer > ();
      ArrayList < Integer > quantities = new ArrayList < Integer > ();
      String date, cDate, dDate, fName, lName, hno, street, city;
      date = cDate = dDate = fName = lName = hno = street = city = "";
      int staffID, productID, quantS;
      staffID = productID = quantS = 0;
      String answer;
      int productcount = 0;
      while (cont == 'y' || cont == 'Y') {
       String productIDp = readEntry("Enter a ProductID: ");
       try {
        productID = Integer.parseInt(productIDp);
       } catch (NumberFormatException pe) {
        pe.printStackTrace();
        break outerloop;
       }
       productIDs.add(productID);
       String quantSp = readEntry("Enter the quantity sold: ");
       try {
        quantS = Integer.parseInt(quantSp);
       } catch (NumberFormatException pq) {
        pq.printStackTrace();
        break outerloop;
       }
       quantities.add(quantS);
       answer = readEntry("Is there another product in the order? (y/n): ");
       if (answer.equals("N") || answer.equals("n")) cont = 'N';
       productcount++;
      };
      int productIDs_A[] = new int[productcount];
      int quantities_A[] = new int[productcount];
      for (int i = 0; i < productcount; i++) {
       productIDs_A[i] = productIDs.get(i);
       quantities_A[i] = quantities.get(i);
      }
      date = readEntry("Enter the date sold: ");
      try {
       Date dateT = formatter.parse(date);
      } catch (ParseException e) {
       e.printStackTrace();
       break outerloop;
      }
      if (option.equals("1")) {
       String staffIDp = readEntry("Enter your Staff ID: ");
       try {
        staffID = Integer.parseInt(staffIDp);
       } catch (NumberFormatException ps) {
        ps.printStackTrace();
        break outerloop;
       }
       option1(conn, productIDs_A, quantities_A, date, staffID);
      } else if (option.equals("2")) {
       cDate = readEntry("Enter the date of Collection: ");
       try {
        Date dateT = formatter.parse(cDate);
       } catch (ParseException e) {
        e.printStackTrace();
        break;
       }
       fName = readEntry("Enter the first name of the collector: ");
       lName = readEntry("Enter the last name of the collector: ");
       String staffIDp = readEntry("Enter your Staff ID: ");
       try {
        staffID = Integer.parseInt(staffIDp);
       } catch (NumberFormatException ps) {
        ps.printStackTrace();
        break outerloop;
       }
       option2(conn, productIDs_A, quantities_A, date, cDate, fName, lName, staffID);
      } else {
       dDate = readEntry("Enter the date of delivery: ");
       try {
        Date dateT = formatter.parse(dDate);
       } catch (ParseException e) {
        e.printStackTrace();
        break;
       }
       fName = readEntry("Enter the first name of the recipient: ");
       lName = readEntry("Enter the last name of the recipient: ");
       hno = readEntry("Enter the house name/no: ");
       street = readEntry("Enter the street: ");
       city = readEntry("Enter the City: ");
       String staffIDp = readEntry("Enter your Staff ID: ");
       try {
        staffID = Integer.parseInt(staffIDp);
       } catch (NumberFormatException ps) {
        ps.printStackTrace();
        break outerloop;
       }
       option3(conn, productIDs_A, quantities_A, date, dDate, fName, lName, hno, street, city, staffID);
      }
      break;
     case "4":
      option4(conn);
      break;
     case "5":
      String fdate = readEntry("Enter the date: ");
      try {
       Date dateT = formatter.parse(fdate);
      } catch (ParseException e) {
       e.printStackTrace();
       break;
      }
      option5(conn, fdate);
      break;
     case "6":
      option6(conn);
      break;
     case "7":
      option7(conn);
      break;
     case "8":
      String yearp = readEntry("Enter the year: ");
      try {
       Date dateT = formatter2.parse(yearp);
      } catch (ParseException e) {
       e.printStackTrace();
       break;
      }
      int year = Integer.parseInt(yearp);
      option8(conn, year);
      break;
     default:
      System.out.println("Invalid option selected.");
      break;
    }
  }
  System.out.println("exiting");
  conn.close();
 }
}
//done
