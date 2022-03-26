package simplehttp.framework.http.extract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import simplehttp.framework.http.HttpRequest;
import simplehttp.framework.http.annotations.request.QueryParameter;
import simplehttp.framework.http.enums.MediaType;

public class QueryParameterValueExtract implements ExtractValue {

	@Override
	public Object getArgValue(HttpRequest request,Parameter parameter,Object ...others) throws Exception {
		if(others == null || others.length == 0  || !(others[1] instanceof Map)) {
			throw new Exception("The second object on others array must be a instance of Map class");
		}
		Map<String, Object> headers = (Map<String,Object>) others[1];
		QueryParameter queryParameter =  parameter.getAnnotation(QueryParameter.class);
		String name = queryParameter.name();
		return headers.get(name);
	}

	@Override
	public List<MediaType> notPermited() {
		return Collections.emptyList();
	}

	@Override
	public Class<? extends Annotation> getAnnotation() {
		return QueryParameter.class;
	}
	
}
