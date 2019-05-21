package com.sande.lioncraft.managers;

import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.RequestRecord;

public class NetworkManagerRunner extends Thread  {

	private NetworkConnector networkConnector;
	private boolean running=false;					// Flag if the thread is running
	private boolean finished=false;					// Flag if tread is ready
	private int id;									// Id of the runner
	private int requestNumber=4;					// Type request
	private RequestRecord rr=new RequestRecord();	// Reqeust record
	private int result;								// result parameter
	
		
	private Chunk chunk;							// Place to store the reqeusted chunk
	private String chunkId;							// chunkID
	
	public NetworkManagerRunner(int id) {
		this.id=id;
		this.networkConnector=NetworkConnector.getConnector();
	}


	public void run()
	{
		chunk=null;
		running=true;
		finished=false;
		rr.setRequesttype(requestNumber);
		rr.setChunkid(chunkId);
		networkConnector.writeRecord(rr);
		chunk=networkConnector.readChunkRecord();
		running=false;
		finished=true;
	}

	
	
	
	public boolean isRunning()
	{
		return running;
	}
	
	public boolean isFinished()
	{
		return finished;
	}
	


	public void setRequestRecord(RequestRecord rr) {
		this.rr = rr;
	}
	
	public Chunk getChunk()
	{
		finished=false;
		return chunk;
	}
	
	
}
