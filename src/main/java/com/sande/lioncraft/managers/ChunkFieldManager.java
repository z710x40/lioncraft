package com.sande.lioncraft.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChunkFieldManager {

	private int fieldsize=9;
	
	private ArrayList<String> currentChunkFieldIDList=new ArrayList<>();	// Huidige lijst met chunks
	private ArrayList<String> chunksToReturn=new ArrayList<>();				// Chunks die objecten terug moeten geven
	private ArrayList<String> chunksToAdd=new ArrayList<>();				// Chunks die moeten worden toegevoegd
	private ArrayList<String> chunksToCollide=new ArrayList<>();			// Chunks die kunnen botsen
	private ArrayList<String> newChunkFieldIDList=new ArrayList<>();		// De nieuwe chunklist na het bereiken van een nieuwe chunk
	private Map<String,Integer> chunkFieldStatus=new HashMap<>();			// Lijst met de status van elke chunk	0=empty,1=build,2=
	
	
	public ChunkFieldManager() {
	}
	
	
	public ChunkFieldManager(int fieldsize) {
		this.fieldsize=fieldsize;
	}

	
	// Maak een nieuwe lijst met chunk vanuit een volledig nieuwe positie
	public void setNewCenterAt(int currentChunkX,int currentChunkZ)
	{
		currentChunkFieldIDList.clear();
		chunkFieldStatus.clear();
		for(int telx=-fieldsize;telx<fieldsize;telx++)
		{
			for(int telz=-fieldsize;telz<fieldsize;telz++)
			{
				String newChunkId=new StringBuilder().append(currentChunkX+telx).append('X').append(currentChunkZ+telz).toString();
				chunkFieldStatus.put(newChunkId, 0);
				currentChunkFieldIDList.add(newChunkId);
				
			}
		}
	}
	
	
	
	// Verplaats het chunkveld naar een nieuw centrum en bereken welke chunk er bij moeten komen en welke er af moeten
	public void moveCenterAt(int currentChunkX,int currentChunkZ)
	{
		// Maak de nieuwe lijst
		newChunkFieldIDList.clear();
		for(int telx=-fieldsize;telx<fieldsize;telx++)
		{
			for(int telz=-fieldsize;telz<fieldsize;telz++)
			{
				newChunkFieldIDList.add(new StringBuilder().append(currentChunkX+telx).append('X').append(currentChunkZ+telz).toString());
			}
		}
		
		// maak een lijst met chunks die er uit moeten
		chunksToReturn.clear();
		for(String currentChunk:currentChunkFieldIDList)
		{
			if(!newChunkFieldIDList.contains(currentChunk))
			{
				chunksToReturn.add(currentChunk);
				chunkFieldStatus.remove(currentChunk);
				
			}
		}
		
		// maak de lijst met chunks die er bij moeten
		chunksToAdd.clear();
		for (String newChunk : newChunkFieldIDList) {
			if (!currentChunkFieldIDList.contains(newChunk)) {
				chunksToAdd.add(newChunk);
				if(!chunkFieldStatus.containsKey(newChunk))
				{
					chunkFieldStatus.put(newChunk, 0);
				}
			}
		}
		
		// Maak de lijst met chunks die moeten kunnen botsen
		chunksToCollide.clear();
		for(int telx=-1;telx<2;telx++)
		{
			for(int telz=-1;telz<2;telz++)
			{
				chunksToCollide.add(new StringBuilder().append(currentChunkX+telx).append('X').append(currentChunkZ+telz).toString());
			}
		}
		
		// Update de current list
		currentChunkFieldIDList.clear();
		newChunkFieldIDList.forEach(fieldId -> currentChunkFieldIDList.add(fieldId));
	}
	
	
	public void setChunkAsBuild(String chunkId)
	{
		chunkFieldStatus.replace(chunkId, 1);
	}
	
	
	public ArrayList<String> getCurrentChunkIdList()
	{
		return currentChunkFieldIDList;
	}
	
	public ArrayList<String> getChunkIdToRemoveList()
	{
		return this.chunksToReturn;
	}
	
	public ArrayList<String> getChunkIdToAddList()
	{
		return this.chunksToAdd;
	}
	
	public ArrayList<String> getChunkIdToCollideList()
	{
		return this.chunksToCollide;
	}


	@Override
	public String toString() {
		return "ChunkFieldManager \n[currentChunkFieldIDList=" + currentChunkFieldIDList + ",\n chunksToReturn=         " + chunksToReturn + ",\n chunksToAdd=            " + chunksToAdd + ",\n chunksToCollide=        " + chunksToCollide + ",\n newChunkFieldIDList=    " + newChunkFieldIDList + "]";
	}

	
	public List<String> getChunksThatAreNotBuild()
	{
		List<String> tempList=new ArrayList<>();
		chunkFieldStatus.forEach((K,V) -> {if(V==0)tempList.add(K);});
		return tempList;
	}
	
}
