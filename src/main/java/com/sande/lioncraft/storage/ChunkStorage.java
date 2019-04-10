package com.sande.lioncraft.storage;

import java.util.HashMap;
import java.util.Map;


import com.sande.lioncraft.network.NetworkConnector;

import lioncraftserver.comobjects.Chunk;



public class ChunkStorage {

	
	private static ChunkStorage instance;
	
	Map<String,Chunk> chunkDB=new HashMap<>();
	
	private ChunkStorage() {
		//nwConnector=NetworkConnector.getConnector();	// Init de connector
	}
	
	public static ChunkStorage getChunkStorage()
	{
		if(instance==null)
		{
			instance=new ChunkStorage();
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
		
		//System.out.println("Nieuwe chunk "+chunkID);
		String coords[]=chunkID.split("X");	// Haal de relatieve coordinaten uit het ID
		int x=Integer.parseInt(coords[0]);
		int z=Integer.parseInt(coords[1]);
		
		Chunk newChunk=new Chunk(x,z);
		//newChunk.RandomFill();
		
		add(newChunk);
		
		return chunkDB.get(chunkID);
	}
}
