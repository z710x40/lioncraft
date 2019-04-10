package lioncraftserver.tools;

import java.util.ArrayList;
import java.util.List;

import com.sande.lioncraft.storage.ChunkStorage;

import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.ChunkListRecord;

public class Processors {

	ChunkStorage chunkStorage=ChunkStorage.getChunkStorage();
	
	public Processors() {
		// TODO Auto-generated constructor stub
	}

	
	
	
	public ChunkListRecord getChunksFromList(List<String> chunkIdList)
	{
		ChunkListRecord newList=new ChunkListRecord();
		chunkIdList.forEach(chunkId -> newList.list.add(chunkStorage.getChunk(chunkId)));
		return newList;
	}
	
}
