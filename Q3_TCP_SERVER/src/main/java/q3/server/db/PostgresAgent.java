package q3.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import q3.server.configuration.AppConfiguration;
 
public class PostgresAgent {
	
	private AppConfiguration appConfig;
	private String sqlHost;
	private String sqlPort;
	private String sqlDbname;
	private String sqlTblname;
	private String username;
	private String password;

	/**
	 * PostgresAgent is a ORMapping object to handle databases operation.
	 * @param appConfig - a config file to initial db
	 * @throws PostgresAgentInitialException - when appConfig is null 
	 */
	public PostgresAgent(AppConfiguration appConfig) throws PostgresAgentInitialException{
		if (null == appConfig){
			throw new PostgresAgentInitialException("AppConfiguration is not be Null");
		}
		this.appConfig = appConfig;
		this.sqlHost = appConfig.getSqlHost();
		this.sqlPort = appConfig.getSqlPort();
		this.sqlDbname = appConfig.getSqlDbname();
		this.sqlTblname = appConfig.getSqlTblname();
		this.username = "postgres";
		this.password = "00000000";
	}
	
	protected Connection getConnection() throws SQLException, ClassNotFoundException{
 		try {
			Class.forName("org.postgresql.Driver");
 		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			throw e;
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(
					"jdbc:postgresql://" + this.sqlHost + ":" + this.sqlPort + "/" + this.sqlDbname, 
					this.username,
					this.password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			throw e;
		}
		return connection;
	}
}