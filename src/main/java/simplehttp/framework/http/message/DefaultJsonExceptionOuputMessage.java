package simplehttp.framework.http.message;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import simplehttp.framework.http.enums.HttpStatus;

public class DefaultJsonExceptionOuputMessage extends DefaultJsonOutputMessage   {
	
	private static final  String EXCEPTION_MESSAGE = "{\"error\": %s ,\"details\": %s }";
	
	public DefaultJsonExceptionOuputMessage(Exception excetion) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, null, excetion);
	}
	

	@Override
	public InputStream fromStream() {
		final String json = String.format(EXCEPTION_MESSAGE, getErrorMessage(),getErrorDetail());
		return new ByteArrayInputStream(json.getBytes());
	}
	private String getErrorMessage() {
			return this.getObjectAsException().getMessage();
	}
	
	private String getErrorDetail() {
		return Arrays.toString(this.getObjectAsException().getStackTrace());
	}
	
	private Exception getObjectAsException() {
		return (Exception) this.object;
	}
	
}
