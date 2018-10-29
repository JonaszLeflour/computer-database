package com.excilys.cdb;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.service.DataPresenter;
import com.excilys.cdb.service.SQLDataPresenter;
import com.excilys.cdb.ui.ConsoleUserInterface;
/**
 * Program main entry point
 * @author Jonasz Leflour
 * @version %I%
 *
 */
public class Main {

	/**
	 * Creates user interface object
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		DataPresenter dp = null;
		try {
			dp = new SQLDataPresenter();
			new ConsoleUserInterface(dp).mainLoop(args);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DatabaseErrorException e) {
			e.printStackTrace();
		}
	}
}
