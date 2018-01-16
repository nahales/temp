import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class Server extends Application {
  // Text area for displaying contents
  private TextArea ta = new TextArea();

  // Mapping of sockets to output streams
  private Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();

  // Server socket
  private ServerSocket serverSocket;

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    ta.setWrapText(true);
	ta.setPrefSize(697, 347);
   
    // Create a scene and place it in the stage
    Scene scene = new Scene(new ScrollPane(ta), 700, 350);
    primaryStage.setTitle("Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    
    new Thread(() -> listen()).start();
  }
      
  private void listen() {
      try {
        // Create a server socket
        serverSocket = new ServerSocket(8000);
        Platform.runLater(() -> 
          ta.appendText("MultiThreadServer started at " + new Date() + '\n'));

      while (true) {
        // Listen for a new connection request
        Socket socket = serverSocket.accept();

        // Display the client number
        Platform.runLater(() -> 
          ta.appendText("Connection from " + socket + " at " + new Date() + '\n'));

        // Create output stream
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

        // Save output stream to hashtable
        outputStreams.put(socket, dout);

        // Create a new thread for the connection
        new ServerThread(this, socket);
      }
    }
    catch(IOException ex) {
      ex.printStackTrace();
    }
  }

  // Used to get the output streams
  Enumeration getOutputStreams(){
      return outputStreams.elements();
  }

  // Used to send message to all clients
  void sendToAll(String message){
      // Go through hashtable and send message to each output stream
      for (Enumeration e = getOutputStreams(); e.hasMoreElements();){
          DataOutputStream dout = (DataOutputStream)e.nextElement();
          try {
              // Write message
              dout.writeUTF(message);
          } catch (IOException ex) {
              ex.printStackTrace();
          }
      }
  }

  class ServerThread extends Thread {
      private Server server;
      private Socket socket;

      /** Construct a thread */
      public ServerThread(Server server, Socket socket) {
          this.socket = socket;
          this.server = server;
          start();
      }

      /** Run a thread */
      public void run() {
          try {
              // Create data input and output streams
              DataInputStream din = new DataInputStream(socket.getInputStream());
							
              // Continuously serve the client
              while (true) {
                  String string = din.readUTF();
				
                  // Send text back to the clients
                  server.sendToAll(string);

                  // Add chat to the server text area
                  ta.appendText(string + '\n');
              }
          }
          catch(IOException ex) {
			  ta.appendText("User from " + socket + " disconnected at " + new Date() + '\n');
              System.out.println("Disconnected from " + socket + " at " + new Date());
          }
      }
  }
  
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
