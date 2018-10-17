package ui;

import java.util.Scanner;
import java.io.*;

public class ConsoleUserInterface implements UserInterface {
	Scanner scan = new Scanner(System.in);
	public ConsoleUserInterface(){
		
	}

	@Override
	public void mainLoop(String[] args) {
		boolean looping = true;
		
		while(looping) {
			System.out.println("Select option :");
			System.out.println("1 - List computers");
			System.out.println("2 - List companies");
			System.out.println("3 - Show computer");
			System.out.println("4 - Update computer infos");
			System.out.println("5 - Delete computer");
			System.out.println("0 - Exit");
			
			int choice = scan.nextInt();
			switch(choice) {
				case 1: listComputers(); break;
				case 2: listCompanies();break;
				case 3: showComputer();break;
				case 4: updateComputer();break;
				case 5: deleteComputer(); break;
				case 0: looping = false;break;
				default : System.out.println("Invalid option");break;
			}
			
		}
		System.out.println("Bye");
	}

	private void listComputers() {
		System.out.println("List computers");
	}
		
	private void listCompanies() {
		System.out.println("List companies");
	}
	
	private void showComputer() {
		System.out.println("Show computer");
	}
	
	private void updateComputer() {
		System.out.println("Update computer infos");
	}
	
	private void deleteComputer() {
		System.out.println("5 - Delete computer");
	}
	
}
