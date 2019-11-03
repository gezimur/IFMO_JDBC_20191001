package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DaoFactory {
    public EmployeeDao employeeDAO() {
        return new EmployeeDao() {
            @Override
            public List<Employee> getByDepartment(Department department) {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " +
                            department.getId().toString());
                    List<Employee> ans = getListOfEmployee(rs);
                    rs.close();
                    return ans;
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM EMPLOYEE WHERE MANAGER = " +
                            employee.getId().toString());
                    List<Employee> ans = getListOfEmployee(rs);
                    rs.close();
                    return ans;
                }catch (SQLException e){
                    return null;
                }
            }
            @Override
            public List<Employee> getAll() {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM EMPLOYEE");
                    List<Employee> ans = getListOfEmployee(rs);
                    rs.close();
                    return ans;
                }catch (SQLException e){
                    return null;
                }
            }

            private List<Employee> getListOfEmployee(ResultSet rs) throws SQLException{
                List<Employee> ans = new LinkedList<>();
                while(rs.next()){
                    ans.add(new Employee(
                            BigInteger.valueOf(rs.getInt("id")),
                            new FullName(
                                    rs.getString("firstName"),
                                    rs.getString("lastName"),
                                    rs.getString("middleName")
                            ),
                            Position.valueOf(rs.getString("Position")),
                            LocalDate.parse(rs.getString("hireDate")),
                            new BigDecimal(rs.getDouble("Salary")),
                            BigInteger.valueOf(rs.getInt("MANAGER")),
                            BigInteger.valueOf(rs.getInt("Department"))
                    ));
                }
                return ans;
            }

            @Override
            public Optional<Employee> getById(BigInteger Id) {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM EMPLOYEE WHERE ID = " +
                            Id.toString());
                    rs.next();
                    Optional<Employee> ans = Optional.of(new Employee(
                            BigInteger.valueOf(rs.getInt("id")),
                            new FullName(
                                    rs.getString("firstName"),
                                    rs.getString("lastName"),
                                    rs.getString("middleName")
                                    ),
                            Position.valueOf(rs.getString("Position")),
                            LocalDate.parse(rs.getString("hireDate")),
                            new BigDecimal(rs.getDouble("Salary")),
                            BigInteger.valueOf(rs.getInt("MANAGER")),
                            BigInteger.valueOf(rs.getInt("Department"))
                    ));
                    rs.close();
                    return ans;
                }catch(SQLException e){
                    return Optional.empty();
                }
            }

            @Override
            public Employee save(Employee employee) {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM EMPLOYEE");

                    while (rs.next() && rs.getInt("id") != employee.getId().intValue()){ }

                    rs.updateInt("ID", employee.getId().intValue());
                    rs.updateString("FIRSTNAME", employee.getFullName().getFirstName());
                    rs.updateString("LASTNAME", employee.getFullName().getLastName());
                    rs.updateString("MIDDLENAME", employee.getFullName().getMiddleName());
                    rs.updateString("POSITION", employee.getPosition().toString());
                    rs.updateInt("MANAGER", employee.getManagerId().intValue());
                    rs.updateString("HIREDATE", employee.getHired().toString());
                    rs.updateDouble("SALARY", employee.getSalary().doubleValue());
                    rs.updateInt("DEPARTMENT", employee.getDepartmentId().intValue());

                    if (rs.isAfterLast()){
                        rs.insertRow();
                    }else{
                        rs.updateRow();
                    }

                    rs.close();
                    return employee;
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public void delete(Employee employee) {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM EMPLOYEE WHERE ID = " +
                            employee.getId().toString());
                    rs.next();
                    rs.deleteRow();
                    rs.close();
                }catch (SQLException e){
                    //lol
                }
            }
        };
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDao() {
            @Override
            public Optional<Department> getById(BigInteger Id) {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM DEPARTMENT WHERE ID = " +
                            Id.toString());
                    rs.next();
                    Optional<Department> ans = Optional.of(new Department(
                            BigInteger.valueOf(rs.getInt("id")),
                            rs.getString("Name"),
                            rs.getString("Location")
                    ));
                    rs.close();
                    return ans;
                }catch(SQLException e){
                    return Optional.empty();
                }
            }

            @Override
            public List<Department> getAll() {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM DEPARTMENT");
                    List<Department> ans = new LinkedList<>();
                    while(rs.next()){
                        ans.add(new Department(
                                BigInteger.valueOf(rs.getInt("id")),
                                rs.getString("Name"),
                                rs.getString("Location")
                        ));
                    }
                    rs.close();
                    return ans;
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public Department save(Department department) {
                try{
                    ResultSet rs = giveMeResultSet("SELECT * FROM Department");
                    while (rs.next() && rs.getInt("id") != department.getId().intValue()){ }
                        rs.updateInt("id", department.getId().intValue());
                        rs.updateString("Name", department.getName());
                        rs.updateString("Location", department.getLocation());

                    if (rs.isAfterLast()){
                        rs.insertRow();
                    }else{
                        rs.updateRow();
                    }
                    rs.close();
                    return department;
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public void delete(Department department) {
                try{
                ResultSet rs = giveMeResultSet("SELECT * FROM Department WHERE ID = " +
                        department.getId().toString());
                rs.next();
                rs.deleteRow();
                rs.close();
                }catch (SQLException e){
                    //lol
                }
            }
        };
    }

    private ResultSet giveMeResultSet(String query) throws SQLException {
        return ConnectionSource.instance()
                .createConnection()
                .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
                .executeQuery(query);
    }
}
