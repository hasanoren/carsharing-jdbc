package carsharing.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private final Connection conn;
    private PreparedStatement statement;

    public CustomerDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Customer getCustomer(long id) {
        Customer customer = null;
        try {
            String sql = "SELECT * FROM CUSTOMER WHERE ID = " + id;
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                customer = new Customer(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getLong("RENTED_CAR_ID"));
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        return customer;
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            String sql = "SELECT * FROM CUSTOMER;";
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customers.add(new Customer(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getLong("RENTED_CAR_ID")));
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        customers.sort(Comparator.comparing(Customer::getId));
        return customers;
    }

    @Override
    public long createCustomer(String name) {
        long id = -1;

        try {
            String sql = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            } else {
                throw new Exception("No ID returned");
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        return id;
    }

    @Override
    public boolean updateCustomer(Customer customer, long carId) {
        try {
            String sql = "UPDATE CUSTOMER SET NAME = ?, RENTED_CAR_ID = ? WHERE ID = ?";
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getName());
            if (carId == 0) {
                statement.setNull(2, java.sql.Types.BIGINT);
            } else {
                statement.setLong(2, carId);
            }
            statement.setLong(3, customer.getId());
            statement.executeUpdate();

            return statement.getGeneratedKeys().next();
        } catch (Exception se) {
            se.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(Customer customer) {
        try {
            String sql = "DELETE FROM CUSTOMER WHERE ID = " + customer.getId();
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();

            return true;
        } catch (Exception se) {
            se.printStackTrace();
            return false;
        }
    }
}