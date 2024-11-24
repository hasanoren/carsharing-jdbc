package carsharing.Car;

import carsharing.Company.Company;
import carsharing.Company.CompanyMenu;
import carsharing.Database;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CarMenu {
    static Scanner scanner = new Scanner(System.in);
    static CarDaoImpl carDao = new CarDaoImpl(Database.conn);

    public static void carMenu(Company company) throws SQLException {
        System.out.printf("""

                '%s' company:
                1. Car list
                2. Create a car
                0. Back
                """, company.getName()
        );
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 0 -> CompanyMenu.companyMenu();
            case 1 -> {
                carList(company);
                carMenu(company);
            }
            case 2 -> {
                createCar(company);
                carMenu(company);
            }
        }
    }

    private static void createCar(Company company) {
        System.out.println("\nEnter the car name:");
        String name = scanner.nextLine();
        long result = carDao.createCar(name, company.getId());

        if (result == -1) {
            System.out.println("Car with this name already exists");
        } else {
            System.out.println("The car was created!");
        }
    }

    public static void carList(Company company) {
        List<Car> cars = carDao.getCars(company.getId());

        if (cars.size() == 0) {
            System.out.println("\nThe car list is empty!");
        } else {
            System.out.printf("\n'%s' cars:\n", company.getName());
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                System.out.println(i + 1 + ". " + car.getName());
            }
        }
    }
}