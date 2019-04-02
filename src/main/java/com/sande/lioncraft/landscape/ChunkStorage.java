package com.sande.lioncraft.landscape;

import java.util.HashMap;
import java.util.Map;

import com.sande.lioncraft.storage.Chunk;


public class ChunkStorage {

	
	Map<String,Chunk> chunkDB=new HashMap<>();
	
	public ChunkStorage() {
		
	}
	
	
	public void add(Chunk chunk)
	{
		chunkDB.put(chunk.getChunkid(), chunk);
	}
	
	
	public Chunk getChunk(String chunkID)
	{
		
		if(chunkDB.containsKey(chunkID))
		{
			//System.out.println("Bestaande chunk "+chunkID);
			return chunkDB.get(chunkID);
		}
		
		//System.out.println("Nieuwe chunk "+chunkID);
		String coords[]=chunkID.split("X");	// Haal de relatieve coordinaten uit het ID
		int x=Integer.parseInt(coords[0]);
		int z=Integer.parseInt(coords[1]);
		
		Chunk newChunk=new Chunk(x,z);
		newChunk.RandomFill();
		
		add(newChunk);
		
		return chunkDB.get(chunkID);
	}
}
