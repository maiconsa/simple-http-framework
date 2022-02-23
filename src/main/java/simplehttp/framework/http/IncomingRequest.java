package simplehttp.framework.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import simplehttp.framework.http.filter.FilterChain;
import simplehttp.framework.http.message.DefaultJsonExceptionOuputMessage;
import simplehttp.framework.http.message.HttpOutputMessage;

public class IncomingRequest implements Runnable {

	private Socket socket;

	private HttpRequest httpRequest;
	private HttpResponse httpResponse;

	private FilterChain filter;
	private List<Object> controllers;
	
	public IncomingRequest(Socket socket,FilterChain filter,List<Object> controllers) {
		this.socket = socket;
		this.filter = filter;
		this.controllers = controllers;
	}


	public void run() {
		try {
			httpRequest = new HttpRequest(new BufferedInputStream(socket.getInputStream()));
			httpResponse = new HttpResponse(socket.getOutputStream());
			
			filter.apply(httpRequest);
	
			HttpRequestControllerResolver requestResolver = new HttpRequestControllerResolver(httpRequest,this.controllers);	
			HttpOutputMessage output =  requestResolver.resolve();
			httpResponse.send(output);
		} catch (Exception e) {
			try {
				HttpOutputMessage outputMessage = new DefaultJsonExceptionOuputMessage(e);
				httpResponse.send(outputMessage);
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} finally {
			closeSocketConnection();
		}

	}

	private void closeSocketConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.format("Houve um problema ao fecha a conexão do socket: %s", e.getMessage());
		}
	}
}
