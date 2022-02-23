package simplehttp.framework.http;

import java.lang.reflect.InvocationTargetException;
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
import simplehttp.framework.http.extract.ExtractValue;
import simplehttp.framework.http.extract.HeaderValueExtract;
import simplehttp.framework.http.extract.PathVariableValueExtract;
import simplehttp.framework.http.extract.PayloadValueExtract;
import simplehttp.framework.http.message.DefaultJsonOutputMessage;
import simplehttp.framework.http.message.HttpOutputMessage;

public class HttpRequestControllerResolver {
	private PathMatcher pathMatcher;
	private HttpRequest httpRequest;
	
	private List<Object> controllers = new ArrayList<>();
	
	private List<ExtractValue> extractions = List.of(
			new PayloadValueExtract(),
			new PathVariableValueExtract(),
			new HeaderValueExtract()
			);
	
	public HttpRequestControllerResolver(HttpRequest httpRequest,List<Object> controllers) {
		this.httpRequest = httpRequest;
		this.pathMatcher = new HttpPathAntMatch();
		
		this.controllers = controllers;
	}
	
	public HttpOutputMessage resolve() throws Exception {
		Map<String, Object> pathVariables = new HashMap<>();
		ObjectAndMethod instanceAndMethod = lookForMethodMatch(pathVariables);
		
		List<Object> args = new ArrayList<>();
		for (Parameter parameter : instanceAndMethod.getMethod().getParameters()) {
			Object argValue = getArgValue(pathVariables, parameter);
			args.add(argValue);
		}
		
		HttpStatus status = null;
		if(instanceAndMethod.getMethod().isAnnotationPresent(ResponseStatus.class)) {
			 status =  instanceAndMethod.getMethod().getAnnotation(ResponseStatus.class).status();
		}
		
		Object returned = instanceAndMethod.invoke(args.toArray());
		return new DefaultJsonOutputMessage(status, new HttpHeaders(), returned);
	}

	private Object getArgValue(Map<String, Object> pathVariables,Parameter parameter) throws Exception {
		
		for (ExtractValue extract : extractions) {
			if(parameter.isAnnotationPresent(extract.getAnnotation())) {
				if(!extract.notPermited().contains(httpRequest.getHeader().contentType().getMediaType())) {
					return extract.getArgValue(httpRequest, parameter,pathVariables);
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
						boolean methodAreEquals = supposedRequest.method().equals(supposedRequest.method());
						String fullPath = pathMatcher.combine(controlerPath, supposedRequest.path());
						boolean match  = pathMatcher.match(fullPath, httpRequest.getPath(), pathVariables);
						boolean contentTypeOk = false;
						try {
							contentTypeOk = supposedRequest.contentType().equals(this.httpRequest.getHeader().contentType().getMediaType());
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
		
		
		public Object invoke(Object ...args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.setAccessible(true);
			return this.method.invoke(instance, args);
		}
		
	}
	
}
