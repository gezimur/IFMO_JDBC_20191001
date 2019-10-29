package com.efimchick.ifmo.web.jdbc;

/**
 * Implement sql queries like described
 */
public class SqlQueries {
    //Select all employees sorted by last name in ascending order
    //language=HSQLDB
    String select01 = "SELECT * FROM EMPLOYEE ORDER BY LASTNAME";

    //Select employees having no more than 5 characters in last name sorted by last name in ascending order
    //language=HSQLDB
    String select02 = "SELECT * FROM EMPLOYEE WHERE LASTNAME LIKE'_____' OR  LASTNAME LIKE'____' OR LASTNAME LIKE'___' OR LASTNAME LIKE'__' OR LASTNAME LIKE'_' ORDER BY LASTNAME";

    //Select employees having salary no less than 2000 and no more than 3000
    //language=HSQLDB
    String select03 = "SELECT * FROM EMPLOYEE WHERE SALARY >= 2000 AND SALARY <= 3000";

    //Select employees having salary no more than 2000 or no less than 3000
    //language=HSQLDB
    String select04 = "SELECT * FROM EMPLOYEE WHERE SALARY <= 2000 OR SALARY >= 3000";

    //Select employees assigned to a department and corresponding department name
    //language=HSQLDB
    String select05 = "SELECT E.*, D.NAME AS DEPNAME FROM EMPLOYEE E INNER JOIN DEPARTMENT D ON D.ID = E.DEPARTMENT";

    //Select all employees and corresponding department name if there is one.
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select06 = "SELECT E.*, D.NAME AS DEPNAME FROM EMPLOYEE E LEFT JOIN DEPARTMENT D ON D.ID = E.DEPARTMENT";

    //Select total salary pf all employees. Name it "total".
    //language=HSQLDB
    String select07 = "SELECT SUM(E.SALARY) AS TOTAL FROM EMPLOYEE E";

    //Select all departments and amount of employees assigned per department
    //Name column containing name of the department "depname".
    //Name column containing employee amount "staff_size".
    //language=HSQLDB
    String select08 = "SELECT D.NAME AS DEPNAME, COUNT(E.DEPARTMENT) AS STAFF_SIZE FROM EMPLOYEE E INNER JOIN DEPARTMENT D ON D.ID = E.DEPARTMENT GROUP BY D.NAME";

    //Select all departments and values of total and average salary per department
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select09 = "SELECT D.NAME AS DEPNAME, SUM(E.SALARY) AS TOTAL, AVG(E.SALARY) AS AVERAGE FROM EMPLOYEE E INNER JOIN DEPARTMENT D ON D.ID = E.DEPARTMENT GROUP BY D.NAME";

    //Select all employees and their managers if there is one.
    //Name column containing employee lastname "employee".
    //Name column containing manager lastname "manager".
    //language=HSQLDB
    String select10 = "SELECT E1.LASTNAME AS EMPLOYEE, E2.LASTNAME AS MANAGER FROM EMPLOYEE E1 LEFT JOIN EMPLOYEE E2 ON E1.MANAGER = E2.ID";


}
