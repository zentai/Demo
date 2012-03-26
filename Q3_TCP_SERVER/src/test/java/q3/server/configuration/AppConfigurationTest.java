package q3.server.configuration;

import static org.junit.Assert.*;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

public class AppConfigurationTest{
	@Test
	public void Test_AppConfiguration_method() throws ConfigurationException{
		AppConfiguration config = new AppConfiguration("../resource/config.xml");
		assertEquals("18000", config.getServerPort());
		assertEquals("127.0.0.1", config.getSqlHost());
		assertEquals("5432", config.getSqlPort());
		assertEquals("pg@localhost", config.getSqlDbname());
		assertEquals("dict", config.getSqlTblname());		
	}
}
