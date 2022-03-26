package simplehttp.framework.http;

import simplehttp.framework.http.enums.HttpStatus;

public class ResponseEntity<T> {
	private HttpStatus status;
	private HttpHeaders headers;
	private T payload;

	public ResponseEntity(HttpStatus status, HttpHeaders headers, T payload) {
		this.status = status;
		this.headers = headers;
		this.payload = payload;
	}


	public HttpStatus getStatus() {
		return status;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public T getPayload() {
		return this.payload;
	}
	
}
