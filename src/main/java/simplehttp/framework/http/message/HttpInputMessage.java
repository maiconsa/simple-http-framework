package simplehttp.framework.http.message;

import simplehttp.framework.http.HttpHeaders;
import simplehttp.framework.http.enums.HttpStatus;

public interface HttpInputMessage {
	HttpStatus status();
	byte[] getBody();
	HttpHeaders getHeaders();
}
