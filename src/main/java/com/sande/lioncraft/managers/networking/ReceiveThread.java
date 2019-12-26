package com.sande.lioncraft.managers.networking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

public class ReceiveThread extends Thread{

	private Logger log = Logger.getLogger(this.getClass());
	private DatagramSocket clientSocket;
	private byte[] recvbuffer=new byte[516];
	private DatagramPacket RDGP=new DatagramPacket(recvbuffer,recvbuffer.length);
	private boolean running=true;
	private LinkedList<byte[]> recvqueue=new LinkedList<>();
	
	public ReceiveThread(DatagramSocket clientSocket) 
	{
		this.clientSocket=clientSocket;
	}

	@Override
	public void run() {
		log.info("Start the receive thread");
		boolean endofrecord = false;
		try {
			while (running) {
				endofrecord = false;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while (!endofrecord) {

					clientSocket.receive(RDGP);
					recvbuffer = RDGP.getData();
					if (recvbuffer[0] == 0)
						endofrecord = true;
					baos.write(recvbuffer, 4, recvbuffer.length - 4);
				}
				recvqueue.add(baos.toByteArray());
				log.debug("Full record received, added to the queue, size is "+recvqueue.size());
				baos.close();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] getLastRecord()
	{
		log.debug("Record requested");
		try{
		byte[] record= recvqueue.removeLast();
		return record;
		}catch(NoSuchElementException e)
		{
			log.debug("No records in the queue");
			return null;
		}
		
		
	}

}
