package carsharing.Car;



import java.util.List;

public interface CarDao {
    Car getCar(long id);
    List<Car> getCars(long companyId);
    long createCar(String name, long companyId);
    boolean updateCar(Car car);
    boolean deleteCar(Car car);
}