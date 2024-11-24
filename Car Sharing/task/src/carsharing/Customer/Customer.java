package carsharing.Customer;

public class Customer {
    private final long id;
    private String name;
    private long rentedCarId;

    public Customer(long id, String name) {
        this.id = id;
        this.name = name;
        this.rentedCarId = 0;
    }
    public Customer(long id, String name, long rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getRentedCarId() {
        return rentedCarId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRentedCarId(long rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}