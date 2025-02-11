package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
	private ServerSocket serverSocket;

	public static void main(String[] ignoredArgs) {
		Scanner scann = new Scanner(System.in);

		System.out.println("Enter the server port: ");
		int port = scann.nextInt();

		new Thread(() -> {
			Server server = new Server();

			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("INFO: Connection established on port " + port);
				server.startConnectionLoop(serverSocket);
			} catch (Exception e) {
				System.out.println("INFO: listening on the port " + port + "message: " + e.getLocalizedMessage());
			}
		}).start();
	}

	public void startConnectionLoop(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;

		while (serverSocket != null && !serverSocket.isClosed()) {
			try {
				new ServerThread(serverSocket.accept()).start();
			} catch (IOException e) {
				System.out.println("INFO: Socket did not accept the connection");
			}
		}
	}

	public void closeSocket() throws IOException {
		if(serverSocket == null || serverSocket.isClosed()) return;

		serverSocket.close();
	}
}