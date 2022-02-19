package simplehttp.framework.http.filter;

import java.util.List;

import simplehttp.framework.http.HttpRequest;
import simplehttp.framework.http.enums.MediaType;

public class CheckAllowedMediaTypeFilter implements RequestFilter {

	private static final List<MediaType> ALLOWED_CONTENT_TYPE = List.of(MediaType.APPLICATION_JSON);
	
	@Override
	public void execute(HttpRequest request, FilterChain apply) throws Exception {

		MediaType contentType = request.getHeader().contentType().getMediaType();
		if (!ALLOWED_CONTENT_TYPE.contains(contentType)) {
			throw new Exception("Request with Content-Type  " + contentType.getValue() + "  not allowed");
		}

		apply.apply(request);

	}
}
