/**
 *  
 * @author Jonasz Leflour
 * @version %I%
 */
package ui;

import java.util.List;
import java.util.Map;
import java.util.EnumMap;
import java.util.Scanner;

import service.DataPresenter;
import model.Company;
import model.Computer;
import persistence.DatabaseAccessor;

public class ConsoleUserInterface implements UserInterface {
	Scanner scan = new Scanner(System.in);
	DataPresenter dp = new DataPresenter();

	public ConsoleUserInterface() {

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
			List<Computer> foundComputers = dp.getComputersByName(name);
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
		Map<DatabaseAccessor.ComputerField, String> params = new EnumMap<DatabaseAccessor.ComputerField, String>(
				DatabaseAccessor.ComputerField.class);
		String val;
		System.out.println("Add computer");
		for (DatabaseAccessor.ComputerField field : DatabaseAccessor.ComputerField.values()) {
			System.out.println("Enter " + field + " : ");
			val = scan.nextLine();
			if (!val.isEmpty()) {
				params.put(field, val);
			}
		}
		dp.addComputer(params);
	}

	private void updateComputer() {
		System.out.println("Update computer infos");
		Map<DatabaseAccessor.ComputerField, String> params = new EnumMap<DatabaseAccessor.ComputerField, String>(
				DatabaseAccessor.ComputerField.class);
		String val;
		System.out.println("Add computer");
		System.out.println("Enter id (mandatory) : ");
		int id = scan.nextInt();
		scan.nextLine();

		for (DatabaseAccessor.ComputerField field : DatabaseAccessor.ComputerField.values()) {
			System.out.println("Enter " + field + " (or nothing to leave as is) : ");
			val = scan.nextLine();
			if (!val.isEmpty()) {
				params.put(field, val);
			}
		}
		dp.updateComputerById(id, params);
	}

	private void deleteComputer() {
		System.out.println("Delete computer");
		System.out.println("Enter id");
		int id = scan.nextInt();
		scan.nextLine();
		dp.removeComputeById(id);
	}

}
