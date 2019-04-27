package lioncraftserver.tools;

import java.util.ArrayList;
import java.util.List;

import com.sande.lioncraft.storage.ChunkStorage;

import lioncraftserver.comobjects.Block;
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
	
	
	public void addNewBlock(String blockid,int blockType)
	{
		String xyz[]=blockid.split("X");
		int x=Integer.parseInt(xyz[0]);
		int y=Integer.parseInt(xyz[1]);
		int z=Integer.parseInt(xyz[2]);
		
		Chunk chunk=chunkStorage.getChunk(Tools.getChunkId(x, z));
		chunk.addBlock(new Block(x,y,z,blockType));
	}
}
