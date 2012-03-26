package q3.server.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class AppConfiguration {
	private String serverPort = "9999";
	private String sqlHost = "127.0.0.1";
	private String sqlPort = "5432";
	private String sqlDbname = "pg@localhost";
	private String sqlTblname = "dist";

	/**
	 * Class AppConfiguration is a CONTRACT between application and xml
	 * AppConfiguration should display xml configuration attribute EXPLICIT,
	 *  
	 * @param configPath - the relative path/absolute path in your config.xml
	 * @throws ConfigurationException
	 */
	public AppConfiguration(String configPath) throws ConfigurationException {
		XMLConfiguration config = new XMLConfiguration(configPath);
		this.serverPort = config.getString("serverport");
		this.sqlHost = config.getString("postgres.host");
		this.sqlPort = config.getString("postgres.port");
		this.sqlDbname = config.getString("postgres.dbname");
		this.sqlTblname = config.getString("postgres.tblname");
	}

	public String getServerPort() {
		return serverPort;
	}

	public String getSqlHost() {
		return sqlHost;
	}

	public String getSqlPort() {
		return sqlPort;
	}

	public String getSqlDbname() {
		return sqlDbname;
	}

	public String getSqlTblname() {
		return sqlTblname;
	}

}
