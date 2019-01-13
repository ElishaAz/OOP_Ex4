package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectToDataBase
{
	public static String getData()
	{
		StringBuilder ans = new StringBuilder();

		String jdbcUrl = "jdbc:mysql://ariel-oop.xyz:3306/oop"; //?useUnicode=yes&characterEncoding=UTF-8&useSSL
		// =false";
		String jdbcUser = "student";
		String jdbcPassword = "student";

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection =
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);


			Statement statement = connection.createStatement();

			//select data
			String allCustomersQuery = "SELECT * FROM logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			System.out.println("FirstID\t\tSecondID\tThirdID\t\tLogTime\t\t\t\tPoint\t\tSomeDouble");
			while (resultSet.next())
			{
				ans.append(resultSet.getInt("FirstID")).append("\t\t").append(resultSet.getInt("SecondID")).
						append("\t\t").append(resultSet.getInt("ThirdID"))
						.append("\t\t").append(resultSet.getTimestamp(
						"LogTime")).append("\t\t\t\t").append(resultSet.getDouble("Point"))
						.append("\t\t").append(resultSet.getDouble("SomeDouble"));
				ans.append('\n');
			}

			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException sqle)
		{
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return ans.toString();
	}

}
