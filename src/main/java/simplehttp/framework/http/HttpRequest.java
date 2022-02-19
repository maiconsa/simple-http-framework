package simplehttp.framework.http;

import static simplehttp.framework.http.HttpConstants.COLON;
import static simplehttp.framework.http.HttpConstants.CR;
import static simplehttp.framework.http.HttpConstants.LF;
import static simplehttp.framework.http.HttpConstants.SP;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import simplehttp.framework.http.directives.ContentTypeDirective;
import simplehttp.framework.http.enums.HttpMethod;
import simplehttp.framework.http.enums.MediaType;

public class HttpRequest extends DataInputStream {
	final int BUFFER  = 20;
	

	private HttpMethod method;
	
	private String path;
	
	private String version;
	
	
	private HttpHeaders headers;
	
	private byte[] messageBody;
	
	public HttpRequest(InputStream in) throws IOException {
		super(in);
		init();
	}
	
	private void init() throws IOException {
		requestLine();
		header();
		
		System.out.println(headers);
		message();
	}
	
	private void requestLine() throws IOException {
			byte[] bytes = readLineEndedByCRLF();

			String line = new String(bytes);
			String[] split = line.split(SP);
			
			method = HttpMethod.valueOf(split[0]);
			path = split[1];
			version  = split[2]; 
	}
	
	private void header() throws IOException {

		HttpHeaders map = new HttpHeaders();
		byte[] bytes = new byte[0];
		do {
			 bytes = readLineEndedByCRLF();
			String header[] = new String(bytes).split(COLON);
			if (header.length == 2) {
				map.put(header[0], header[1].trim());
			}
		} while (bytes[0] != CR );
		this.headers = map;
	}
	
	
	private void message() throws IOException {
		long contentLenght = this.headers.getContentLength();
		byte bytes[] = new byte[Long.valueOf(contentLenght).intValue()];
		int pos = 0;
		while(pos < contentLenght) {
			bytes[pos++] = this.readByte();
		}
		this.messageBody = bytes;
	}
	
	private byte[] readLineEndedByCRLF() throws IOException {
		byte[] bytes= new byte[BUFFER];
		int pos  = -1;
		do {
			pos++;
			if(pos == bytes.length ) {
				bytes = Arrays.copyOf(bytes, pos + BUFFER);
			}
			bytes[pos] = this.readByte();
		}while(!endedByCRLF(bytes[pos] , bytes[(pos > 0 ? pos : 1 ) -1]));
		return Arrays.copyOfRange(bytes, 0, pos);

	}
	
	private boolean endedByCRLF(byte last , byte beforeLast) {
			boolean lf = (char) last ==  LF;
			boolean cr = (char) beforeLast ==  CR;
			return lf && cr;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}
	public String getVersion() {
		return version;
	}
	
	public HttpHeaders getHeader(){
		return this.headers;
	}
	
	public byte[] getMessageBody() {
		return this.messageBody;
	}
	
	
	public boolean isMultipart() throws Exception {
		ContentTypeDirective directives = this.getHeader().contentType();
		MediaType mediaType = directives.getMediaType(); 
		
		return mediaType.equals(MediaType.MULTIPART_DATA);
	}
}
