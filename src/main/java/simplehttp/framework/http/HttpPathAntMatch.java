package simplehttp.framework.http;

import java.util.Map;
import java.util.regex.Pattern;

public class HttpPathAntMatch implements PathMatcher {
	
	private final static String DEFAULT_PATH_SEPARATOR = "/";
	
	private final static String OPEN_PATH_VARIABLE = "{";
	private final static String CLOSE_PATH_VARIABLE = "}";
	private final static String QUERY_PARAMETER_DELIMITER= "?";
	private final static String QUERY_PARAMETER_SEPARATOR = "&";
	 
	private final static String PATH_VARIABLE_REGEX = "\\{[^/]+?\\}";

	@Override
	public String combine(String path1, String path2) {
		path1 = path1.trim();
		path2 = path2.trim();
		if(!path1.startsWith(DEFAULT_PATH_SEPARATOR)) path1 = DEFAULT_PATH_SEPARATOR.concat(path1);
		if(path2.endsWith(DEFAULT_PATH_SEPARATOR)) path2 = path2.substring(0, path2.lastIndexOf(DEFAULT_PATH_SEPARATOR));
		
		String merge = path1.concat(DEFAULT_PATH_SEPARATOR).concat(path2);
		return merge.replaceFirst("///", "/").replaceFirst("//", "/").trim();
	}

	/*
	 * Check if pattern 
	 * */
	@Override
	public boolean match(String pattern, String path, Map<String, Object> pathVariables) {
		
		int indexOfQueryDelimiter = path.indexOf(QUERY_PARAMETER_DELIMITER);
		path = indexOfQueryDelimiter > 0  ? path.substring(0,indexOfQueryDelimiter) : path;
		
		String piecesPattern[] = splitBySeparator(pattern);
		String piecesPath[] = splitBySeparator(path);
		if(piecesPath.length != piecesPattern.length) return false;
		
			for (int i = 0; i < piecesPattern.length; i++) {
				if(Pattern.matches(PATH_VARIABLE_REGEX, piecesPattern[i])) {
					String value = piecesPath[i];
					String pathVariable = piecesPattern[i].replace(OPEN_PATH_VARIABLE, "").replace(CLOSE_PATH_VARIABLE, "").trim();
					pathVariables.put(pathVariable, value);
					continue;
				}
				if(!Pattern.matches(piecesPattern[i], piecesPath[i])) {
					return false;
				}
			}			

		return true;
	}
	
	
	
	private String[] splitBySeparator(String path) {
		return path.split(DEFAULT_PATH_SEPARATOR);
	};
	
	public static void main(String[] args) {
		HttpPathAntMatch ant = new HttpPathAntMatch();
		System.out.println(ant.combine("/teste", "/123"));
		System.out.println(ant.combine("/teste", "123"));
		System.out.println(ant.combine("teste", "123"));
		System.out.println(ant.combine("teste", "/123"));
		System.out.println(ant.combine("teste/", "/123/"));
		System.out.println(ant.combine("/teste/", "/123//"));
		System.out.println(ant.combine("/teste/", ""));
	}
	
	
}
