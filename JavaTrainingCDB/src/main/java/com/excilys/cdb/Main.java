package com.excilys.cdb;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.DAOConfig;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.service.SimpleCompanyService;
import com.excilys.cdb.service.SimpleComputerService;
import com.excilys.cdb.ui.ConsoleUserInterface;
/**
 * Program main entry point
 * @author Jonasz Leflour
 * @version %I%
 *
 */
public class Main {
	static Logger logger = LoggerFactory.getLogger(Main.class);
	ApplicationContext ctx = new AnnotationConfigApplicationContext(DAOConfig.class);
	/**
	 * Creates user interface object
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		try {
			new ConsoleUserInterface(new SimpleComputerService(), new SimpleCompanyService()).mainLoop(args);
		} catch (IOException | DatabaseErrorException e) {
			logger.error("Error : ", e);
		}
	}
}
