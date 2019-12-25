package com.efimchick.ifmo.web.jdbc.service;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceFactory {
    private Employee president = new Employee(
            new BigInteger(String.valueOf(7839)),
            new FullName(
                    "JOHN",
                    "KING",
                    "MARIA"),
            Position.valueOf("PRESIDENT"),
            LocalDate.parse("1981-11-17"),
            new BigDecimal(5000.00000),
            null,
            null
    );
    public EmployeeService employeeService(){
        return new EmployeeService() {
            @Override
            public List<Employee> getAllSortByHireDate(Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE ORDER BY HIREDATE ASC");
                    List<Employee> ans = getListOfEmployee(resultSet, null);
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getAllSortByLastname(Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE ORDER BY LASTNAME ASC");
                    List<Employee> ans = getListOfEmployee(resultSet, null);
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getAllSortBySalary(Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE ORDER BY SALARY ASC");
                    List<Employee> ans = getListOfEmployee(resultSet, null);
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE ORDER BY DEPARTMENT, LASTNAME");
                    List<Employee> ans = getListOfEmployee(resultSet, null);
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE " +
                                    "WHERE DEPARTMENT = " +
                                    department.getId().intValue() +
                                    " ORDER BY HIREDATE ASC");
                    List<Employee> ans = getListOfEmployee(resultSet, president);
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE E " +
                            "WHERE E.DEPARTMENT = " +
                            department.getId().intValue() +
                            "ORDER BY SALARY ASC");

                    List<Employee> ans = getListOfEmployee(resultSet, president);
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE E " +
                            "WHERE E.DEPARTMENT = " +
                            department.getId().intValue() +
                            "ORDER BY LASTNAME ASC");

                    List<Employee> ans = getListOfEmployee(resultSet, president);
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE E " +
                            "WHERE E.MANAGER = " +
                            manager.getId().intValue() +
                            "ORDER BY LASTNAME ASC");
                    List<Employee> ans = getListOfEmployee(resultSet, getItsManagerNull(manager));
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE E " +
                            "WHERE E.MANAGER = " +
                            manager.getId().intValue() +
                            "ORDER BY HIREDATE ASC");
                    List<Employee> ans = getListOfEmployee(resultSet, getItsManagerNull(manager));
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE " +
                            "WHERE MANAGER = " +
                            manager.getId().intValue() +
                            "ORDER BY SALARY ASC");
                    List<Employee> ans = getListOfEmployee(resultSet, getItsManagerNull(manager));
                    resultSet.close();
                    return getNeededList(paging, ans);
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE ");
                    resultSet.first();
                    while (!resultSet.isAfterLast() &&
                            resultSet.getInt("ID") != employee.getId().intValue()){
                        resultSet.next();
                    }
                    Employee ans = getEmployee(resultSet, true, null);
                    resultSet.close();
                    return ans;
                }catch (SQLException e){
                    return null;
                }
            }

            @Override
            public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
                try{
                    ResultSet resultSet = getResultSet("SELECT * FROM EMPLOYEE E " +
                            "WHERE E.DEPARTMENT = " +
                            department.getId().intValue() +
                            "ORDER BY SALARY DESC");
                    List<Employee> list = getListOfEmployee(resultSet, president);
                    resultSet.close();
                    return list.get(salaryRank - 1);
                }catch (SQLException e){
                    return null;
                }
            }
        };
    }
    private ResultSet getResultSet(String query) throws SQLException {
        return ConnectionSource.instance()
                .createConnection()
                .createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)
                .executeQuery(query);
    }
    private List<Employee> getListOfEmployee(ResultSet resultSet, Employee upperManager)throws SQLException{
        List<Employee> listOfEmployee = new ArrayList<>();
        resultSet.first();
        while (!resultSet.isAfterLast()){
            listOfEmployee.add(getEmployee(resultSet, false, upperManager));
            resultSet.next();
        }
        return listOfEmployee;
    }
    private Employee getEmployee(ResultSet resultSet, boolean isFull, Employee upperManager)throws SQLException{

        int thisRow = resultSet.getRow();
        Employee manager = null;

        if (resultSet.getInt("MANAGER") != 0){
            int managerID = resultSet.getInt("manager");
            resultSet.first();
            while (!resultSet.isAfterLast() &&
                    managerID != resultSet.getInt("id")){
                resultSet.next();
                //search manager row
            }
            if (!resultSet.isAfterLast()){//if found
                if (isFull){
                    manager = getEmployee(resultSet, true, upperManager);
                }else{
                    manager = new Employee(
                            new BigInteger(String.valueOf(resultSet.getInt("id"))),
                            new FullName(
                                    resultSet.getString("firstName"),
                                    resultSet.getString("lastName"),
                                    resultSet.getString("middleName")),
                            Position.valueOf(resultSet.getString("Position")),
                            LocalDate.parse(resultSet.getString("hireDate")),
                            new BigDecimal(resultSet.getDouble("Salary")),
                            null,
                            getDepartment(resultSet.getInt("DEPARTMENT"))
                    );
                }//*/
            }
            if (upperManager != null &&
                    managerID == upperManager.getId().intValue()) manager = upperManager;
        }// we have manager*/
        resultSet.absolute( thisRow);


        return new Employee(
                new BigInteger(String.valueOf(resultSet.getInt("ID"))),
                new FullName(
                        resultSet.getString("FIRSTNAME"),
                        resultSet.getString("LASTNAME"),
                        resultSet.getString("MIDDLENAME")),
                Position.valueOf(resultSet.getString("POSITION")),
                LocalDate.parse(resultSet.getString("HIREDATE")),
                new BigDecimal(resultSet.getDouble("SALARY")),
                manager,
                getDepartment(resultSet.getInt("DEPARTMENT"))
        );
    }
    private Department getDepartment(int departmentId){
        HashMap<Integer, Department> departments = getDepartments();
        if (departments != null){
            return departments.get(departmentId);
        }else{
            return null;
        }
    }

    private  List<Employee> getNeededList(Paging paging, List<Employee> list){
        int left = (paging.page - 1) * paging.itemPerPage;
        int right = left + paging.itemPerPage;
        return list.subList(left, (right < list.size())? right: list.size());
    }
    private HashMap<Integer, Department> getDepartments(){
        try{
            ResultSet resultSet = getResultSet("SELECT * FROM DEPARTMENT");
            HashMap<Integer, Department> ans = new HashMap<>();
            while (resultSet.next()) {
                ans.put(resultSet.getInt("ID"),
                        new Department(
                                new BigInteger(String.valueOf(resultSet.getInt("ID"))),
                                resultSet.getString("NAME"),
                                resultSet.getString("LOCATION"))
                );
            }
            resultSet.close();
            return ans;
        }catch (SQLException e){
            return null;
        }

    }
    private Employee getItsManagerNull(Employee employee){
        return new Employee(
                employee.getId(),
                employee.getFullName(),
                employee.getPosition(),
                employee.getHired(),
                employee.getSalary(),
                null,
                employee.getDepartment()
        );
    }
}
