package simplehttp.framework.http;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import simplehttp.framework.http.annotations.request.Controller;
import simplehttp.framework.http.annotations.request.ResponseStatus;
import simplehttp.framework.http.annotations.request.mapping.Do;
import simplehttp.framework.http.enums.HttpStatus;
import simplehttp.framework.http.enums.MediaType;
import simplehttp.framework.http.extract.ExtractValue;
import simplehttp.framework.http.extract.HeaderValueExtract;
import simplehttp.framework.http.extract.PathVariableValueExtract;
import simplehttp.framework.http.extract.PayloadValueExtract;
import simplehttp.framework.http.extract.QueryParameterValueExtract;
import simplehttp.framework.http.message.DefaultJsonOutputMessage;
import simplehttp.framework.http.message.HttpOutputMessage;
import simplehttp.framework.http.message.ResponseEntityOutputMessage;
import simplehttp.framework.http.utils.HttpUtils;

public class HttpRequestControllerResolver {
	private PathMatcher pathMatcher;
	private HttpRequest httpRequest;
	
	private List<Object> controllers = new ArrayList<>();
	
	private List<ExtractValue> extractions = List.of(
			new PayloadValueExtract(),
			new PathVariableValueExtract(),
			new HeaderValueExtract(),
			new QueryParameterValueExtract()
			);
	
	public HttpRequestControllerResolver(HttpRequest httpRequest,List<Object> controllers) {
		this.httpRequest = httpRequest;
		this.pathMatcher = new HttpPathAntMatch();
		
		this.controllers = controllers;
	}
	
	public HttpOutputMessage resolve() throws Exception {
		Map<String, Object> pathVariables = new HashMap<>();

		ObjectAndMethod instanceAndMethod = lookForMethodMatch(pathVariables);
		Map<String,String> queryParameters   = HttpUtils.getQueryMap(this.httpRequest.getPath());
		List<Object> args = new ArrayList<>();
		for (Parameter parameter : instanceAndMethod.getMethod().getParameters()) {
			Object argValue = getArgValue(pathVariables,queryParameters, parameter);
			args.add(argValue);
		}
			
		Object returned = instanceAndMethod.invoke(args.toArray());
		if (returned instanceof ResponseEntity) {
			return new ResponseEntityOutputMessage<>((ResponseEntity<?>)returned);
		} else {
			if(!instanceAndMethod.getMethod().isAnnotationPresent(ResponseStatus.class)) throw  new Exception("Invalid HttpStatus");
			HttpStatus	status = instanceAndMethod.getMethod().getAnnotation(ResponseStatus.class).status();
			return new DefaultJsonOutputMessage(status, new HttpHeaders(), returned);
		}
	}

	private Object getArgValue(Map<String, Object> pathVariables, Map<String, String> queryParameters,Parameter parameter) throws Exception {
		for (ExtractValue extract : extractions) {
			if(parameter.isAnnotationPresent(extract.getAnnotation())) {
				if(!extract.notPermited().contains(httpRequest.getHeader().contentType().getMediaType())) {
					return extract.getArgValue(httpRequest, parameter,pathVariables,queryParameters);
				}
			}
		}
		return null;
	}
	

	private ObjectAndMethod lookForMethodMatch(Map<String, Object> pathVariables) throws Exception {
		for (Object controler : controllers) {
			Controller controllerAnnotation = Arrays.asList(controler.getClass().getAnnotationsByType(Controller.class))
					.stream().distinct().findFirst().orElseThrow(() -> new Exception("Class is not a Controller"));
			
			String controlerPath = controllerAnnotation.path();
		
			Optional<Method> method =  Arrays.asList(controler.getClass().getMethods()).stream()
					.filter(item -> item.isAnnotationPresent(Do.class))
					.filter(item -> {
						Do supposedRequest = item.getAnnotation(Do.class);
						boolean methodAreEquals = supposedRequest.method().equals(this.httpRequest.getMethod());
						String fullPath = pathMatcher.combine(controlerPath, supposedRequest.path());
						boolean match  = pathMatcher.match(fullPath, httpRequest.getPath(), pathVariables);
						boolean contentTypeOk = false;
						try {
							
							if(this.httpRequest.getHeader().contentType() == null) {
								String accept = this.httpRequest.getHeader().getAccept();
								contentTypeOk = accept != null && accept.contains(MediaType.TEXT_HTML.getValue()) ;
							}else {
								contentTypeOk = supposedRequest.contentType().equals(this.httpRequest.getHeader().contentType().getMediaType());
							}
						} catch (Exception e) {
							return false;
						}
								
 						return methodAreEquals &&  match && contentTypeOk;
					}).findFirst();
			
			if(!method.isPresent()) {
				continue;
			}
			
			return new ObjectAndMethod(controler, method.get());
		}
		
		throw new Exception("None controller was finded for :" + httpRequest.getMethod() + " " + httpRequest.getPath());
	}	
	
	
	public class ObjectAndMethod{
		private Object instance;
		private Method method;
		
		public ObjectAndMethod(Object instance , Method method) {
			this.instance = instance;
			this.method  = method;
		}

		public Object getInstance() {
			return instance;
		}

		public Method getMethod() {
			return method;
		}
		
		
		public Object invoke(Object ...args) throws Exception {
			method.setAccessible(true);
			return this.method.invoke(instance, args);
		}
		
	}
	
}
