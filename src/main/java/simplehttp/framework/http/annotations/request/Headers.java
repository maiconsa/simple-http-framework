package simplehttp.framework.http.annotations.request;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

@Target(METHOD)
public @interface Headers {
	String name() ;
	String[] only();
	boolean all() default true;
}
