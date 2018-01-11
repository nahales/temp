import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.print("Enter your name: ");
		Scanner sc = new Scanner(System.in);
    String name = sc.nextLine();
		/** InputStreamReader converts byte streams to character streams */
		/** BufferedReader class reads text from a character-input stream, buffering characters
		so as to provide for the efficient reading of characters, arrays, and lines */
		// wrap the input stream with a BufferedReader constructor
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		/** Class PrintWriter Prints formatted representations of objects to a text-output stream */
		// wrap the output stream with a PrintWriter constructor
		// true as the second argument specifies that we want to automatically flush the output stream
		PrintWriter output = new PrintWriter(getSocket().getOutputStream(), true);
		while (true) {
			System.out.print("Enter Text: ");
			/** readLine() is a member of the BufferedReader class that reads a string from the keyboard */
			String message = input.readLine();
			output.println(name + ": " + message);
		if(message.equals("exit")) {
			System.out.println("Exiting from server...");
			break;
		}
	}
}

	public static Socket getSocket() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 8000);
		return socket;
	}
}
