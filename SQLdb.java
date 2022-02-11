import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;	

public class SQLdb{
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement prepSt = null;
	private ResultSet resultSet = null;
	public void showDept(String deptName) throws Exception{
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect();
			prepSt = connect.prepareStatement("SELECT emp.emp_no, first_name, last_name FROM departments AS dp, dept_emp AS de, employees"
					+ " AS emp WHERE dp.dept_no = de.dept_no AND emp.emp_no = de.emp_no AND dept_name = ?");
			prepSt.setString(1, deptName);
			resultSet = prepSt.executeQuery();
			while(resultSet.next()){
				String empId = resultSet.getString(1);
			    String firstName = resultSet.getString(2);
			    String lastName = resultSet.getString(3);
			    System.out.println(empId + "	" + firstName + "	" + lastName);
			}
		}
		catch (Exception e){
			throw e;
		}
	}
	public void addEmp(String firstName, String lastName, String deptName, String birthDate, String gender, String salary) throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect();
		    int rows = 0;
		    prepSt = connect.prepareStatement("INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES"
					+ "((SELECT MAX(emp_no) FROM dept_emp)+1, ?, ?, ?, ?, CURDATE())");
		    prepSt.setString(1, birthDate);
		    prepSt.setString(2, firstName);
		    prepSt.setString(3, lastName);
		    prepSt.setString(4, gender);
			rows += prepSt.executeUpdate();
			
			prepSt = connect.prepareStatement("INSERT INTO salaries (emp_no, salary, from_date, to_date) VALUES ((SELECT MAX(emp_no) "
					+ "FROM dept_emp) + 1, ?, CURDATE(), '9999-01-01')");
			prepSt.setString(1, salary);
			rows += prepSt.executeUpdate();
			
			prepSt = connect.prepareStatement("INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) VALUES ((SELECT MAX(emp_no) "
					+ "FROM salaries), (SELECT departments.dept_no FROM departments WHERE dept_name= ?), CURDATE(), '9999-01-01')");
			prepSt.setString(1, deptName);
			rows += prepSt.executeUpdate();
			
			statement = connect.createStatement();
			rows += statement.executeUpdate("INSERT INTO titles (emp_no, title, from_date, to_date) VALUES "
					+ "((SELECT MAX(emp_no) FROM salaries), 'Staff', CURDATE(), '9999-01-01')");
			if(rows == 4) {
				System.out.println("Employee " + firstName + " " + lastName + " added!");
			}
			
		}
		catch (Exception e){
			throw e;
		}
	}
	public void deleteEmp(String empNo) throws Exception{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect();
			prepSt = connect.prepareStatement("SELECT first_name, last_name FROM employees WHERE emp_no= ?");
			prepSt.setString(1, empNo);
		    resultSet = prepSt.executeQuery();
	    	if(resultSet.next()) { //checks if valid empNo in employees  
		    	String firstName = resultSet.getString(1);
		    	String lastName = resultSet.getString(2);
		    	String[] args = {"dept_emp", "employees", "salaries", "titles"};
		    	for(int i = 0; i < args.length; i++) {
		    		prepSt = connect.prepareStatement("DELETE FROM " + args[i] + " WHERE emp_no=?");
		    		prepSt.setString(1, empNo);
		    		prepSt.executeUpdate();
		    	}
				System.out.println("Employee " + firstName + " " + lastName + " was successfully deleted!");
			}
			else{
				System.out.println("Employee with id " + empNo + " does not exist");
			}
		}
		catch (Exception e){
			throw e;
		}
	}
	public void showSalaries() throws Exception{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT SUM(salary) FROM salaries WHERE to_date='9999-01-01'");
			if(resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
			else {
				System.out.println("Error with database fetching");
			}
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	private void connect() throws Exception{
		try {
			Scanner scan = new Scanner(new File("credentials.txt"));
			String con;
			if(scan.hasNextLine()) {
				con = scan.next();
				scan.close();
			}
			else {
				System.out.println("credentials.txt cannot be opened");
				scan.close();
				throw new FileNotFoundException();
				
			}
		    connect = DriverManager.getConnection(con);
			
		}
		catch (Exception e) {
			throw e;
		}
	}
}
