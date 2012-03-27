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
	
	/*
	 * Requirement:
    - updates database with received data by the following rules:
        - all terms are versioned. A new term starts with version number 1.
        - terms are case-sensitive
        - if a term doesn't exist, it is added
        - if an existing term has an empty definition in received data, it is removed from database
        - if an existing term's definition in database is the same as received data, it remains unchanged
        - if an existing term's definition in database differs from received data, its definition in database is updated with received data and its version number bumped up by 1 
	 */
	@Test
	public void test_save() throws ConfigurationException, PostgresAgentInitialException{
		AppConfiguration appConfig = new AppConfiguration("../resource/test_config.xml"); 
		PostgresAgent agent = new PostgresAgent(appConfig);
		
		/*
		 * if databases dont have term record
		 * PostgresAgent.query(term) should return ["0", null, null]  
		 */
		String[] term_record = agent.query(term);
		this.assertTermRecordEqual("0", null, null, term_record);

		/*
		 * - all terms are versioned. A new term starts with version number 1.
		 * - if a term doesn't exist, it is added
		 * 
		 * after update term, 
		 * PostgresAgent.query(term) should return ["1", "test", "defination_v1"]  
		 */
		
		String term = "test";
		String defination = "defination_v1";
		agent.update(term, defination);
		term_record = agent.query(term);
		this.assertTermRecordEqual("1", term, defination, term_record);

		/*
		 * - if an existing term's definition in database is the same as received data, it remains unchanged
		 *  
		 * PostgresAgent.query(term) should return ["1", "test", "defination_v1"]  
		 */
		term = "test";
		defination = "defination_v1";
		agent.update(term, defination);
		term_record = agent.query(term);
		this.assertTermRecordEqual("1", term, defination, term_record);
		
		/*
		 * - if an existing term's definition in database differs from received data, its definition in database is updated with received data and its version number bumped up by 1
		 *  
		 * PostgresAgent.query(term) should return ["2", "test", "defination_v2"]  
		 */
		term = "test";
		defination = "defination_v2";
		agent.update(term, defination);
		term_record = agent.query(term);
		this.assertTermRecordEqual("2", term, defination, term_record);

		/*
		 * - if an existing term has an empty definition in received data, it is removed from database
		 *  
		 * PostgresAgent.query(term) should return ["0", null, null]  
		 */
		term = "test";
		defination = null;
		agent.update(term, defination);
		term_record = agent.query(term);
		this.assertTermRecordEqual("0", term, defination, term_record);
		

	}

	private void assertTermRecordEqual(String expVersion, String expTerm, String expDefination, String[] term_record) {
		String expected_version = expVersion;  
		String actual_version = term_record[0];
		assertEquals(expected_version, actual_version);
		
		String expected_term = expTerm;  
		String actual_term = term_record[1];
		assertEquals(expected_term, actual_term);
		
		String expected_defination = expDefination;  
		String actual_defination = term_record[2];
		assertEquals(expected_defination, actual_defination);
	}

}
