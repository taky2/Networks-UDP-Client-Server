/**
 * This program is ran on the server side to communicate with a client via UDP.
 * 
 * Required file(s): Inventory.java
 * 
 * @author Dustin Fay
 */
import java.io.*;
import java.net.*;

/**
 * The Class UDPServer responds to client requests with inventory information.
 */
public class UDPServer {
	
	// The resource list of computers.
	private static Inventory inventoryList;
	// The udp server socket.
	private DatagramSocket udpServerSocket;
	// udpPacketIN is the packet received from client
	// udpPacketOUT is the packet sent to the server 
	private DatagramPacket udpPacketIN, udpPacketOUT;
	// The messages sent to and received from the client 
	private String msgFromClient, msgToClient;
	// Maintain connection?
	private boolean morePackets;
	// Buffer for receiving messages
	private byte[] buffIn;
	// Buffer for sending messages
	private byte[] buffOut;

	/**
	 * The main method creates a new UDP server.
	 */
	public static void main(String[] args) {
		new UDPServer();
	}

	/**
	 * Instantiates a new UDP server and initializes variables.
	 */
	public UDPServer() {
		inventoryList = new Inventory();
                morePackets = true;
		udpPacketIN = null;
		udpPacketOUT = null;
		msgFromClient = null;
		msgToClient = null;
		buffIn = new byte[256];
		buffOut = new byte[256];

		try {
			udpServerSocket = new DatagramSocket(5678);
		} catch (SocketException e1) {
			System.out.println("Unable to create DatagramSocket.");
			e1.printStackTrace();
		}
		startServer();
	}

	public void startServer() {
                System.out.println("\nServer started...");
                System.out.println("Press 'control+c' at any time to close the server\n");

		while (morePackets) {
                    try {
			// Receive UDP packet from client
			udpPacketIN = new DatagramPacket(buffIn, buffIn.length);
			udpServerSocket.receive(udpPacketIN);
			msgFromClient = new String(udpPacketIN.getData(), 0, udpPacketIN.getLength());
			System.out.println("New request from " + udpPacketIN.getSocketAddress() +": '" + msgFromClient +"' \n");
			// Use client msg to decide what to do
			if (msgFromClient.equals("initialize")) {
                            System.out.println("Client: " + udpPacketIN.getSocketAddress() + " connected.\n");
                            msgToClient = inventoryList.toString();
			} else if (msgFromClient.equals("exit")) {
                            System.out.println("Client: " + udpPacketIN.getSocketAddress() + " disconnected.\n");
			} else { // compare client input against item id's in Inventory
                            msgToClient = inventoryList.getItem(msgFromClient);
                            System.out.println("Response to "+ udpPacketIN.getSocketAddress() +": \n"+ msgToClient +" \n\n");
			}
			// send the response to the client at address and port
			InetAddress address = udpPacketIN.getAddress();
			int port = udpPacketIN.getPort();
			buffOut = msgToClient.getBytes();
			udpPacketOUT = new DatagramPacket(buffOut, buffOut.length, address, port);
			udpServerSocket.send(udpPacketOUT);
                    } catch (IOException e) {
                    e.printStackTrace();
                    morePackets = false;
                    }
                }//end while
		udpServerSocket.close();
	}//end startServer
}//end UDPServer

