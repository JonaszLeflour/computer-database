package com.excilys.cdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.config.RootConfig;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.ui.ConsoleUserInterface;
/**
 * Program main entry point
 * @author Jonasz Leflour
 * @version %I%
 *
 */
public class Main {
	static Logger logger = LoggerFactory.getLogger(Main.class);
	//static ApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
	/**
	 * Creates user interface object
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		try {
			new ConsoleUserInterface().mainLoop(args);
		} catch (DatabaseErrorException e) {
			e.printStackTrace();
		}
	}
}
