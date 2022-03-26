package simplehttp.framework.http.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
	private final static String QUERY_PARAMETER_DELIMITER= "?";
	private final static String QUERY_PARAMETER_SEPARATOR = "&";
	private final static String EQUALS_OPERATOR = "=";
	private HttpUtils() {}
	
	public static Map<String,String> getQueryMap(String path){
		int indexOfQueryDelimiter = path.indexOf(QUERY_PARAMETER_DELIMITER);
		Map<String,String> query = new HashMap<>();
		
		if(indexOfQueryDelimiter > 0 ) {
			String queryString = path.substring(indexOfQueryDelimiter+1);
			Arrays.asList(queryString.split(QUERY_PARAMETER_SEPARATOR)).stream().forEach(value -> {
				String keyValue[] =  value.split(EQUALS_OPERATOR);
				if(keyValue.length  == 2) {
					query.put(keyValue[0], keyValue[1]);
				}else {
					query.put(keyValue[0], null);
				}
				
			});
	
		}
		
		return query;
	}
}
