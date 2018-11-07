package com.excilys.cdb;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	static Logger logger = LoggerFactory.getLogger(Main.class);
	
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
		} catch (IOException | DatabaseErrorException e) {
			logger.error("Error : ", e);
		}
	}
}
