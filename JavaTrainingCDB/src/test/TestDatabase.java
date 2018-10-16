package test;

import persistence.DatabaseAccessor;
import service.DataPresenter;

public class TestDatabase {

	public static void main(String[] args) {
		DataPresenter dp = new DataPresenter();
		
		System.out.println(dp.getComputerList());
		
		
		
	}

}
