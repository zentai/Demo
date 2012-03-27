package q3.server.db;

import static org.junit.Assert.*;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import q3.server.configuration.AppConfiguration;
import java.sql.Connection;
import java.sql.SQLException;
public class PostgresAgentTest {

	@Test
	public void test_getConnection() throws ConfigurationException, PostgresAgentInitialException, ClassNotFoundException, SQLException {
		AppConfiguration appConfig = new AppConfiguration("../resource/config.xml"); 
		PostgresAgent agent = new PostgresAgent(appConfig);
		Connection connection = agent.getConnection();
		assertNotNull(connection);
	}	
	
	@Test
	public void test_getConnection_in_test_env() throws ConfigurationException, PostgresAgentInitialException, ClassNotFoundException, SQLException {
		AppConfiguration appConfig = new AppConfiguration("../resource/test_config.xml"); 
		PostgresAgent agent = new PostgresAgent(appConfig);
		Connection connection = agent.getConnection();
		assertNotNull(connection);
	}

}
