package simplehttp.framework.http.enums;

public enum MediaType {
	APPLICATION_JSON("application/json"),
	MULTIPART_DATA("multipart/form-data"),
	TEXT_HTML("text/html"),
	TEXT_PLAIN("text/plain"),
	ALL("*/*");
	private String value;
	MediaType(String value){
		this.value = value;
	}
	
	
	public String getValue() {
		return this.value;
	}
}
