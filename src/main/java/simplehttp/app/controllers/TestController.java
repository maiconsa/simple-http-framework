package simplehttp.app.controllers;

import java.util.List;

import simplehttp.app.User;
import simplehttp.framework.http.HttpHeaders;
import simplehttp.framework.http.annotations.request.Controller;
import simplehttp.framework.http.annotations.request.Header;
import simplehttp.framework.http.annotations.request.PathVariable;
import simplehttp.framework.http.annotations.request.Payload;
import simplehttp.framework.http.annotations.request.ResponseStatus;
import simplehttp.framework.http.annotations.request.mapping.Do;
import simplehttp.framework.http.enums.HttpMethod;
import simplehttp.framework.http.enums.HttpStatus;
import simplehttp.framework.http.enums.MediaType;

@Controller(path = "/user/{userId}/")	
public class TestController {
	
	public TestController() {
	}
	
	@Do(method = HttpMethod.POST, contentType  = MediaType.APPLICATION_JSON, path = "/image/{imageId}")
	@ResponseStatus(status = HttpStatus.OK)
	public User post(@Payload User user ,
			@PathVariable(name = "userId") String userId,
			@PathVariable(name = "imageId") String imageId ) {
		return new User(1L, user.getName(), user.getPassword());
	}
	
	@Do(method = HttpMethod.GET, contentType  = MediaType.APPLICATION_JSON, path = "/image")
	@ResponseStatus(status = HttpStatus.OK)
	public List<User> get(
			@PathVariable(name = "userId") String userId,
			@Header(name = HttpHeaders.CONTENT_LEGNTH) String length,
			@Header(name = HttpHeaders.CONTENT_TYPE) String contentType
			) {
		return List.of(
				new User(1L, "maicon", "123"), 
				new User(1L, "12312", "123"));
	}
}
