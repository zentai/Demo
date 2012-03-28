package q3.server.protocol;

public class StreamParser {

/**
 *     operation code, int16, must have the value of 0x0add
 *     term data length, uint16
 *     term data, bytes
 *     definition data length, uint32
 *     definition data, bytes
 *     
 *     protocol stracture:
 *     | header | term_length | term_data | definition_length | definition_data |
 *     | int 16 | uint 16     | variable  | uint 32           | variable        |   
 *
*/
	public StreamParser(){
		
	}
}
