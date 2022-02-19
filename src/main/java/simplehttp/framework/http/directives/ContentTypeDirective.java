package simplehttp.framework.http.directives;

import java.util.Arrays;
import java.util.Optional;

import simplehttp.framework.http.enums.MediaType;

public class ContentTypeDirective {
	public static final String CHARSET = "charset";
	public static final String BOUNDARY = "boundary";
	
	private String value,boundary,charset;
	
	public ContentTypeDirective(String value, String charset, String boundary) {
		super();
		this.value = value;
		this.charset = charset;
		this.boundary = boundary;
	}
	public String getValue() {
		return value;
	}
	public String getCharset() {
		return charset;
	}
	public String getBoundary() {
		return boundary;
	}
	
	@Override
	public String toString() {
		return String.format("%s ; boundary=%s;charset=%s", value,boundary,charset);
	}
	
	public MediaType getMediaType() throws Exception {
		Optional<MediaType> optional = Arrays.asList(MediaType.values())
				.stream()
				.filter(media -> media.getValue().equals(value))
				.findFirst();
		return optional.orElseThrow(() -> new Exception("Invalid Content Type"));
	}
	
}
