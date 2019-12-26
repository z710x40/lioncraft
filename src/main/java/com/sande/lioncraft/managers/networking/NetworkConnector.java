package com.sande.lioncraft.managers.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UTFDataFormatException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;



public class NetworkConnector {

	private String hostname;
	private InetAddress inet;
	private int port = 2016;
	
	//private SocketChannel channel;			// channel naar de server
	private DatagramSocket clientSocket;	// For the UDP
	private SendThread sendThread;
	private ReceiveThread receiveThread;
	private Logger log = Logger.getLogger(this.getClass());
	
	private static NetworkConnector INSTANCE=new NetworkConnector();
	private int bytesRead;
	
	
	private boolean requestInProcessing=false;
	
	private NetworkConnector() {
		try {
			clientSocket = new DatagramSocket();
			//clientSocket.setSoTimeout(1);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Instance of the Network Connector created");
	}
	
	
	public static NetworkConnector getConnector()
	{
		
		return INSTANCE;
	}
	
	
	public boolean connect(String hostname,int port)
	{
		log.info("Connect to "+hostname+" at port "+port);
		this.hostname=hostname;
		this.port=port;
		
		try {
			inet=InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sendThread=new SendThread(clientSocket,inet,port);
		sendThread.start();
		
		receiveThread=new ReceiveThread(clientSocket);
		receiveThread.start();
		
		
		
		
		return true;
	}
	


	
	
	
	/**
	 * Write a request record to the server
	 * 
	 * @param request
	 * @return
	 */
	public int writeUDPRecord(RequestRecord request) {
		log.debug("Send record to the Server via UDP");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);		// Zet het request om in een bytearray
			oos.writeObject(request);
			byte[] inter = bos.toByteArray();

			sendThread.addByteArray(inter);			// Zet het op het netwerk
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1;
	}
	
	
	
	/**
	 * Get a list of Chunks from the lioncraft server
	 * @return
	 */
	public ChunkListRecord readChunkListRecord() {
		
		log.debug("Read chunklist from the network input queue");
		byte[] inBuffer = null;								// Maak een readbuffer
		int total=0;
		try {
			inBuffer=receiveThread.getLastRecord();
			if(inBuffer==null)return null;
			
			log.debug("Data available for chunklistrecord");
			ByteArrayInputStream bis = new ByteArrayInputStream(inBuffer);	// Maak een inputstream op basis van de outputstream
			ObjectInputStream ois = new ObjectInputStream(bis);							// Converteer deze naar object
			return (ChunkListRecord) ois.readObject();									// Lees het object in

		}  catch (ClassNotFoundException e) {
			log.info("ClassNotFoundException. Object size was "+total);
			e.printStackTrace();
			return null;
		} catch(UTFDataFormatException e){
			log.info("UTFDataFormatException. "+e.getMessage());
		    e.printStackTrace();
		    return null;
		}catch (StreamCorruptedException e) {
			log.info("StreamCorruptedException."+e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			log.info("IOException "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	
}
