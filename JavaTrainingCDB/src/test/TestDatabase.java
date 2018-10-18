package test;

import service.SQLDataPresenter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

import model.Company;
import model.Computer;
import model.ComputerField;

/**
 * Main class for testing non-ui components
 * @author Jonasz Leflour
 * @version %I%
 * 
 */
public class TestDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SQLDataPresenter dp = new SQLDataPresenter();
		System.out.println(dp.getComputers());
		System.out.println(dp.getCompanies());
		System.out.println(dp.getComputerById(1));
		//System.out.println(dp.getComputerByName("Power Macintosh 7300"));
		Map<ComputerField,String> fields 
			=new EnumMap<ComputerField,String>(ComputerField.class);
		
		System.out.println(Date.valueOf(LocalDate.now()).toString());
		fields.put(ComputerField.introduced, Date.valueOf(LocalDate.now()).toString());
		
		dp.updateComputerById(1,fields);
		System.out.println(dp.getComputerById(1));
		
		System.out.println("All Lolvisions : ");
		System.out.println(dp.getComputersByName("Lolvision"));
		
		
		dp.addComputer(new Computer(0,"Lolvision", LocalDate.of(1990, 1, 16),LocalDate.now(),new Company(2,"lol")));
		System.out.println(dp.getComputersByName("Lolvision"));
		dp.removeComputersByName("Lolvision");
		System.out.println(dp.getComputersByName("Lolvision"));
		
		
	}

}
