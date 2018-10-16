package persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import model.Computer;





public class DatabaseAccessor {
	private String URL = "jdbc:mysql://localhost:3306/computer-database-db"
    		+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET";
    private String user = "admincdb";
    private String password = "qwerty1234";
	
    private Connection con = null;
    
	
	public DatabaseAccessor(){
		try {
			reconnect(user,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public void reconnect(String user, String password) throws SQLException {
		if(con != null) {
			con.close();
		}
		con = DriverManager.getConnection(URL, user, password);
		this.user = user;
		this.password = password; 
	}
	
	public ResultSet executeRequest(String SQL) {
		try{
        	Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            return rs;
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
		
	}
	
	public ResultSet getAllComputers(){
        return executeRequest("SELECT * FROM computer");
    }
	
	public ResultSet getAllCompanies(){
        return executeRequest("SELECT * FROM company");
    }
	
	public ResultSet getCompanybyId(int id){
        return executeRequest("SELECT * FROM company WHERE id="+id);
    }
	
	public ResultSet getComputerById(int id) {
		return executeRequest("SELECT * FROM computer WHERE id="+id);
	}
	
	public ResultSet getComputerByName(String name) {
		return executeRequest("SELECT * FROM computer WHERE name='"+name+"'");
	}
	
	public void createComputer(String name) {
		executeRequest("INSERT INTO computer (name) VALUES ('"+name+"')");
	}
	
	public void deleteComputerByName(String name) {
		executeRequest("DELETE FROM computer WHERE name = '"+name+"'");
	}
	
	public void updateComputerName(int id, String name) {
		executeRequest("UPDATE computer SET name ='"+name+"' WHERE id = "+id+"");
	}
	public void updateComputerIntroduced(int id, LocalDateTime introduced) {
		executeRequest("UPDATE computer SET introduced ='"+introduced+"' WHERE id = "+id+"");
	}
	public void updateComputerDiscontinued(int id, LocalDateTime discontinued) {
		executeRequest("UPDATE computer SET discontinued ='"+discontinued+"' WHERE id = "+id+"");
	}
	public void updateComputerCompany(int id, int company_id) {
		executeRequest("UPDATE computer SET company_id ='"+company_id+"' WHERE id = "+id+"");
	}
	
}
