package simplehttp.framework.http;

import java.util.Map;

public interface PathMatcher {
	public String combine(String path1 , String path2);
	public boolean match(String pattern,String path, Map<String, Object> pathVariables);
}
