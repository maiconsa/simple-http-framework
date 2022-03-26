package simplehttp.framework.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import simplehttp.framework.http.Server;
import simplehttp.framework.http.annotations.request.Controller;
import simplehttp.framework.http.filter.ApplicationFilterChain;
import simplehttp.framework.http.filter.CheckAllowedMediaTypeFilter;
import simplehttp.framework.http.filter.FilterChain;

public class ApplicationContext {
	
	ApplicationFilterChain appFilter;
	
	private List<Object> controllers;
	
	private Properties properties;
	
	public ApplicationContext(String ...basePackages) throws Exception{
		appFilter = new ApplicationFilterChain(List.of(new CheckAllowedMediaTypeFilter()));
		this.properties = loadProperties();
		Server server = new Server(Integer.parseInt( (String)properties.getOrDefault("app.port", "8080")));
		server.setApplicationContext(this);
		loadClass(basePackages);
	 	new Thread( server).run();
	}
	
	
	public FilterChain getFilterChain() {
		
		return this.appFilter;
	}
	
	public List<Object> getController(){
		return this.controllers;
	};
	
	private  void loadClass(String[] basePackages ) throws Exception  {
		List<Class<?>> classes = new ArrayList<>();
		for (String packageName: basePackages) {
			classes.addAll(LoadClassUtils.loadClassFromPackge(packageName));
		}
		
		List<Object> tempControllersInstance = new ArrayList<>();
	     List<Class<?>> controllerClasses = classes.stream().filter(classe -> classe.isAnnotationPresent(Controller.class))
	    		 .distinct()
	    		 .collect(Collectors.toList());
	     for (Class<?> class1 : controllerClasses) {
	    	 tempControllersInstance.add(class1.getConstructors()[0].newInstance());
		}
	     
	     controllers = Collections.unmodifiableList(tempControllersInstance);

		
	}
	
	private Properties loadProperties() throws IOException {
		Properties properties = new Properties();
		InputStream is = 	getClass().getResourceAsStream("/application.properties");
		properties.load(is);
		return properties;
	}
	
	
	public static void init(String ...basePackages) throws Exception {
		 new ApplicationContext(basePackages);
	}
	
}
