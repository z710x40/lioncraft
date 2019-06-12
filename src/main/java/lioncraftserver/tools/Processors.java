package lioncraftserver.tools;


import java.util.List;
import org.apache.log4j.Logger;
import lioncraftserver.ChunkServerStorageManager;
import lioncraftserver.comobjects.Block;
import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.ChunkListRecord;


public class Processors {

	ChunkServerStorageManager chunkServerStorage=ChunkServerStorageManager.getChunkStorage();
	
	private Logger log = Logger.getLogger(this.getClass());
	
	public Processors() {
		// TODO Auto-generated constructor stub
	}

	
	public Chunk getSingleChunk(String chunkId)
	{
		Chunk temp=chunkServerStorage.getChunk(chunkId);
		return temp;
	}
	
	
	public ChunkListRecord getChunksFromList(List<String> chunkIdList)
	{
		log.debug("Get a list of chunks from the list of chunkID's");
		ChunkListRecord newList=new ChunkListRecord();
		chunkIdList.forEach(chunkId -> newList.list.add(chunkServerStorage.getChunk(chunkId)));
		log.debug("Number of chunks is "+newList.list.size());
		return newList;
	}
	
	
	public void addNewBlock(String blockid,int blockType)
	{
		String xyz[]=blockid.split("X");
		int x=Integer.parseInt(xyz[0]);
		int y=Integer.parseInt(xyz[1]);
		int z=Integer.parseInt(xyz[2]);
		
		Chunk chunk=chunkServerStorage.getChunk(Tools.getChunkId(x, z));
		chunk.addBlock(new Block(x,y,z,blockType));
	}




	public void delBlock(String blockid,String chunkid) {
		
		Chunk chunk=chunkServerStorage.getChunk(chunkid);
		chunk.delBlock(blockid);
	}
}
