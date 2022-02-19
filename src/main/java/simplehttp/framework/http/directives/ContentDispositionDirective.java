package simplehttp.framework.http.directives;

public class ContentDispositionDirective {
	public static final String NAME = "name";
	public static final String FILENAME = "boundary";
	
	private String value,name,filename;
	
	public ContentDispositionDirective(String value, String filename, String name) {
		super();
		this.value = value;
		this.filename = filename;
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public String getCharset() {
		return filename;
	}
	public String getBoundary() {
		return name;
	}
	
	@Override
	public String toString() {
		return String.format("%s ; name=%s;filename=%s", value,name,filename);
	}
	
	
}
