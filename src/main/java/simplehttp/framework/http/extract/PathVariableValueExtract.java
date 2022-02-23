package simplehttp.framework.http.extract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simplehttp.framework.http.HttpRequest;
import simplehttp.framework.http.annotations.request.PathVariable;
import simplehttp.framework.http.enums.MediaType;

public class PathVariableValueExtract implements ExtractValue {

	@Override
	public Object getArgValue(HttpRequest request,Parameter parameter,Object ...others) throws Exception {
		if(others == null || others.length == 0  || !(others[0] instanceof Map)) {
			throw new Exception("The first object on others array must be a instance of Map class");
		}
		Map<String, Object> pathVariables =(HashMap<String, Object>) others[0];
		PathVariable pathVariable =  parameter.getAnnotation(PathVariable.class);
		String name = pathVariable.name();
		return pathVariables.get(name);
	}

	@Override
	public List<MediaType> notPermited() {
		return Collections.emptyList();
	}

	@Override
	public Class<? extends Annotation> getAnnotation() {
		return PathVariable.class;
	}
	
}
