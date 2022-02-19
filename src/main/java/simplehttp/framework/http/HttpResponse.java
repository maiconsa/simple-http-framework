package simplehttp.framework.http;

import static simplehttp.framework.http.HttpConstants.CRLF;
import static simplehttp.framework.http.HttpConstants.HTTP_VERSION_1_1;
import static simplehttp.framework.http.HttpConstants.LF;
import static simplehttp.framework.http.HttpConstants.SP;

import java.io.OutputStream;

import simplehttp.framework.http.enums.HttpStatus;
import simplehttp.framework.http.message.HttpOutputMessage;

public  class HttpResponse {
	
	private OutputStream outputStream;
	
	private HttpHeaders headers;
	
	private HttpStatus status;
	
	private String reasonPhase = "";
	
	public HttpResponse(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	
	public void send(HttpOutputMessage message) throws Exception {
		
		this.status = message.status();
		this.headers = message.getHeaders();
		this.outputStream.write(startLine());
		this.outputStream.write(getByteHeaders());
		this.outputStream.write(LF);
		message.fromStream().transferTo(this.outputStream);
		this.outputStream.write(CRLF);
		this.outputStream.close();
	}
	
	
	private byte[] startLine() {
		
		StringBuilder builder = new StringBuilder();
		builder.append(HTTP_VERSION_1_1);
		builder.append(SP);
		builder.append(status.status());
		builder.append(reasonPhase);
		builder.append(CRLF);
		
		return builder.toString().getBytes();
		
	}
	
	private byte[] getByteHeaders() throws Exception {
		if(this.headers == null) {
			throw new Exception("Headers must not null.");
		}
		
		StringBuilder builder = new StringBuilder();
		this.headers.entrySet().forEach((entry) -> {
			builder.append(entry.getKey());
			builder.append(entry.getValue());
			builder.append("\r\n");
		});
		
		return builder.toString().getBytes();
	}


	public HttpHeaders getHeaders() {
		return headers;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
