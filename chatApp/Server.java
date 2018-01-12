import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
	/* Static variable is a variable which belongs to the class and not to object (instance) - No encapsulation */
	/* Final variables are initialized only once, at the time of declaration and connot be reassigned later */
	public static final int PORT = 8000;

	public static void main(String[] args) throws IOException {
		new Server().runServer();
	}

	public void runServer() throws IOException {
		Date date = new Date();
		/** Create a server socket */
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("MultiThreadServer started at " + date);
		/** Loop for accepting unlimited number of clients */
		while (true) {
			/** Listen and accept client socket */
			Socket socket = serverSocket.accept();
			/* Thread.start() is required to actually create a new thread
			 so that the runnable's run method is executed in parallel*/
			/** Start a new thread and calls the run() method for the accepted socket */
			new ServerThread(socket).start();
		}
	}

	public class ServerThread  extends Thread {
		Socket socket;
		/** Construct a thread */
		ServerThread(Socket socket) {
			this.socket = socket;
		}
		/** Implement run() method which is the entry point of the new thread */
		public void run() {
			try {
				System.out.println("Connection from " + socket + " at " + new Date());
				BufferedReader input = new BufferedReader (new InputStreamReader (socket.getInputStream()));
				while (true) {
					String clientInput = input.readLine();
					String[] inputArr = clientInput.split(": ", 2);
					String username = inputArr[0].toString();
					String message = inputArr[1].toString();
					if(message.equals("exit")) {
						System.out.println("User '" + username +"' from " + socket + " disconnected at " + new Date());
						break;
					}
					System.out.println(clientInput);
				}
				socket.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
