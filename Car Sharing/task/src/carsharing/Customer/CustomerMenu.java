package carsharing.Customer;

import carsharing.Car.Car;
import carsharing.Car.CarDaoImpl;
import carsharing.Company.Company;
import carsharing.Company.CompanyDaoImpl;
import carsharing.Database;
import carsharing.Menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static carsharing.Menu.start;

public class CustomerMenu {
    static Scanner scanner = new Scanner(System.in);
    static CustomerDaoImpl customerDao = new CustomerDaoImpl(Database.conn);
    static CompanyDaoImpl companyDao = new CompanyDaoImpl(Database.conn);
    static CarDaoImpl carDao = new CarDaoImpl(Database.conn);

    public static void createCustomer() {
        System.out.println("\nEnter the customer name:");
        String name = scanner.nextLine();
        long id = customerDao.createCustomer(name);

        if (id != -1) {
            System.out.println("The customer was added!");
        } else {
            System.out.println("Customer creation failed");
        }
    }

    public static void customerList() throws SQLException {
        List<Customer> customers = customerDao.getCustomers();

        if (customers.size() == 0) {
            System.out.println("\nThe customer list is empty!");
            Menu.start();
        } else {
            System.out.println("\nCustomer List:");
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                System.out.println(i + 1 + ". " + customer.getName());
            }
            System.out.println("0. Back");

            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) {
                start();
            } else {
                Customer chosenCustomer = customerDao.getCustomer(customers.get(choice - 1).getId());
                customerMenu(chosenCustomer);
            }
        }
    }

    private static void customerMenu(Customer customer) throws SQLException {
        System.out.println("""

                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back""");

        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 0 -> start();
            case 1 -> {
                rentCar(customer);
                customerMenu(customer);
            }
            case 2 -> {
                returnCar(customer);
                customerMenu(customer);
            }
            case 3 -> {
                myCar(customer);
                customerMenu(customer);
            }
        }
    }

    private static void myCar(Customer customer) {
        Customer currentCustomer = customerDao.getCustomer(customer.getId());
        if (currentCustomer.getRentedCarId() == 0) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            long carId = currentCustomer.getRentedCarId();
            Car rentedCar = carDao.getCar(carId);
            Company rentedCompany = companyDao.getCompany(rentedCar.getCompanyId());
            System.out.printf("""
                    
                    Your rented car:
                    %s
                    Company:
                    %s
                    """, rentedCar.getName(), rentedCompany.getName());
        }
    }

    private static void returnCar(Customer customer) {
        Customer currentCustomer = customerDao.getCustomer(customer.getId());
        if (currentCustomer.getRentedCarId() == 0) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            boolean isCustomerUpdated = customerDao.updateCustomer(customer, 0);
            if (isCustomerUpdated) {
                System.out.println("\nYou've returned a rented car!");
            } else {
                System.out.println("\nYou didn't rent a car!");
            }
        }
    }

    private static void rentCar(Customer customer) throws SQLException {
        Customer currentCustomer = customerDao.getCustomer(customer.getId());
        if (currentCustomer.getRentedCarId() != 0) {
            System.out.println("\nYou've already rented a car!");
            return;
        }

        List<Company> companies = companyDao.getCompanies();
        if (companies.size() == 0) {
            System.out.println("\nThe car list is empty!");
            rentCar(customer);
        } else {
            System.out.println("\nChoose a company:");
            for (int i = 0; i < companies.size(); i++) {
                Company company = companies.get(i);
                System.out.println((i + 1) + ". " + company.getName());
            }
            System.out.println("0. Back");
        }

        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 0) {
            start();
        } else {
            Company chosenCompany = companyDao.getCompany(companies.get(choice - 1).getId());
            List<Car> cars = carDao.getNonRentedCars(chosenCompany.getId());
            if (cars.size() == 0) {
                System.out.printf("\nNo available cars in the '%s' company\n", chosenCompany.getName());
                rentCar(customer);
            } else {
                System.out.println("\nChoose a car:");
                for (int i = 0; i < cars.size(); i++) {
                    Car car = cars.get(i);
                    System.out.println((i + 1) + ". " + car.getName());
                }
                System.out.println("0. Back");
            }

            int choice2 = Integer.parseInt(scanner.nextLine());
            if (choice2 == 0) {
                start();
            } else {
                Car chosenCar = carDao.getCar(cars.get(choice2 - 1).getId());
                boolean isCustomerUpdated = customerDao.updateCustomer(customer, chosenCar.getId());

                if (isCustomerUpdated) {
                    System.out.printf("\nYou rented '%s'\n", chosenCar.getName());
                } else {
                    System.out.println("\nYou've already rented a car!");
                }
                customerMenu(customer);
            }
        }
    }

}