
public class main{
    public static final void main(String[] args) throws Exception {
        SQLdb sql = new SQLdb();
        for(int i = 2; i < args.length; i++) {
        	args[i] = cleanUpUserIn(args[i]);
        }
        if (args[2].equals("show")) {
        	if(args[3].equals("employees") && args[4].equals("department")) {
        		String deptName;
        		if(args.length == 7) {
        			deptName = args[5] + " " + args[6];
        		}
        		else {
        			deptName = args[5];
        		}
        		sql.showDept(deptName);
        	}
        	else if(args[3].equals("salaries") && args[4].equals("sum")) {
        		sql.showSalaries();
        	}
        	
        }
        if (args[2].equals("add") && args[3].equals("employee")) {
        	if(args.length == 10) {
        		sql.addEmp(args[4], args[5], args[6], args[7], args[8], args[9]);
        	}
        }
        if(args[2].equals("delete") && args[3].equals("employee")) {
        	sql.deleteEmp(args[4]);
        }
    }
  //checks if userIn has quotes on one side or the other, removes them from string
    public static String cleanUpUserIn(String userIn) {
    	if(userIn.charAt(0) == 34 || userIn.charAt(0) == 39) {
    		userIn = userIn.substring(1, userIn.length());
    	}
    	if(userIn.charAt(userIn.length()-1) == 34 || userIn.charAt(userIn.length()-1) == 39) {
    		userIn = userIn.substring(0, userIn.length()-1);
    	}
    	return userIn;
    }
}