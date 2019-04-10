package com.sande.lioncraft.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;

public class NetworkConnector {

	String hostname;
	InetAddress inet;
	int port = 2016;
	
	SocketChannel channel;		// channel naar de server
	
	
	private static NetworkConnector INSTANCE;
	
	private NetworkConnector() {
		
		
	}
	
	
	public static NetworkConnector getConnector()
	{
		if(INSTANCE==null)
		{
			System.out.println("Instance of the Network Connector created");
			INSTANCE=new NetworkConnector();
		}
		return INSTANCE;
	}
	
	
	public boolean connect(String hostname,int port)
	{
		this.hostname=hostname;
		this.port=port;
		try {
			inet=InetAddress.getByName(hostname);
			InetSocketAddress socket = new InetSocketAddress(inet, port);
			channel = SocketChannel.open(socket);
			channel.configureBlocking(false);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.printf("Client connected to %s at port %d\n",hostname,port);
		return true;
	}
	
	/**
	 * Close the channel is a proper way
	 */
	public void close()
	{
		//System.out.println("Close channel");
		try {
			channel.close();
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Write a request record to the server
	 * @param request
	 * @return
	 */
	public int writeRecord(RequestRecord request) {
		//System.out.println("Write request");
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(request);
			return channel.write(ByteBuffer.wrap(bos.toByteArray()));
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
		//System.out.println("Read chunklist");
		ByteBuffer inBuffer = ByteBuffer.allocate(8192);								// Maak een readbuffer
		
		try {
			ByteArrayOutputStream baos=new ByteArrayOutputStream();						// Maak een outputstream
			while(timeOutRead(inBuffer,20)!=0)											// Loop zolang er data is
			{
				//System.out.println("readed bytes "+inBuffer.limit());
				baos.write(inBuffer.array());											// Schrijf de data naar de outputbuffer
				inBuffer.clear();														// clean de data
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());	// Maak een inputstream op basis van de outputstream
			ObjectInputStream ois = new ObjectInputStream(bis);							// Converteer deze naar object
			return (ChunkListRecord) ois.readObject();									// Lees het object in

		} catch (IOException e) {
			System.out.println("IOException "+e.getMessage());
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
			e.printStackTrace();
			return null;
		}
	}
	
	
	private int timeOutRead(ByteBuffer buffer,int timeout)
	{
		int rc=0;
		for(int tel=0;tel<timeout;tel++)
		{
			try {
				rc=channel.read(buffer);
				if(rc!=0)
				{
					return rc;
				}
				Thread.sleep(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.printf("Socket time out after %d milliseconds\n",timeout);
		return 0;
		
	}
	
}