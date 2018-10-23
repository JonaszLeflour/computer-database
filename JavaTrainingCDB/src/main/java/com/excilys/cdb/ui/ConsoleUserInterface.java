package com.excilys.cdb.ui;

import java.util.List;
import java.time.LocalDate;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.DatabaseErrorException;
import com.excilys.cdb.persistence.InvalidParameterException;
import com.excilys.cdb.persistence.ObjectNotFoundException;
import com.excilys.cdb.service.DataPresenter;

/**
 * @author Jonasz Leflour
 * @version %I%
 */
public final class ConsoleUserInterface implements UserInterface {
	Scanner scan = new Scanner(System.in);
	DataPresenter dp;
	
	/**
	 * implementation of DataPresenter provided at creation
	 * @param dp
	 */
	public ConsoleUserInterface(DataPresenter dp) {
		this.dp = dp;
	}

	@Override
	public void mainLoop(String[] args) {
		boolean looping = true;

		while (looping) {
			System.out.println("Select option :");
			System.out.println("1 - List computers");
			System.out.println("2 - List companies");
			System.out.println("3 - Show computer (name)");
			System.out.println("4 - Update computer infos");
			System.out.println("5 - Add computer");
			System.out.println("6 - Delete computer (id)");
			System.out.println("0 - Exit");

			String choice = scan.nextLine();
			switch (choice) {
			case "1":
				listComputers();
				break;
			case "2":
				listCompanies();
				break;
			case "3":
				showComputer();
				break;
			case "4":
				updateComputer();
				break;
			case "5":
				addComputer();
				break;
			case "6":
				deleteComputer();
				break;
			case "0":
				looping = false;
				break;
			default:
				System.out.println("Invalid option");
				break;
			}
		}
		System.out.println("Bye");
	}

	private void listComputers() {
		List<Computer> computers = dp.getComputers();
		int pageSize = 10;
		int currentPage = 0;
		int nbPages = (int) Math.ceil(computers.size() / (double) pageSize);
		while (true) {
			int start = currentPage * pageSize;
			int end = Math.min((currentPage + 1) * pageSize, computers.size());
			List<Computer> page = computers.subList(start, end);
			for (Computer computer : page) {
				System.out.println(computer.getId() + " - " + computer.getName());
			}
			System.out.println("Page " + (currentPage + 1) + " / " + nbPages);

			// 4 cases : full list displayed, start of list displayed, end of list
			// displayed, middle list displayed
			// full display
			boolean validChoice = false;
			if (nbPages == 1) {
				System.out.println("e to exit");
				while (!validChoice) {
					String choice = scan.nextLine();
					if (!choice.equals("e")) {
						System.out.println("Invalid choice");
					} else {
						validChoice = true;
						return;
					}
				}

			} else {
				// middle list
				if (currentPage != 0 && currentPage < (nbPages - 1)) {
					System.out.println("n for next page - p for previous page - e to exit");
					while (!validChoice) {
						String choice = scan.nextLine();
						switch (choice) {
						case "n":
							currentPage++;
							validChoice = true;
							break;
						case "p":
							currentPage--;
							validChoice = true;
							break;
						case "e":
							return;
						default:
							System.out.println("Invalid choice");
							break;

						}
					}
				}
				// start list
				else if (currentPage == 0) {
					System.out.println("n for next page - e to exit");
					while (!validChoice) {
						String choice = scan.nextLine();
						switch (choice) {
						case "n":
							currentPage++;
							validChoice = true;
							break;
						case "e":
							return;
						default:
							System.out.println("Invalid choice");
							break;

						}
					}
				}
				// end list
				else if (currentPage == nbPages - 1) {
					System.out.println("p for previous page - e to exit");
					while (!validChoice) {
						String choice = scan.nextLine();
						switch (choice) {
						case "p":
							currentPage--;
							validChoice = true;
							break;
						case "e":
							return;
						default:
							System.out.println("Invalid choice");
							break;

						}
					}
				}

			}
		}
	}

	private void listCompanies() {
		List<Company> companies = dp.getCompanies();
		int pageSize = 10;
		int currentPage = 0;
		int nbPages = (int) Math.ceil(companies.size() / (double) pageSize);
		while (true) {
			int start = currentPage * pageSize;
			int end = Math.min((currentPage + 1) * pageSize, companies.size());
			List<Company> page = companies.subList(start, end);
			for (Company computer : page) {
				System.out.println(computer.getId() + " - " + computer.getName());
			}
			System.out.println("Page " + (currentPage + 1) + " / " + nbPages);

			// 4 cases : full list displayed, start of list displayed, end of list
			// displayed, middle list displayed
			// full display
			boolean validChoice = false;
			if (nbPages == 1) {
				System.out.println("e to exit");
				while (!validChoice) {
					String choice = scan.nextLine();
					if (!choice.equals("e")) {
						System.out.println("Invalid choice");
					} else {
						validChoice = true;
						return;
					}
				}
			} else {
				// middle list
				if (currentPage != 0 && currentPage < (nbPages - 1)) {
					System.out.println("n for next page - p for previous page - e to exit");
					while (!validChoice) {
						String choice = scan.nextLine();
						switch (choice) {
						case "n":
							currentPage++;
							validChoice = true;
							break;
						case "p":
							currentPage--;
							validChoice = true;
							break;
						case "e":
							return;
						default:
							System.out.println("Invalid choice");
							break;

						}
					}
				}
				// start list
				else if (currentPage == 0) {
					System.out.println("n for next page - e to exit");
					while (!validChoice) {
						String choice = scan.nextLine();
						switch (choice) {
						case "n":
							currentPage++;
							validChoice = true;
							break;
						case "e":
							return;
						default:
							System.out.println("Invalid choice");
							break;

						}
					}
				}
				// end list
				else if (currentPage == nbPages - 1) {
					System.out.println("p for previous page - e to exit");
					while (!validChoice) {
						String choice = scan.nextLine();
						switch (choice) {
						case "p":
							currentPage--;
							validChoice = true;
							break;
						case "e":
							return;
						default:
							System.out.println("Invalid choice");
							break;

						}
					}
				}

			}
		}
	}

	private void showComputer() {
		System.out.println("Show computer");
		while (true) {
			System.out.println("Enter computer name (or nothing to exit) :");
			String name = new String();
			name = scan.nextLine();
			if (name.isEmpty()) {
				return;
			}
			List<Computer> foundComputers = null;
			try {
				foundComputers = dp.getComputersByName(name);
			} catch (DatabaseErrorException e) {
				System.out.println("Error in database, couldn't query for "+name);
				return;
			}
			if (foundComputers.isEmpty()) {
				System.out.println(name + " not found");
			} else {
				for (Computer computer : foundComputers) {
					System.out.println(computer.toString());
				}
			}
		}
	}

	private void addComputer() {
		Computer.Builder computerBuilder = new Computer.Builder();
		
		String val;
		System.out.println("Add computer");
		
		System.out.println("Enter name : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.name(val);
		}
		
		System.out.println("Enter introduction date (yyyy-mm-dd format) : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.introduced(LocalDate.parse(val));
		}
		
		System.out.println("Enter discontinuation date (yyyy-mm-dd format) : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.discontinued((LocalDate.parse(val)));
		}
		
		System.out.println("Enter company id : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.company(new Company(Integer.parseInt(val),""));
		}
		
		Computer computer = new Computer(computerBuilder);
		try {
			dp.addComputer(computer);
		} catch (DatabaseErrorException e) {
			System.out.println("Error in database, couldn't add "+computer.toString());
		} catch (InvalidParameterException e) {
			System.out.println("Invalid parameters, couldn't add "+computer.toString());
		}
	}

	private void updateComputer() {
		Computer.Builder computerBuilder = new Computer.Builder();	
		String val;
		System.out.println("Upate computer");
	
		System.out.println("Enter introduction date (blank for no update) : ");
		System.out.println("Enter Company id (blank for no update) : ");
		
		System.out.println("Enter id (mandatory) : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.id(Integer.parseInt(val));
		}
		
		System.out.println("Enter name (blank for no update) : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.name(val);
		}
		
		System.out.println("Enter introduction date (yyyy-mm-dd format) (blank for no update) : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.introduced(LocalDate.parse(val));
		}
		
		System.out.println("Enter discontinuation date (yyyy-mm-dd format) (blank for no update) : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.discontinued((LocalDate.parse(val)));
		}
		
		System.out.println("Enter company id (blank for no update) : ");
		val = scan.nextLine();
		if (!val.isEmpty()) {
			computerBuilder.company(new Company(Integer.parseInt(val),""));
		}
		
		Computer computer = new Computer(computerBuilder);
		try {
			dp.updateComputer(computer);
		} catch (InvalidParameterException e) {
			System.out.println("Invalid parameters, couldn't update computer with id="+computer.getId()+" with new values");
		} catch (DatabaseErrorException e) {
			System.out.println("Error in database, couldn't update");
		}
	}

	private void deleteComputer() {
		System.out.println("Delete computer");
		System.out.println("Enter id");
		int id = scan.nextInt();
		scan.nextLine();
		try {
			dp.removeComputerById(id);
		} catch (DatabaseErrorException e) {
			System.out.println("Error in database, couldn't update");
		} catch (ObjectNotFoundException e) {
			System.out.println("Error : couldn't find computer with id="+id);
		}
	}

}
