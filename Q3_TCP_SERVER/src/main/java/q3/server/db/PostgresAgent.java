package q3.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import q3.server.configuration.AppConfiguration;
/**
 *
 *	  Requirement:
 *    - updates database with received data by the following rules:
 *    - all terms are versioned. A new term starts with version number 1.
 *    - terms are case-sensitive
 *    - if a term doesn't exist, it is added
 *    - if an existing term has an empty definition in received data, it is removed from database
 *    - if an existing term's definition in database is the same as received data, it remains unchanged
 *    - if an existing term's definition in database differs from received data, its definition in database is updated with received data and its version number bumped up by 1 
 * 
 * @author Lenovo
 *
 */
public class PostgresAgent {
	static final Logger log = LoggerFactory.getLogger(PostgresAgent.class);
	private AppConfiguration appConfig;
	private String sqlHost;
	private String sqlPort;
	private String sqlDbname;
	private String sqlTblname;
	private String username;
	private String password;

	/**
	 * PostgresAgent is a ORMapping object to handle databases operation.
	 * 
	 * @param appConfig
	 *            - a config file to initial db
	 * @throws PostgresAgentInitialException
	 *             - when appConfig is null
	 */
	public PostgresAgent(AppConfiguration appConfig)
			throws PostgresAgentInitialException {
		if (null == appConfig) {
			log.error("AppConfiguration is not be Null");
			throw new PostgresAgentInitialException(
					"AppConfiguration is not be Null");
		}
		this.appConfig = appConfig;
		this.sqlHost = appConfig.getSqlHost();
		this.sqlPort = appConfig.getSqlPort();
		this.sqlDbname = appConfig.getSqlDbname();
		this.sqlTblname = appConfig.getSqlTblname();
		this.username = "postgres";
		this.password = "00000000";
	}

	protected Connection getConnection() throws SQLException,
			ClassNotFoundException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			log.error("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			throw e;
		}

		System.out.println("PostgreSQL JDBC Driver Registered!");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://"
					+ this.sqlHost + ":" + this.sqlPort + "/" + this.sqlDbname,
					this.username, this.password);
		} catch (SQLException e) {
			log.error("Connection Failed! Check output console");
			throw e;
		}
		return connection;
	}

	public String[] query(String term) throws ClassNotFoundException,
			SQLException {
		Connection connection = this.getConnection();
		if (null == connection) {
			log.error("Connection should not be null");
			return new String[0];
		}
		try {
			Statement stGetCount = connection.createStatement();
			ResultSet rs = stGetCount.executeQuery("SELECT * from "
					+ this.sqlTblname + " where term = '" + term + "'");
			String[] result = new String[3];
			while (rs.next()) {
				result[0] = rs.getString(1);
				result[1] = rs.getString(2);
				result[2] = rs.getString(3);
				log.debug("Result: " + rs.getString(1) + ", " + rs.getString(2)
						+ ", " + rs.getString(3));
			}

			return result;
		} catch (SQLException e) {
			log.error("Could not create statement in JDBC");
			e.printStackTrace();
		}

		return null;
	}

	protected void update_db(Connection connection, String sqlText){
		try {
			Statement st = connection.createStatement();
			log.debug("Executing this command: " + sqlText + "\n");
			st.executeUpdate(sqlText);
		} catch (SQLException e) {
			log.error("Could not create statement in JDBC");
			e.printStackTrace();
		}
	}
	public void update(String term, String definition) throws ClassNotFoundException, SQLException {
		if (null == term){
			log.error("term is primary key, should not be null");
			return;
		}
		String[] term_result = this.query(term);
		int version = 0;
		Connection connection = this.getConnection();
        
		/*
         * - if an existing term has an empty definition in received data, 
         * 	 it is removed from database
         */
		if (null == definition){
			log.debug("term has an empty definition in received data, it is removed from database");
			String sqlText = "delete from " + this.sqlTblname + " where term = '"+ term + "'";
			this.update_db(connection, sqlText);
			return;
		}
		
		/*
		 * - if an existing term's definition in database is the same as received data, 
		 * it remains unchanged 
		 */
		if (this.is_same(term, definition, term_result)){
			log.debug("term's definition in database is the same as received data, it remains unchanged");
			return;
		}
		
		/*
		 * - if a term doesn't exist, it is added
		 * - all terms are versioned. A new term starts with version number 1.
		 */
		if (this.is_emptyRecord(term_result)) {
			log.debug("a term doesn't exist, it is added");
			version = 1;
			String sqlText = "insert into " + this.sqlTblname + " values ('"+ term + "', " + version + ", '" + definition + "')";
			this.update_db(connection, sqlText);			
		}else{
	        /*
	         *  - if an existing term's definition in database differs from received data, 
	         *  its definition in database is updated with received data and its version number bumped up by 1
	         */
			log.debug("existing term's definition in database, verison update");
			version = Integer.parseInt(term_result[1]);
			version = version + 1;
			String sqlText = "update " + this.sqlTblname + " set version = " + version + ", definition = '"+ definition +"' where term = '"+ term +"'";
			this.update_db(connection, sqlText);
		}
	}

	private boolean is_emptyRecord(String[] term_result) {
		if (term_result[1] == null){
			return true;
		}
		return false;
	}

	private boolean is_same(String term, String defination, String[] term_result) {
		String actual_term = term_result[0];
		String actual_defination = term_result[2];
		if (term.equals(actual_term) && defination.equals(actual_defination)) {
			return true;
		}
		return false;
	}
}