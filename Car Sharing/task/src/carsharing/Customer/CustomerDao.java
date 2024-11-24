package carsharing.Customer;

import java.util.List;

public interface CustomerDao {
    Customer getCustomer(long id);
    List<Customer> getCustomers();
    long createCustomer(String name);
    boolean updateCustomer(Customer customer, long carId);
    boolean deleteCustomer(Customer customer);
}