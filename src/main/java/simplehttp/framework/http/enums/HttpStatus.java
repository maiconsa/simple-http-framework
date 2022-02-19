package simplehttp.framework.http.enums;

public enum HttpStatus {
	OK(200),BAD_REQUEST(400),NOT_CONTENT(204),INTERNAL_SERVER_ERROR(500);
	int statusCode;
	
	private HttpStatus(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public int status() {
		return this.statusCode;
	}
	
}
