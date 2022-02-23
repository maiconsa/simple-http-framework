package simplehttp.framework.http.extract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

import simplehttp.framework.http.HttpRequest;
import simplehttp.framework.http.enums.MediaType;

public interface ExtractValue {
	Object getArgValue(HttpRequest request,Parameter parameter,Object ...others) throws Exception;
	List<MediaType> notPermited();
	Class<? extends Annotation> getAnnotation();
}
