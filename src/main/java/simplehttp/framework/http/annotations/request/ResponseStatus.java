package simplehttp.framework.http.annotations.request;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import simplehttp.framework.http.enums.HttpStatus;

@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseStatus {
	HttpStatus status();
}
