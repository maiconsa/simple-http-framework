package simplehttp.framework.http.extract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

import com.google.gson.GsonBuilder;

import simplehttp.framework.http.HttpRequest;
import simplehttp.framework.http.annotations.request.Payload;
import simplehttp.framework.http.enums.MediaType;

public class PayloadValueExtract implements ExtractValue {

	@Override
	public Object getArgValue(HttpRequest request,Parameter parameter,Object ...others) {
		 String json = new String(request.getMessageBody());
	     return  new GsonBuilder().create().fromJson(json, parameter.getType());
	}

	@Override
	public List<MediaType> notPermited() {
		return List.of(MediaType.APPLICATION_JSON);
	}

	@Override
	public Class<? extends Annotation> getAnnotation() {
		return Payload.class;
	}
	
}
