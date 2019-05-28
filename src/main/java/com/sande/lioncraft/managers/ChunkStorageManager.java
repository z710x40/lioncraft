package com.sande.lioncraft.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;


public class ChunkStorageManager implements Runnable{

	private static ChunkStorageManager instance;
	Map<String,Chunk> chunkDB=new HashMap<>();
	NetworkConnector nwConnector =NetworkConnector.getConnector();
	private List<String> chunksToReqeust=new ArrayList<>();
	private Map<String,Integer> chunkRequestStatusList=new HashMap<>(); // 0=empty ,1=requested,
	boolean runflag=true;
	
	boolean readyToSend=true;
	boolean readyToReceive=false;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private ChunkStorageManager() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public static ChunkStorageManager getChunkStorage()
	{
		if(instance==null)
		{
			instance=new ChunkStorageManager();
		}
		
		return instance;
	}
	
	
	public void add(Chunk chunk)
	{
		chunkDB.put(chunk.getChunkid(), chunk);
	}
	
	
	public Chunk getChunk(String chunkID)
	{
		
		if(chunkDB.containsKey(chunkID))
		{
			return chunkDB.get(chunkID);
		}
		
		if(chunkRequestStatusList.containsKey(chunkID))
		{
			if(chunkRequestStatusList.get(chunkID)==1)return null;
		}
		
		chunksToReqeust.add(chunkID);
		chunkRequestStatusList.put(chunkID, 1);
		if(chunksToReqeust.size()>5)
		{
			requestNewChunks();
			log.info("Request new chunks from the server");
		}
		
		return chunkDB.get(chunkID);
	}
	
	
	private void requestNewChunks()
	{
		if(this.readyToSend)
		{
			RequestRecord rr=new RequestRecord();
			rr.setRequesttype(1);
			rr.setChunkids(chunksToReqeust);
			nwConnector.writeRecord(rr);
			chunksToReqeust.clear();
			readyToSend=false;
			readyToReceive=true;
		}
		
	}

	@Override
	public void run() {

		log.info("Start Thread");
		while (runflag)
		{
			if(readyToReceive)
			{
			ChunkListRecord chkRec=nwConnector.readChunkListRecord();
			
				if(chkRec!=null)
				{
					chkRec.list.forEach(chunk -> chunkDB.put(chunk.getChunkid(), chunk) );
					log.info("New chunk recieved : "+ chkRec.list.size());
					log.info("Chunkdb is "+chunkDB.size());
					readyToSend=true;
					readyToReceive=false;
				}
			}
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//log.info(chunkRequestStatusList);
		}
	}

	public void endThread() {
		runflag=false;
		
	}
	
	
	
	
}
