package simplehttp.framework.http.filter;

import simplehttp.framework.http.HttpRequest;

public interface FilterChain {
	void apply(HttpRequest request) throws Exception;
}
