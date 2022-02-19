package simplehttp.framework.http.filter;

import simplehttp.framework.http.HttpRequest;

public interface RequestFilter {
	void  execute(HttpRequest request,FilterChain apply) throws Exception;
}


