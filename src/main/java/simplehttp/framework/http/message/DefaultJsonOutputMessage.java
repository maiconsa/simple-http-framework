package simplehttp.framework.http.message;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.google.gson.GsonBuilder;

import simplehttp.framework.http.HttpHeaders;
import simplehttp.framework.http.enums.HttpStatus;
import simplehttp.framework.http.enums.MediaType;

public class DefaultJsonOutputMessage implements HttpOutputMessage {
	protected HttpStatus status;
	protected HttpHeaders headers;
	protected Object object;

	public DefaultJsonOutputMessage(HttpStatus status, HttpHeaders headers, Object object) {
		this.status = status;
		this.headers = headers;
		this.object = object;

		defaultHeaderInitialize();

	}

	private void defaultHeaderInitialize() {
		if (this.headers == null) {
			this.headers = new HttpHeaders();
			HttpHeaders h = new HttpHeaders();
			h.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
		}

		if (!this.headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
			this.headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue());
		}
	}

	@Override
	public HttpStatus status() {
		return status;
	}

	@Override
	public InputStream fromStream() {
		String json = new GsonBuilder().create().toJson(this.object);
		return new ByteArrayInputStream(json.getBytes());
	}

	@Override
	public HttpHeaders getHeaders() {

		return headers;
	}

}
