package com.efimchick.ifmo.web.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

public class SetMapperFactory {

    public SetMapper<Set<Employee>> employeesSetMapper() {
        return new SetMapper<Set<Employee>>() {
            @Override
            public Set<Employee> mapSet(ResultSet resultSet)  {
                try{
                    resultSet.first();
                    LinkedHashSet<Employee> neededSet = new LinkedHashSet<>();
                    while (!resultSet.isAfterLast()){
                        neededSet.add(getEmployee(resultSet));
                        resultSet.next();
                    }
                    return neededSet;
                }catch (SQLException e){
                    HashSet<Employee> neededSet = new HashSet<>();
                    neededSet.add(new Employee(
                            new BigInteger("0"),
                            new FullName(
                                    "where",
                                    "was",
                                    "exception"),
                            Position.valueOf("PRESIDENT"),
                            LocalDate.parse("1981-02-20"),
                            new BigDecimal(1600.0),
                            null));
                    return neededSet;
                }
            }
        };
    }
    private Employee getEmployee(ResultSet resultSet) throws SQLException{
        int thisRow = resultSet.getRow();
        Employee manager = null;
        if (resultSet.getInt("manager") != 0){
            int managerID = resultSet.getInt("manager");
            resultSet.first();
            while (!resultSet.isAfterLast() &&
                    managerID != resultSet.getInt("id")){
                resultSet.next();
            }
            if (!resultSet.isAfterLast()){
                manager = getEmployee(resultSet);
            }
        }
        resultSet.absolute(thisRow);
        return new Employee(
                new BigInteger(String.valueOf(resultSet.getInt("id"))),
                new FullName(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("middleName")),
                Position.valueOf(resultSet.getString("Position")),
                LocalDate.parse(resultSet.getString("hireDate")),
                new BigDecimal(resultSet.getDouble("Salary")),
                manager);
    }
}
