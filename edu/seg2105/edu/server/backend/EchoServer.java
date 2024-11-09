package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  String loginCode = "loginID";
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
	    // Retrieve the login ID of the client
	    String loginID = (String) client.getInfo(loginCode); 

	    String message = (String) msg;

	    if (message.startsWith("#login")) {
	        String[] messageParts = message.split(" ");
	        
	        if (messageParts.length > 1) {
	            String newLoginID = messageParts[1];
	            System.out.println("Message received: " + message + " from null.");
	            
	            client.setInfo(loginCode, newLoginID); 
	            System.out.println(newLoginID + " has logged on.");
	            sendToAllClients(newLoginID + " has logged on.");
	        } else {
	            System.out.println("ERROR!! No login ID was provided.");
	            try {
	                client.close(); // Close the connection if no login ID was provided
	            } catch (IOException e) {
	                System.out.println("Error closing connection: " + e.getMessage());
	            }
	        }
	    } else {
	        if (loginID != null) {
	            System.out.println("Message received: " + message + " from " + loginID);
	            sendToAllClients(loginID + "> " + message);
	        }
	    }
	}


    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = DEFAULT_PORT; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    ServerConsole serverConsole = new ServerConsole(sv);
    
    try 
    {
      sv.listen(); //Start listening for connections
      serverConsole.accept(); // Start accepting input from server console
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  /**
	 * Implemented the hook method called each time a new client connection is
	 * accepted. The default implementation does nothing.
	 * @param client the connection connected to the client.
	 */
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("A client has connected.");
	}

	/**
	 * Implemented the hook method called each time a client disconnects.
	 * The default implementation does nothing. The method
	 * may be overridden by subclasses but should remains synchronized.
	 *
	 * @param client the connection with the client.
	 */
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		String loginID = (String) client.getInfo(loginCode);
		
		if (loginID != null) {
			System.out.println(loginID + " has disconnected.");
		} else {
			System.out.println("A client has disconnected.");
		}
	}
	
	/**
	 * Implemented the hook method called each time an exception is thrown in a
	 * ConnectionToClient thread.
	 * The method may be overridden by subclasses but should remains
	 * synchronized.
	 *
	 * @param client the client that raised the exception.
	 * @param Throwable the exception thrown.
	 */
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		String loginID = (String) client.getInfo(loginCode);
		
		if (loginID != null) {
			System.out.println(loginID + " has disconnected.");
		} else {
			System.out.println("A client has disconnected.");
		}
	}
}
//End of EchoServer class
