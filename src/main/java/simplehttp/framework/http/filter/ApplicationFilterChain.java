package simplehttp.framework.http.filter;


import java.util.List;

import simplehttp.framework.http.HttpRequest;

public class ApplicationFilterChain implements FilterChain {

	private List<RequestFilter> filters;
	private int pointer = -1;
	public ApplicationFilterChain(List<RequestFilter> filters) {
		this.filters = filters;
	}

	@Override
	public void apply(HttpRequest request) throws Exception {
		pointer++;
		if (hasNext()) {
			RequestFilter filter = this.filters.get(pointer);
			filter.execute(request, this);
		}
	}
	
	
	private boolean hasNext() {
		if(filters == null) return false;
		return this.pointer < filters.size();
	}

}
