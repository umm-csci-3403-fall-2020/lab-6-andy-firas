package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("Got a request!");

			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();

			ConnectionThread connectionThread = new ConnectionThread(socket, input, output);
			Thread thread = new Thread(connectionThread);
			thread.start();
		}
	}

	public static class ConnectionThread implements Runnable {
		public Socket sock;
		public InputStream input;
		public OutputStream output;

		public ConnectionThread(Socket sock, InputStream input, OutputStream output)
				throws IOException, InterruptedException {
			this.sock = sock;
			this.input = input;
			this.output = output;
		}

		@Override
		public void run() {
			int inputByte;
			try {
				while ((inputByte = input.read()) != -1) {
					output.write(inputByte);
					output.flush();
				}
				sock.shutdownOutput();
			} catch (IOException ioe) {
				System.out.println("We caught an exception");
				System.out.println(ioe);
			}
		}
	}
}
