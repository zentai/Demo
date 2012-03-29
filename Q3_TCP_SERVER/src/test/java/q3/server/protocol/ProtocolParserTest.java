package q3.server.protocol;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Test;

public class ProtocolParserTest {

	@Test
	public void test() throws IOException {
		
		String term = "passwd";
		String definition = "Allows you to change your password.";
		System.out.println(definition.length());
		byte[] header = {0x0a, (byte) 0xdd};
		byte[] term_length = ByteBuffer.allocate(2).putShort((short) term.getBytes().length).array(); // convert term length to bytes
		byte[] term_byte = term.getBytes();
		byte[] definition_length = ByteBuffer.allocate(4).putInt(definition.getBytes().length).array(); // convert term length to bytes
		byte[] definition_byte = definition.getBytes();
		
		ByteArrayOutputStream f = new ByteArrayOutputStream(); 
		f.write(header);
		f.write(term_length);
		f.write(term_byte);
		f.write(definition_length);
		f.write(definition_byte);
		
		byte[] test_byte = f.toByteArray();
		for (byte b : test_byte) {
			System.out.print((char)b);
		}
		
		ProtocolParser sp = new ProtocolParser(test_byte);
		assertEquals(true, sp.isContainsHeader());
		assertEquals(term.getBytes().length, sp.getTermLength());
		String s = sp.getTerm();
		assertEquals(term, sp.getTerm());
		assertEquals(definition.getBytes().length, sp.getDefinitionLength());
		assertEquals(definition, sp.getDefinition());
	}

}
