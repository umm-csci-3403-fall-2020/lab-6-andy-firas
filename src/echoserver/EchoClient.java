package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException {
		Socket socket = new Socket("localhost", PORT_NUMBER);
		// InputStream socketInputStream = socket.getInputStream();
		// OutputStream socketOutputStream = socket.getOutputStream();

		InputReader reader = new InputReader(socket);
		OutputWriter writer = new OutputWriter(socket);
		ThreadReader treader = new ThreadReader(reader);
		ThreadWriter twriter = new ThreadWriter(writer);

		treader.start();
		twriter.start();

	}

	public class InputReader implements Runnable {
		InputStream input;
		Socket sock;

		public InputReader(Socket sock) throws IOException {
			this.sock = sock;
			this.input = sock.getInputStream();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int inputByte;
			try {
				while ((inputByte = input.read()) != -1) {
					System.out.write(inputByte);
					System.out.flush();
				}
				sock.close();
			} catch (IOException ioe) {
				System.out.println("We caught an exception");
				System.out.println(ioe);
			}
		}
	}

	public class OutputWriter implements Runnable {
		OutputStream output;
		Socket sock;

		public OutputWriter(Socket sock) throws IOException {
			this.sock = sock;
			this.output = sock.getOutputStream();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int outputByte;
			try {
				while ((outputByte = System.in.read()) != -1) {
					output.write(outputByte);
					output.flush();
				}
				sock.shutdownInput();
			} catch (IOException ioe) {
				System.out.println("We caught an exception");
				System.out.println(ioe);
			}
		}
	}
}
