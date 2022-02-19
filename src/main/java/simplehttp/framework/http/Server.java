package simplehttp.framework.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import simplehttp.framework.context.ApplicationContext;

public class Server implements Runnable {
	private final ServerSocket serverSocket;

	private ExecutorService pollRequest;
	
	private  ApplicationContext applicationContext;
	
	private List<Future<?>> incomingRequests;
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		pollRequest = Executors.newFixedThreadPool(10);
		incomingRequests = new ArrayList<Future<?>>();
	}

	public void run() {
		System.out.format("[Server] Application Started in port %s...\n",serverSocket.getLocalPort());
		while (true) {

			try {
				Socket socket = serverSocket.accept();
				if(socket == null) continue;
				IncomingRequest request = new IncomingRequest(socket,applicationContext.getFilterChain(),applicationContext.getController());
				Future<?> futureIncomingRequest = pollRequest.submit(request);
				incomingRequests.add(futureIncomingRequest);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public void setApplicationContext(ApplicationContext context) 
	{
		this.applicationContext   = context;
	}
}
