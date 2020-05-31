## Steps.

 1. Add your MySQL connector to your classpath enviromental variables.
 2. Open your MySQL workbench and connect to the database.
 3. Create a new schema called store.
 4. Make your new schema your default database.
 5. Open create_tables.sql and run (this will create your tables needed.)
 6. Populate the inventory table by importing your inventory data.
 7. Populate your staff table by importing your staff data.
 8. In Assignment.java the values for *PRODUCT_SALE_THRESHOLD* and *STAFF_SALE_THRESHOLD* correspond to the sales value that makes a popular product and  top selling staff respectively.
 9. Compile Assignment.java `javac Assignment.java` in the project folder.
 10. Run the project with `java Assignment`

**Look below for more information on the functionality of the application.**

## Options .

 1. Enter a in-store order.
 2. Enter a collection order.
 3. Enter a delivery order.
 4. The biggest product sellers of all time according to the £[*PRODUCT SALE THRESHOLD*].
 5. While the store reserves items for in-store collection, it places a limit on how long it will wait for a customer. If the order is uncompleted (i.e. not picked up) and is a collection order, then for a given date the program will, identify all orders that have a collection date 8 days or older than the provided date, re-add the items from the order to the product stock amount and delete the order and any data pertaining to that order.
 6. Obtain a list of stAFF who have sold at least £[*STAFF SALE THRESHOLD*] of items during the store's lifetime and print their names alongside the amount.
 7. Figuring out which staff has sold the most of beggest product sellers according to the *PRODUCT_SALES_THRESHOLD*.
 8. The criteria of teh employee of the year is staff that have sold more than the *STAFF_SALES_THRESHOLD* and also sold at least one of the products that qualify for more than the *PRODUCT_SALES_THRESHOLD*.


