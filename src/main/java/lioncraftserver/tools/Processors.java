package lioncraftserver.tools;


import java.util.List;

import com.sande.lioncraft.managers.ChunkStorageManager;

import lioncraftserver.comobjects.Block;
import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.ChunkListRecord;

public class Processors {

	ChunkStorageManager chunkStorage=ChunkStorageManager.getChunkStorage();
	
	
	public Processors() {
		// TODO Auto-generated constructor stub
	}

	
	public Chunk getSingleChunk(String chunkId)
	{
		return chunkStorage.getChunk(chunkId);
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




	public void delBlock(String blockid,String chunkid) {
		
		Chunk chunk=chunkStorage.getChunk(chunkid);
		chunk.delBlock(blockid);
	}
}
