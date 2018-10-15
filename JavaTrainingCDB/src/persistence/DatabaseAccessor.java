package persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DatabaseAccessor {
	public void printAllComputers() {
        // Create a variable for the connection string.
        String URL = "jdbc:mysql://localhost:3306/computer-database-db"
        		+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "admincdb";
        String password = "qwerty1234";
        
        
        try (Connection con = DriverManager.getConnection(URL, user, password)){
        		
        	Statement stmt = con.createStatement();
            String SQL = "SELECT * FROM computer";
            ResultSet rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
}
