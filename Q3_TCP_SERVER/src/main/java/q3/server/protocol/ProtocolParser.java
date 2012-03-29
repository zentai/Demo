package q3.server.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolParser {
	static final Logger logger = LoggerFactory.getLogger(ProtocolParser.class);
	private byte[] bytes;
	/**
	 * operation code, int16, must have the value of 0x0add term data length,
	 * uint16 term data, bytes definition data length, uint32 definition data,
	 * bytes
	 * 
	 * protocol stracture: | header | term_length | term_data | definition_length | definition_data |
	 *                     | int 16 | uint 16     | variable  | uint 32           | variable        |
	 * @param test_byte 
	 * 
	 */
	public ProtocolParser(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * Convert byte array to String 
	 * @param bytes - the byte array to be convert 
	 * @return String, the byte array convert result
	 */
	public String bytesToString(byte[] bytes){
		return new String(bytes);
	}
	
	/**
	 * Convert bytes to uint16, 
	 * @param bytes - 2 byte to be convert to unit16,
	 * @return -1 if bytes array length not equal 2 bytes else return int
	 */
	public int bytesToUint16(byte[] bytes){
		if (bytes.length != 2){
			logger.error("Uint16 should consist by 2 bytes");
			return -1;
		}
		return (unsignedByteToInt(bytes[0]) << 8) | unsignedByteToInt(bytes[1]);
	}
	
	/**
	 * Convert bytes to uint32, 
	 * @param bytes - 4 byte to be convert to unit32,
	 * @return -1 if bytes array length not equal 2 bytes else return int
	 */
	public int bytesToUint32(byte[] bytes){
		if (bytes.length != 4){
			logger.error("Uint32 should consist by 4 bytes");
			return -1;
		}
		return (unsignedByteToInt(bytes[0]) << 24) | (unsignedByteToInt(bytes[1]) << 16) | (unsignedByteToInt(bytes[2]) << 8) | unsignedByteToInt(bytes[3]);
	}	
	
	/**
	 * a util function to convert byte to unsign int
	 * @param b - byte to be convert to int
	 * @return integer
	 */
	private int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}
	
	/**
	 * check if protocol header exists 
	 * @param bytes
	 * @return true if exists else false
	 */
	private boolean isHeader(byte[] bytes){
		byte[] header = { 0xa, (byte) 0xdd };
		if (bytes[0] == header[0] && bytes[1] == header[1]){
			return true;
		}
		return false;
	}

	/**
	 * check if protocol header exists 
	 * @return true if header exists
	 */
	public boolean isContainsHeader() {
		return this.isHeader(this.bytes);
	}

	/**
	 * return the Term byte length.
	 * @return int - term byte length
	 */
	public int getTermLength() {
		byte[] term_length = {this.bytes[2], this.bytes[3]};
		return this.bytesToUint16(term_length);
	}

	/**
	 * get term from protocols byte arrays
	 * @return String 
	 */
	public String getTerm() {
		int start_index = 2 + 2; // + header 2bytes + term length 2bytes
		int term_length = this.getTermLength();
        
		byte[] dist = new byte[term_length];
        System.arraycopy(this.bytes, start_index, dist, 0, term_length);
		return this.bytesToString(dist);
	}

	/**
	 * get Definition length from byte arrays
	 * @return int , the bytes length of Definition
	 */
	public int getDefinitionLength() {
		byte[] definition_length = new byte[4];
		int start_index = 2 + 2 + this.getTermLength(); // head + term_length + term
		System.arraycopy(this.bytes, start_index, definition_length, 0, 4);
		return this.bytesToUint32(definition_length);
	}

	/**
	 * get Definition content from byte arrays
	 * @return String, the content from byte arrays
	 */
	public String getDefinition() {  
		int definition_length = this.getDefinitionLength();
		byte[] definition = new byte[definition_length];
		int start_index = 2 + 2 + this.getTermLength() + 4; // head + term_length + term + definition_length
		System.arraycopy(this.bytes, start_index, definition, 0, definition_length);
		return this.bytesToString(definition);
	}
}
