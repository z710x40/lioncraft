package com.sande.lioncraft.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sande.lioncraft.managers.networking.NetworkConnector;

import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;



/**
 * Manager for all the chunks
 * @author Erwin
 *
 */
public class ChunkStorageManager implements Runnable{

	private static ChunkStorageManager INSTANCE=new ChunkStorageManager();;
	Map<String,Chunk> chunkDB=new HashMap<>();							// De ChunkStorage
	NetworkConnector nwConnector =NetworkConnector.getConnector();
	private List<String> chunksToReqeust=new ArrayList<>();
	private Map<String,Integer> chunkRequestStatusList=new HashMap<>(); // 0=empty ,1=requested,2=done
	boolean runflag=true;
	
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private ChunkStorageManager() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public static ChunkStorageManager getChunkStorage()
	{
		return INSTANCE;
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
		if(chunksToReqeust.size()>2)
		{
			requestNewChunks();
			log.debug("Request new chunks from the server");
		}
		
		return chunkDB.get(chunkID);
	}
	
	
	private void requestNewChunks() {

		RequestRecord rr = new RequestRecord();
		rr.setRequesttype(1);
		rr.setChunkids(chunksToReqeust);
		log.debug("Request chunks " + chunksToReqeust);
		nwConnector.writeUDPRecord(rr);
		chunksToReqeust.clear();

	}

	@Override
	public void run() {

		log.info("Start ChunkStorage Thread");
		while (runflag)
		{
			ChunkListRecord chkRec=nwConnector.readChunkListRecord();	// Oke haal de lijst op
			if(chkRec!=null)										// Bevat de lijst gevens?
				{	
					for(Chunk chunk:chkRec.list)						// Ja, loop langs alle gegevens
					{
						chunkDB.put(chunk.getChunkid(), chunk);						// Voeg ze toe aan de database
						chunkRequestStatusList.remove(chunk.getChunkid());			// en haal ze weg uit de reqeust list
					}
					log.debug("New chunk recieved : "+ chkRec.list.size());
					log.debug("Chunkdb is "+chunkDB.size());
					
				}

		}
	}

	
	
	public void endThread() {
		runflag=false;
		
	}
	
	
	
	
}
