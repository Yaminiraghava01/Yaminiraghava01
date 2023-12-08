import java.io.IOException;
import java.sql.*;

public class project {
	
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Letsdoit@123");
		
		connection.setAutoCommit(false); // ensures atomicity property
		connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); //ensures isolation property
		
		Statement stmt = null;
		
		try {
			stmt = connection.createStatement();
			
			// creating the relations product, depot and stock and defining the keys
			
			stmt.execute("Create table product(prod varchar(10),pname varchar(10),price int, primary key(prod))");
			stmt.execute("Create table depot(dep varchar(10),addr varchar(10),voume int, primary key(dep))");
			stmt.execute("Create table stock(prod varchar(10),dep varchar(10),quantity int, primary key(prod,dep) )");
			stmt.execute("alter table stock add constraint depot_fk foreign key(dep) references depot(dep) on update cascade on delete cascade");
			stmt.execute("alter table stock add constraint producy_fk foreign key(prod) references product(prod) on update cascade on delete cascade");

			
			//inserting values into the relations
			
			stmt.execute("Insert into product values('p1','tape',3)");
			stmt.execute("Insert into product values('p2','tv',250)");
			stmt.execute("Insert into product values('p3','vcr',80)");

			
			stmt.execute("Insert into depot values('d1','New york',9000)");
			stmt.execute("Insert into depot values('d2','Syracuse',6000)");
			stmt.execute("Insert into depot values('d4','New york',2000)");

			stmt.execute("Insert into stock values('p1','d1',1000)");
			stmt.execute("Insert into stock values('p1','d2',-100)");
			stmt.execute("Insert into stock values('p1','d4',1200)");
			stmt.execute("Insert into stock values('p3','d1',3000)");
			stmt.execute("Insert into stock values('p3','d4',2000)");
			stmt.execute("Insert into stock values('p2','d4',1500)");
			stmt.execute("Insert into stock values('p2','d1',-400)");
			stmt.execute("Insert into stock values('p2','d2',2000)");
			
			
			
			// The following statements execute queries
		
			stmt.executeUpdate("Delete from product where prod ='p1'");  
			//The product p1 is deleted from Product and Stock. Since, while defining the foreign key we used on delete cascade the entry in stock with p1 will be deleted in stock as well.
			
			stmt.executeUpdate("Delete from depot where dep = 'd1'"); 
			//The depot d1 is deleted from Depot and Stock.By using on delete cascade we can delete the entries of d1 with stock just by deleting d1 in depot relation.
			
			stmt.executeUpdate("Update product set prod = 'pp1' where prod ='p1'");
			//The product p1 changes its name to pp1 in Product and Stock
			
			stmt.executeUpdate("Update depot set dep = 'dd1' where dep ='d1'");
			//The depot d1 changes its name to dd1 in Depot and Stock.

			stmt.executeUpdate("insert into product values('p100','cd',5)");
			//We add a product (p100, cd, 5) in Product and (p100,d2, 50) in Stock.
			
			stmt.executeUpdate("insert into depot values('d100','Chicago',100)");
			//We add a depot (d100, Chicago, 100) in Depot and (p1, d100, 100) in Stock.
			
			stmt.executeUpdate("insert into stock values('p100','d2',50)");
			//We add a product (p100, cd, 5) in Product and (p100, d2, 50) in Stock.
		
			stmt.executeUpdate("insert into stock values('p1','d100',100)");
			//We add a depot (d100, Chicago, 100) in Depot and (p1, d100, 100) in Stock.
			
			
		} catch(SQLException e) {
			System.out.println("An exception is thrown");
			
			e.printStackTrace();
			connection.rollback();/* makes sure that the database is "consistent" by rolling back the changes made if there is any errors.*/
			stmt.close();
			connection.close();
			return;
		}
		connection.commit(); // commits the transactions
		stmt.close();
		connection.close();
		System.out.println("Successfully performed the transactions!");
	}
}
