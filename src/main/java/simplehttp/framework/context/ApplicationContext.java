package simplehttp.framework.context;

import java.io.IOException;
import java.util.List;

import simplehttp.app.TestController;
import simplehttp.framework.http.Server;
import simplehttp.framework.http.filter.ApplicationFilterChain;
import simplehttp.framework.http.filter.CheckAllowedMediaTypeFilter;
import simplehttp.framework.http.filter.FilterChain;

public class ApplicationContext {
	
	ApplicationFilterChain appFilter;
	
	private List<Object> controller = List.of(new TestController());
	
	public ApplicationContext() throws IOException {
		appFilter = new ApplicationFilterChain(List.of(new CheckAllowedMediaTypeFilter()));
		
		Server server = new Server(9999);
		server.setApplicationContext(this);
	 	new Thread( server).run();
	}
	
	
	public FilterChain getFilterChain() {
		
		return this.appFilter;
	}
	
	public List<Object> getController(){
		return this.controller;
	};
	
	
	public static void init() throws IOException {
		new ApplicationContext();
	}
	
}
