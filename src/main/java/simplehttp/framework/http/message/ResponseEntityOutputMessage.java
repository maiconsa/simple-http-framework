package simplehttp.framework.http.message;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.google.gson.GsonBuilder;

import simplehttp.framework.http.HttpHeaders;
import simplehttp.framework.http.ResponseEntity;
import simplehttp.framework.http.enums.HttpStatus;

public class ResponseEntityOutputMessage<T> implements HttpOutputMessage {
	
	private ResponseEntity<T> responseEntity;
	
	public ResponseEntityOutputMessage(ResponseEntity<T> responseEntity) {
		this.responseEntity = responseEntity;
	}
	
	@Override
	public HttpStatus status() {
		return this.responseEntity.getStatus();
	}

	@Override
	public InputStream fromStream() {
		String json = new GsonBuilder().create().toJson(this.responseEntity.getPayload());
		return new ByteArrayInputStream(json.getBytes());
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.responseEntity.getHeaders();
	}

}
