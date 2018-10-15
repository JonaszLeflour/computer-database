package test;

import persistence.DatabaseAccessor;

public class TestDatabase {

	public static void main(String[] args) {
		DatabaseAccessor dbConnector = new DatabaseAccessor();
		dbConnector.printAllComputers();
		
	}

}
