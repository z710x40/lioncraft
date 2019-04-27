package lioncraftserver.tools;

import java.util.Random;

import com.sande.lioncraft.blockcase.Btype;
import com.sande.lioncraft.storage.ChunkStorage;

import lioncraftserver.comobjects.Block;
import lioncraftserver.comobjects.Chunk;

public class WorldGenerator {

	
	ChunkStorage chunkStorage=ChunkStorage.getChunkStorage();
	
	public WorldGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	
	// Create a full random world
	public void randomWorld()
	{
		Random random=new Random();
		
		int size=1000;
		int total=10000;
		
		
		System.out.println("Start generate Random Word");
		for(int tel=0;tel<total;tel++)
		{
			int x=random.nextInt(size)-500;
			int y=random.nextInt(20)+1;				// Niet te hoog
			int z=random.nextInt(size)-500;
			
			Block block=new Block(x,y,z,Btype.TESTBLOCK);
			
			Chunk chunk=chunkStorage.getChunk(Tools.getChunkId(x, z));
			chunk.addBlock(block);
		}
		System.out.println("Finished generate Random Word");
	}
	
	
	public void testWorld()
	{
		addBlock(0,1,0,Btype.TESTBLOCK);
		addBlock(0,1,2,Btype.BRICK);
		addBlock(0,1,4,Btype.APPLICATION);
		addBlock(0,1,8,Btype.MIDDLEWARE);
		addBlock(0,1,9,Btype.RACK);
		addBlock(0,1,11,Btype.SOFTWARE);
		addBlock(0,1,11,Btype.SIGNAL);
								
	}
	
	private void addBlock(int x, int y, int z,int blocktype)
	{
		Block block=new Block(x,y,z,blocktype);
		Chunk chunk=chunkStorage.getChunk(Tools.getChunkId(x, z));
		chunk.addBlock(block);
	}

}
