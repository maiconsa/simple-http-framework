package simplehttp.framework.http.multipart;

import java.io.InputStream;

import simplehttp.framework.http.HttpHeaders;

public class ReceveidPart {
	private HttpHeaders headers;
	private InputStream inputStream;
	public ReceveidPart(HttpHeaders headers, InputStream inputStream) {
		super();
		this.headers = headers;
		this.inputStream = inputStream;
	}
	
	
}
