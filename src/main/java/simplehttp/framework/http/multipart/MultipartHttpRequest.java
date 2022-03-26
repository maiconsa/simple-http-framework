package simplehttp.framework.http.multipart;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simplehttp.framework.http.HttpConstants;
import simplehttp.framework.http.HttpHeaders;
import simplehttp.framework.http.HttpRequest;
import simplehttp.framework.http.directives.ContentTypeDirective;

public class MultipartHttpRequest {
	private HttpRequest httpRequest;
	
	
	private List<ReceveidPart> parts;
	
	public MultipartHttpRequest(HttpRequest httpRequest) throws Exception {
		this.httpRequest = httpRequest;
		this.parts= new ArrayList<>();
		this.resolve();
	}
	
	
	private void resolve() throws Exception {
		ContentTypeDirective directives = httpRequest.getHeader().contentType();
		if(directives.getBoundary().length() > 70) {
			throw new Exception("size of boundary parameter must be less than 70 characters");
		}
				
		byte[] messageBody = httpRequest.getMessageBody();

		int boundaryParameterLength = directives.getBoundary().length();
		int pos = 0 ;
		boolean end  = false;
			
		while(end == false){
				byte [] boundaryDelimiter= HttpConstants.BOUNDARY_DELIMITER;
				
				if(pos == 0 ) boundaryDelimiter = HttpConstants.DASH_DASH;
				
				boolean isBoundaryDelimiter = Arrays.equals(Arrays.copyOfRange(messageBody, pos , pos + boundaryDelimiter.length), boundaryDelimiter);
				int indexEndEncapsulationBoundary = pos + boundaryDelimiter.length + boundaryParameterLength;
			    
				end = Arrays.equals(Arrays.copyOfRange(messageBody, indexEndEncapsulationBoundary , indexEndEncapsulationBoundary + HttpConstants.CLOSE_DELIMITER.length), HttpConstants.CLOSE_DELIMITER);
			     if(isBoundaryDelimiter && !end) {
			    	    pos += boundaryDelimiter.length +  boundaryParameterLength  + HttpConstants.CRLF.length  ;
			    		int bodyPartLenght = 0 ;
			    		int startPosition = pos;
			
			    	    boolean bodyPartEnd = Arrays.equals(Arrays.copyOfRange(messageBody, pos , pos + HttpConstants.BOUNDARY_DELIMITER.length), HttpConstants.BOUNDARY_DELIMITER);
			    	    while(!bodyPartEnd) {
			    	    	pos++;
			    	    	bodyPartLenght++;
				    	     bodyPartEnd = Arrays.equals(Arrays.copyOfRange(messageBody, pos , pos + HttpConstants.BOUNDARY_DELIMITER.length), HttpConstants.BOUNDARY_DELIMITER);
			    	    }
			    	    this.parts.add(buildPartFrom(Arrays.copyOfRange(messageBody, startPosition - 1, startPosition +bodyPartLenght )));
			     }
		}
	
		
	}
	
	private ReceveidPart buildPartFrom(byte[] content) {
		int pos = 0 ;
	    boolean headerPartEnd = Arrays.equals(Arrays.copyOfRange(content, pos , pos + HttpConstants.HEADER_PART_END.length), HttpConstants.HEADER_PART_END);
	    while(!headerPartEnd) {
	    	pos++;
		     headerPartEnd = Arrays.equals(Arrays.copyOfRange(content, pos , pos + HttpConstants.HEADER_PART_END.length), HttpConstants.HEADER_PART_END);

	    }
	    
	    byte[] headerBytes = Arrays.copyOfRange(content,0 ,pos);   
	    String lines[] = new String(headerBytes,StandardCharsets.UTF_8).split("\r\n");
	    HttpHeaders headers = new HttpHeaders();

	    for (String line : lines) {
	    	String pieces[] = line.split(HttpConstants.COLON); 
	    		headers.put(pieces[0], pieces[1].trim());
		}
	    
	    byte[] bodyBytes = Arrays.copyOfRange(content, pos +  HttpConstants.HEADER_PART_END.length , content.length);
	    System.out.println(headers);
		return new ReceveidPart(headers,new ByteArrayInputStream(bodyBytes));
	}
	
	
}
