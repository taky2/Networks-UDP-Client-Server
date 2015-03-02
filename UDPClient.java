/**
 * This program runs on the client side to communicate with a server via UDP.
 * @author Dustin Fay
 */
import java.io.*;
import java.net.*;
import java.sql.Timestamp;

/**
 * UDPClient requests item inventory from a remote server.
 */
public class UDPClient {
	
	// UDP socket for client/server communication
	private DatagramSocket udpSocket;
	// Input from user command line
	private BufferedReader systemInput;
	// Messages befing sent from server and user
	private String msgFromServer, msgFromUser;
	// Address of the server to connect to
	private InetAddress address;
	// Message buffer for sending messages to server
	private byte[] bufferQuery;
	// udpPacketRequest is packet being sent to server 
	// udpPacketResponse is packet being received from server
	private DatagramPacket udpPacketRequest, udpPacketResponse;
	// The message buffer for receiving messages from server
	private byte[] bufferReply;
	// Times used to calculate elapsed time
	private Timestamp requestTime, responseTime;

	/**
	 * main method creates a new UDPClient.
	 */
	public static void main(String[] args) {
		new UDPClient();
	}

	/**
	 * Instantiates a new UDP client and prompts for a server address. 
	 */
	public UDPClient() {
		// Create an UDPSocket for client/server communication
		try {udpSocket = new DatagramSocket();} 
                catch (SocketException e) {
                    System.out.println("Unable to create DatagramSocket.");
                    e.printStackTrace();
                }
		systemInput = new BufferedReader(new InputStreamReader(System.in));
		msgFromServer = null;
		msgFromUser = null;
		// Ask user for Internet address of the server
		System.out.println("\nPlease input the DNS or IP of the machine on which the Server Program runs");
		try {address = InetAddress.getByName(systemInput.readLine());} 
                catch (UnknownHostException e) {e.printStackTrace();} 
                catch (IOException e) {e.printStackTrace();}
		// Send packet containting 'initialize' to let server know a new client is connecting
		bufferQuery = "initialize".getBytes();
		bufferReply = new byte[256];
		startClient();
	}

	/**
	 * Start client and allow user to make requests until they type 'exit'.
	 */
	public void startClient() {
                System.out.println("Connection started...\n");
                // Send initial request
		udpPacketRequest = new DatagramPacket(bufferQuery, bufferQuery.length, address, 5678);
		try {udpSocket.send(udpPacketRequest);} 
                catch (IOException e) {e.printStackTrace();}
		// Get initial inventory table
		udpPacketResponse = new DatagramPacket(bufferReply, bufferReply.length);
		try {udpSocket.receive(udpPacketResponse);} 
                catch (IOException e) {e.printStackTrace();}
		System.out.println("Items in inventory: ");
		System.out.println(new String(udpPacketResponse.getData(), 0, udpPacketResponse.getLength()));
		System.out.println("Please input an id to make a query or 'exit' to close connection");
		try {
                    while ((msgFromUser = systemInput.readLine()) != null) {
                        // send request for item details
                        bufferQuery = msgFromUser.getBytes();
                        udpPacketRequest = new DatagramPacket(bufferQuery, bufferQuery.length, address, 5678);
                        requestTime = new Timestamp(System.currentTimeMillis());
                        udpSocket.send(udpPacketRequest);
                        // get response
                        udpPacketResponse = new DatagramPacket(bufferReply, bufferReply.length);
                        udpSocket.receive(udpPacketResponse);
                        responseTime = new Timestamp(System.currentTimeMillis());
                        long elapsedTime = responseTime.getTime() - requestTime.getTime();
                        // display response
                        msgFromServer = new String(udpPacketResponse.getData(), 0, udpPacketResponse.getLength());
                        System.out.println("\nResponse: \n" + msgFromServer + "\nRTT of Query: " + elapsedTime + " msec\n");
                        // exit option for user
                        if (msgFromUser.equals("exit")) {
                            System.out.println("Connection closing...");
                            break;
                        }
                        System.out.println("Please input an id to make a query or 'exit' to close connection");
                    }//end while 
		} catch (IOException e) {e.printStackTrace();}
                udpSocket.close();
	}//end startClient
}//end UDPClient
