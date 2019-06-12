package com.sande.lioncraft.managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;



public class NetworkConnector {

	String hostname;
	InetAddress inet;
	int port = 2016;
	
	SocketChannel channel;		// channel naar de server
	
	ByteBuffer inBuffer = ByteBuffer.allocate(8192);								// Maak een readbuffer
	private Logger log = Logger.getLogger(this.getClass());
	
	private static NetworkConnector INSTANCE;
	int bytesRead;
	
	
	
	boolean requestInProcessing=false;
	
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
		log.info("Client connected to "+hostname+" at port "+port);
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
		log.debug("Write request");
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(request);
			byte[] inter=bos.toByteArray();
			ByteBuffer interBuf=ByteBuffer.wrap(inter);
			return channel.write(interBuf);
		} catch (IOException e) {
			log.debug("IOException "+e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}
	
	
	
	/**
	 * Get a list of Chunks from the lioncraft server
	 * @return
	 */
	public ChunkListRecord readChunkListRecord() {
		
		//log.debug("Read chunklist from the network");
		ByteBuffer inBuffer = ByteBuffer.allocate(8192);								// Maak een readbuffer
		int total=0;
		try {
			ByteArrayOutputStream baos=new ByteArrayOutputStream();						// Maak een outputstream
			while(timeOutRead(inBuffer,10)>0)											// Loop zolang er data is
			{
				inBuffer.flip();
				log.info("readed bytes "+inBuffer.limit());
				total+=inBuffer.limit();
				baos.write(inBuffer.array());											// Schrijf de data naar de outputbuffer
				inBuffer.clear();														// clean de data
			}
			log.info("total readed bytes is "+total);
			if(total==0)return null;
			
			ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());	// Maak een inputstream op basis van de outputstream
			ObjectInputStream ois = new ObjectInputStream(bis);							// Converteer deze naar object
			return (ChunkListRecord) ois.readObject();									// Lees het object in

		}  catch (ClassNotFoundException e) {
			log.info("ClassNotFoundException. Object size was "+total);
			e.printStackTrace();
			return null;
		} catch (StreamCorruptedException e) {
			log.info("StreamCorruptedException."+e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			log.info("IOException "+e.getMessage());
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
					log.info("Bytest read is "+rc);
					return rc;
				}
				if(rc==-1)
				{
					log.info("End of stream");
					return 0;	
				}
				Thread.sleep(1);
			} catch (IOException e) {
				log.debug("IOException "+e.getMessage());
				e.printStackTrace();
			} catch (InterruptedException e) {
				log.debug("InterruptedException "+e.getMessage());
				e.printStackTrace();
			}
		}
		log.info("Socket time out after "+timeout+" milliseconds");
		return 0;
		
	}
	
	
	
	
}
