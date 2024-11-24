package carsharing;

import carsharing.Car.CarDaoImpl;
import carsharing.Company.CompanyDaoImpl;

import java.sql.SQLException;
import java.util.Scanner;

import static carsharing.Company.CompanyMenu.companyMenu;
import static carsharing.Customer.CustomerMenu.createCustomer;
import static carsharing.Customer.CustomerMenu.customerList;

public class Menu {
    static Scanner scanner = new Scanner(System.in);
    static CompanyDaoImpl companyDao;
    static CarDaoImpl carDao;

    public Menu() {
        companyDao = new CompanyDaoImpl(Database.conn);
        carDao = new CarDaoImpl(Database.conn);
    }

    public static void start() throws SQLException {
        System.out.println("""
                
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit"""
        );
        while (true) {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 0 -> {
                    return;
                }
                case 1 -> companyMenu();
                case 2 -> customerList();
                case 3 -> {
                    createCustomer();
                    start();
                }
            }
        }
    }
}