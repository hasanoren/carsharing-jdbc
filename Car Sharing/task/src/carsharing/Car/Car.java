package carsharing.Car;

public class Car {
    private final long id;
    private String name;
    private long companyId;

    public Car(long id, String name, long companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}