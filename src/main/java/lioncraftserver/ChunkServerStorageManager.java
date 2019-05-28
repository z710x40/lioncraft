package lioncraftserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;


public class ChunkServerStorageManager {

	private static ChunkServerStorageManager instance;
	Map<String,Chunk> chunkDB=new HashMap<>();
	
	private List<String> chunksToReqeust=new ArrayList<>();
	boolean runflag=true;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private ChunkServerStorageManager() {
		
	}
	
	public static ChunkServerStorageManager getChunkStorage()
	{
		if(instance==null)
		{
			instance=new ChunkServerStorageManager();
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
		
		String coords[]=chunkID.split("X");	// Haal de relatieve coordinaten uit het ID
		int x=Integer.parseInt(coords[0]);
		int z=Integer.parseInt(coords[1]);
		
		Chunk newChunk=new Chunk(x,z);
		//newChunk.RandomFill();
		
		add(newChunk);
		
		return chunkDB.get(chunkID);
	}
	
	
	
}
