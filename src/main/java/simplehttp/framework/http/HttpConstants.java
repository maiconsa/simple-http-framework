package simplehttp.framework.http;

public class HttpConstants {
	
	public static final char CR =  '\r';
	public static final char LF = '\n';
	public static final char DASH = '-';
	public static final String SP = " ";
	
	public static final byte[] CRLF = new byte[]{CR,LF};
	public static final byte[] BOUNDARY_DELIMITER = new byte[] {CR,LF, DASH,DASH};
	public static final byte[] CLOSE_DELIMITER = new byte[] {DASH,DASH,CR,LF}; 
	public static final byte[] DASH_DASH = new byte[] {DASH,DASH}; 
	public static final byte[] HEADER_PART_END = new byte[] {CR,LF , CR,LF};
	public static final String COLON = ":";
	
	public static final String HTTP_VERSION_1_1 = "HTTP/1.1";

	private HttpConstants() {}
}
