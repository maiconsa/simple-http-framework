package simplehttp.framework.http.annotations.request.mapping;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import simplehttp.framework.http.enums.HttpMethod;
import simplehttp.framework.http.enums.MediaType;

@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Do  {
    public String path() default "";
    public MediaType contentType() default MediaType.APPLICATION_JSON; 
    public HttpMethod method() ;
}
