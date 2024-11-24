package carsharing.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CarDaoImpl implements CarDao, AutoCloseable {
    private final Connection conn;
    private PreparedStatement statement;

    public CarDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Car getCar(long id) {
        Car car = null;
        try {
            String sql = "SELECT * FROM CAR WHERE ID = " + id;
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                car = new Car(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getLong("COMPANY_ID"));
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        return car;
    }

    @Override
    public List<Car> getCars(long companyId) {
        List<Car> cars = new ArrayList<>();

        try {
            String sql = "SELECT * FROM CAR WHERE COMPANY_ID = " + companyId;
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getLong("COMPANY_ID")));
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        cars.sort(Comparator.comparing(Car::getId));
        return cars;
    }

    public List<Car> getNonRentedCars(long companyId) {
        List<Car> cars = new ArrayList<>();

        try {
            String sql = "SELECT * FROM CAR WHERE COMPANY_ID = " + companyId + " AND ID NOT IN (SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT  NULL)";
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getLong("COMPANY_ID")));
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        cars.sort(Comparator.comparing(Car::getId));
        return cars;
    }

    @Override
    public long createCar(String name, long companyId) {
        long id = -1;

        try {
            String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + name + "'," + companyId + ")";
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating car failed, no ID obtained.");
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Unique index or primary key violation")) {
                    return -1;
                } else {
                    throw e;
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return id;
    }

    @Override
    public boolean updateCar(Car car) {
        try {
            String sql = "UPDATE CAR SET NAME = '" + car.getName() + "', COMPANY_ID = " + car.getCompanyId() + " WHERE ID = " + car.getId();
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            return statement.getGeneratedKeys().next();
        } catch (Exception se) {
            se.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCar(carsharing.Car.Car car) {
        try {
            String sql = "DELETE FROM CAR WHERE ID = " + car.getId();
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            return statement.getGeneratedKeys().next();
        } catch (Exception se) {
            se.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() throws SQLException {
        System.out.println("Closing CarDao connection");
        conn.close();
    }
}
