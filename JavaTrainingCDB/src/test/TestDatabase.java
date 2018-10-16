package test;

import service.DataPresenter;

public class TestDatabase {

	public static void main(String[] args) {
		DataPresenter dp = new DataPresenter();
		
		System.out.println(dp.getComputers());
		System.out.println(dp.getCompanies());
		//System.out.println(dp.getComputerById(1));
		//System.out.println(dp.getComputerByName("Power Macintosh 7300"));
		//System.out.println(dp.updateComputerById(1,new Map<String, String>().put("discontinued", Date.valueOf(LocalDate.now()).toString()));
		
		
		
	}

}
