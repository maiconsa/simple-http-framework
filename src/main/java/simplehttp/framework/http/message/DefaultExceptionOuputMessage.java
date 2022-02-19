package simplehttp.framework.http.message;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import simplehttp.framework.http.HttpHeaders;
import simplehttp.framework.http.enums.HttpStatus;
import simplehttp.framework.http.enums.MediaType;

public class DefaultExceptionOuputMessage   {
	
	private static final  String EXCEPTION_MESSAGE = "{\"error\": %s ,\"details\": %s }";
	
	private Exception exception;
	public DefaultExceptionOuputMessage(Exception excetion) {
		this.exception = excetion;
	}
	
	public HttpOutputMessage getOutputMessage() {
		return new HttpOutputMessage() {

			@Override
			public HttpStatus status() {
				return HttpStatus.INTERNAL_SERVER_ERROR;
			}

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders h = new HttpHeaders();
				h.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				return h;
			}

			@Override
			public InputStream fromStream() {
				final String json = String.format(EXCEPTION_MESSAGE, getErrorMessage(),getErrorDetail());
				return new ByteArrayInputStream(json.getBytes());
			}
		};
	}
	
	private String getErrorMessage() {
		return this.exception.getMessage();
	}
	
	private String getErrorDetail() {
		return Arrays.toString(this.exception.getStackTrace());
	}
	
}
