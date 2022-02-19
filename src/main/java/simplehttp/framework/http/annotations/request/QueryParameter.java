package simplehttp.framework.http.annotations.request;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

@Target(METHOD)
public @interface QueryParameter {
	String name();
}
