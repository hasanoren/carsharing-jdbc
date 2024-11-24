package carsharing.Company;

import carsharing.Car.CarMenu;
import carsharing.Database;
import carsharing.Menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CompanyMenu {
    static Scanner scanner = new Scanner(System.in);
    static CompanyDaoImpl companyDao = new CompanyDaoImpl(Database.conn);

    public static void companyMenu() throws SQLException {
        System.out.println("""

                1. Company list
                2. Create a company
                0. Back"""
        );
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 0 -> Menu.start();
            case 1 -> {
                CompanyMenu.companyList();
                companyMenu();
            }
            case 2 -> {
                CompanyMenu.createCompany();
                companyMenu();
            }
        }
    }

    public static void createCompany() {
        System.out.println("\nEnter the company name:");
        String name = scanner.nextLine();
        long result = companyDao.createCompany(name);

        if (result == -1) {
            System.out.println("Company with this name already exists");
        } else {
            System.out.println("The company was created!");
        }
    }

    public static void companyList() throws SQLException {
        List<Company> companies = companyDao.getCompanies();

        if (companies.size() == 0) {
            System.out.println("\nThe company list is empty!");
            companyMenu();
        } else {
            System.out.println("\nChoose a company:");
            for (int i = 0; i < companies.size(); i++) {
                Company company = companies.get(i);
                System.out.println(i + 1 + ". " + company.getName());
            }
            System.out.println("0. Back");

            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) {
                companyMenu();
            } else {
                Company chosenCompany = companyDao.getCompany(companies.get(choice - 1).getId());
                CarMenu.carMenu(chosenCompany);
            }
        }
    }
}