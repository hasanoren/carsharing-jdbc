package carsharing.Company;

import java.util.List;

public interface CompanyDao {
    Company getCompany(long id);
    List<Company> getCompanies();
    long createCompany(String name);
    boolean updateCompany(Company company);
    boolean deleteCompany(Company company);
}