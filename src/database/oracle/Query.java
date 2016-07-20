import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

/**
 * The usage for this would be:
 * 
 * java -cp ojdbc14.jar:.  Query -con connection_host:port_number:instance_name -user user_name -passwd password -query "select * from Table"
 * 
 * OR
 * 
 * java -cp ojdbc14.jar:. Query -file FULL_FILE_PATH -query "select * from Table"
 * 
 * 
 * @author kiranshirali@gmail.com
 *
 */


class Query {
	public static void main(String args[]) {
		try {

			String connectionName = "";
			String usernName = "";
			String passwd = "";
			String query = "";
			String filename = "";

			if (args.length == 0) {
				System.out
						.println("Proper Usage is: java Query -con HOST_NAME:PORT:INSTANCE_NAME -user USER_NAME -passwd PASSWORD -query 'QUERY_STRING_IN_DOUBLE_QUOTE'");
				System.out
						.println("Or aletername Usage is: java Query -file FILE_NAME.txt. The file should contain HOST_NAME:PORT:INSTANCE_NAME on first line, username on second line and passwd to third line");
				System.exit(0);
			} else {

				for (int counter = 0; counter < args.length; counter++) {

					if (args[counter].equalsIgnoreCase("-con")) {
						connectionName = args[counter + 1];
					}
					if (args[counter].equalsIgnoreCase("-user")) {
						usernName = args[counter + 1];
					}
					if (args[counter].equalsIgnoreCase("-passwd")) {
						passwd = args[counter + 1];
					}
					if (args[counter].equalsIgnoreCase("-query")) {
						query = args[counter + 1];
						query = query.replace("\"", "");
					}
					if (args[counter].equalsIgnoreCase("-file")) {
						filename = args[counter + 1];
					}

				}
				
				
				if(!filename.isEmpty()){
					BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
					try {
					    StringBuilder sb = new StringBuilder();
					    String line = bufferedReader.readLine();

					    while (line != null) {
					        sb.append(line);
					        sb.append(";");
					        line = bufferedReader.readLine();
					    }
					    String everything = sb.toString();
					    String params[] = everything.split(";");
					    
					    connectionName = params[0];
					    usernName= params[1];
					    passwd= params[2];
					    
					} finally {
						bufferedReader.close();
					}
					
				}

				if (connectionName.isEmpty() || usernName.isEmpty() || passwd
						.isEmpty()) {
					System.out
							.println("Proper Usage is: java Query -con HOST_NAME:PORT:INSTANCE_NAME -user USER_NAME -passwd PASSWORD -query 'QUERY_STRING_IN_DOUBLE_QUOTE'");
					System.out
							.println("Or aletername Usage is: java Query -file FILE_NAME.txt. The file should contain HOST_NAME:PORT:INSTANCE_NAME on first line, username on second line and passwd to third line");
					System.exit(0);
				}

			}

			// step1 load the driver class
			try {

				Class.forName("oracle.jdbc.driver.OracleDriver");

			} catch (ClassNotFoundException e) {

				System.out.println("Where is your Oracle JDBC Driver?");
				e.printStackTrace();
				return;

			}

			
			
			// step2 create the connection object
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@"
					+ connectionName, usernName, passwd);

			// step3 create the statement object
			Statement stmt = con.createStatement();

			// step4 execute query
			ResultSet resultSet = stmt.executeQuery(query);
			ResultSetMetaData rsmd = resultSet.getMetaData();

			int columnCounter = 0;
			while (columnCounter < rsmd.getColumnCount() - 1) {
				System.out.print(rsmd.getColumnName(columnCounter + 2) + "  ");
				columnCounter++;
			}
			System.out.println();
			while (resultSet.next()) {

				columnCounter = 0;
				while (columnCounter < rsmd.getColumnCount()) {
					System.out.print(resultSet.getString(columnCounter + 1)
							+ "  ");
					columnCounter++;
				}
				System.out.println();
			}

			// step5 close the connection object
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}
}