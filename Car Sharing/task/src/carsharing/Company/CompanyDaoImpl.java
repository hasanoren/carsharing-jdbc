package carsharing.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private final Connection conn;
    private PreparedStatement statement;

    public CompanyDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Company getCompany(long id) {
        Company company = null;
        try {
            String sql = "SELECT * FROM COMPANY WHERE ID = " + id;
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                company = new Company(resultSet.getLong("ID"), resultSet.getString("NAME"));
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        return company;
    }

    @Override
    public List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        try {
            String sql = "SELECT * FROM COMPANY";
            statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getLong("ID"), resultSet.getString("NAME")));
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        companies.sort(Comparator.comparing(Company::getId));
        return companies;
    }

    @Override
    public long createCompany(String name) {
        long id = -1;

        try {
            String sql = "INSERT INTO COMPANY (NAME) VALUES ('" + name + "')";
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            try {
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        id = resultSet.getLong(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Unique index or primary key violation")) {
                    return -1;
                } else {
                    throw e;
                }
            }
        } catch (Exception se) {
            se.printStackTrace();
        }

        return id;
    }

    @Override
    public boolean updateCompany(Company company) {
        try {
            String sql = "UPDATE COMPANY SET NAME = '" + company.getName() + "' WHERE ID = " + company.getId();
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            return statement.getGeneratedKeys().next();
        } catch (Exception se) {
            se.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCompany(Company company) {
        try {
            String sql = "DELETE FROM COMPANY WHERE ID = " + company.getId();
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();

            return statement.getGeneratedKeys().next();
        } catch (Exception se) {
            se.printStackTrace();
            return false;
        }
    }
}