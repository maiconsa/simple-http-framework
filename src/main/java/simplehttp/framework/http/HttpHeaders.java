package simplehttp.framework.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import simplehttp.framework.http.directives.ContentDispositionDirective;
import simplehttp.framework.http.directives.ContentTypeDirective;

public class HttpHeaders  extends HashMap<String, Object>{

	private static final long serialVersionUID = 1L;
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_LEGNTH = "Content-Length";
	public static final String CONTENT_DISPOSITION="Content-Disposition"; 
	
	public HttpHeaders() {
		
	}
	
	
	
	public ContentTypeDirective contentType() {
		if(!containsKey(CONTENT_TYPE)) return null;
			String contentType  = get(CONTENT_TYPE).toString();
			
			String mimeType = null, boundary = "",charset="UTF-8";
							
			List<String> pieces  = Arrays.asList(contentType.split(";"));
			for (String pieceValue : pieces) {
				int index = pieceValue.indexOf('=');
				if(index > 0) {
					  if(pieceValue.contains(ContentTypeDirective.BOUNDARY)) {
						  boundary = pieceValue.substring(index);
					  }
					  if(pieceValue.contains(ContentTypeDirective.CHARSET)) {
						  charset = pieceValue.substring(index);
					  }
				}else {
					mimeType = pieceValue.trim();
				}
			}	
			return new ContentTypeDirective(mimeType, charset, boundary);
			
	}
	
	public ContentDispositionDirective contentDisposition() {
		String contentType  = get(CONTENT_DISPOSITION).toString();
		
		String value = null, name = "",filename="";
						
		List<String> pieces  = Arrays.asList(contentType.split(";"));
		for (String pieceValue : pieces) {
			int index = pieceValue.indexOf('=');
			if(index > 0) {
				  if(pieceValue.contains(ContentDispositionDirective.NAME)) {
					  name = pieceValue.substring(index);
				  }
				  if(pieceValue.contains(ContentDispositionDirective.FILENAME)) {
					  filename = pieceValue.substring(index);
				  }
			}else {
				value = pieceValue.trim();
			}
		}	
		return new ContentDispositionDirective(value, filename, name);
		
}
	
	public long getContentLength() {
		if(!containsKey(CONTENT_LEGNTH)) return 0L;
		return Long.parseLong(get(CONTENT_LEGNTH).toString());
	}
	
	
}
