package simplehttp.framework.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.GsonBuilder;

import simplehttp.framework.http.annotations.request.Controller;
import simplehttp.framework.http.annotations.request.PathVariable;
import simplehttp.framework.http.annotations.request.Payload;
import simplehttp.framework.http.annotations.request.mapping.Do;
import simplehttp.framework.http.enums.HttpStatus;
import simplehttp.framework.http.enums.MediaType;
import simplehttp.framework.http.message.HttpOutputMessage;



public class HttpRequestControllerResolver {
	private PathMatcher pathMatcher;
	private HttpRequest httpRequest;
	
	private List<Object> controllers = new ArrayList<>();
	
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
			if(parameter.isAnnotationPresent(Payload.class)) {
			    String json = new String(this.httpRequest.getMessageBody());
			    Object instance=   new GsonBuilder().create().fromJson(json, parameter.getType());
			    args.add(instance);
			}else if(parameter.isAnnotationPresent(PathVariable.class)) {
				PathVariable pathVariable =  parameter.getAnnotation(PathVariable.class);
				String name = pathVariable.name();
				args.add(pathVariables.get(name));
			}else {
				args.add(null);
			}
		}
	
		Object returned = instanceAndMethod.invoke(args.toArray());
		return new HttpOutputMessage() {
			
			@Override
			public HttpStatus status() {
				return HttpStatus.OK;
			}
			
			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders h = new HttpHeaders();
				h.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
				return h;
			}
			
			@Override
			public InputStream fromStream() {
				String json = new GsonBuilder().create().toJson(returned);
				return new ByteArrayInputStream(json.getBytes());
			}
		};
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
