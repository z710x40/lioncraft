package com.sande.lioncraft.managers.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class SendThread extends Thread{

	private Logger log = Logger.getLogger(this.getClass());
	private DatagramSocket clientSocket;
	private DatagramPacket sendPacket;
	private InetAddress addr;
	private int port;
	private boolean running=true;
	byte[] sendbuffer=new byte[516];
	
	private LinkedList<byte[]> sendqueue=new LinkedList<>();
	
	public SendThread(DatagramSocket clientSocket,InetAddress addr, int port) 
	{
		this.clientSocket=clientSocket;
		this.addr=addr;
		this.port=port;
		
		sendPacket = new DatagramPacket(sendbuffer, sendbuffer.length, addr, port);
	}

	
	public void stopThread()
	{
		running=false;
	}
	
	
	public void addByteArray(byte[] byteArray)
	{
		sendqueue.add(byteArray);
		log.debug("Record added queue size is "+sendqueue.size());
	}
	
	
	@Override
	public void run() {

		log.info("Start the send thread");
		while(running)
		{//log.info("queue size is "+sendqueue.size());
			if(sendqueue.size()>0)
			{
				log.debug("New data in the queue, send it");
				byte[] record=sendqueue.removeLast();
				send(record);
			}
			try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		log.info("Send thread ended");
	}


	private void send(byte[] record) {
		log.debug("Send record size is "+record.length);
		ByteArrayInputStream bais=new ByteArrayInputStream(record);
		
		int readedBytes=0;
		boolean loopflag=true;
		while(loopflag)
		{
			readedBytes=bais.read(sendbuffer, 4, 512);
			log.debug("Bytes read is "+readedBytes);
			if(readedBytes<512)
			{	
				sendbuffer[0]=0;
				loopflag=false;
			}
			else{
			sendbuffer[0]=1;
			}
			
			sendPacket.setData(sendbuffer);
			sendPacket.setLength(sendbuffer.length);
			
			
			try {
				clientSocket.send(sendPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return;
	}


	

	
	
	
}
