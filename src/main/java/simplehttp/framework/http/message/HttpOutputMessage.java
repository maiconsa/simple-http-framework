package simplehttp.framework.http.message;

import java.io.InputStream;

import simplehttp.framework.http.HttpHeaders;
import simplehttp.framework.http.enums.HttpStatus;

public interface HttpOutputMessage {
	HttpStatus status();
	InputStream fromStream();	
	HttpHeaders getHeaders();
}
