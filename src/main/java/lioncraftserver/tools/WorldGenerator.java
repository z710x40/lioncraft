package lioncraftserver.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import com.sande.lioncraft.blockcase.BlockType;
import com.sande.lioncraft.managers.ChunkStorageManager;

import lioncraftserver.ChunkServerStorageManager;
import lioncraftserver.comobjects.Block;
import lioncraftserver.comobjects.Chunk;

public class WorldGenerator {

	
	ChunkServerStorageManager chunkServerStorage=ChunkServerStorageManager.getChunkStorage();
	
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
			
			Block block=new Block(x,y,z,BlockType.TESTBLOCK.getID());
			
			Chunk chunk=chunkServerStorage.getChunk(Tools.getChunkId(x, z));
			chunk.addBlock(block);
		}
		System.out.println("Finished generate Random Word");
	}
	
	
	public void testWorld()
	{
		addBlock(0,1,0,BlockType.TESTBLOCK.getID());
		addBlock(0,1,2,BlockType.BRICKBLOCK.getID());
		addBlock(0,1,4,BlockType.LETTERBLOCK.getID(),"zoek het ff uit");
		
		addBlock(0,1,6,BlockType.LETTERBLOCK.getID(),"en dit is een test");
	
		for(int x=1;x<200;x++)
		{
			addBlock(0,1,x,BlockType.BRICKBLOCK.getID());
			addBlock(0,2,x,BlockType.BRICKBLOCK.getID());
			addBlock(0,3,x,BlockType.BRICKBLOCK.getID());
			addBlock(0,4,x,BlockType.BRICKBLOCK.getID());
			addBlock(0,5,x,BlockType.BRICKBLOCK.getID());
			
			addBlock(10,1,x+10,BlockType.LETTERBLOCK.getID(),"en dit is een test");
			addBlock(10,2,x+10,BlockType.LETTERBLOCK.getID(),"en dit is een test");
			addBlock(10,3,x+10,BlockType.LETTERBLOCK.getID(),"en dit is een test");
			addBlock(10,4,x+10,BlockType.LETTERBLOCK.getID(),"en dit is een test");
			addBlock(10,5,x+10,BlockType.LETTERBLOCK.getID(),"en dit is een test");
		}
								
	}
	
	
	
public void WorldFromCSV(String csvfilename) {
		
		int maxrow=20;
		int step=10;
		int x=10;
		int z=10;
		
		File csvfile=new File(csvfilename);
		try {
			Scanner csvscan = new Scanner(csvfile);
			while(csvscan.hasNextLine())
			{
				String line=csvscan.nextLine();
				// System.out.println(line);
				String[] fields=line.split(",");
				System.out.println(fields[2]);
				
				addBlock(x,1,z,BlockType.SERVERBLOCK.getID());
				addBlock(x,2,z,BlockType.SERVERBLOCK.getID());
				if(line.toLowerCase().contains("linux"))
						{addBlock(x,3,z,BlockType.LINUXBLOCK.getID());
						}
				else
				{
					addBlock(x,3,z,BlockType.SERVERBLOCK.getID());
				}
					
				
				addBlock(x,4,z,BlockType.LETTERBLOCK.getID(),fields[2]);
				
				x=x+step;
				maxrow--;
				if(maxrow<1)
					{
					 maxrow=20;
					 z=z+step;
					 x=0;
					}
			}
			csvscan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private void addBlock(int x, int y, int z,int blocktype)
	{
		Block block=new Block(x,y,z,blocktype);
		Chunk chunk=chunkServerStorage.getChunk(Tools.getChunkId(x, z));
		chunk.addBlock(block);
	}
	
	
	private void addBlock(int x, int y, int z,int blocktype,String text)
	{
		Block block=new Block(x,y,z,blocktype);
		block.setText(text);
		Chunk chunk=chunkServerStorage.getChunk(Tools.getChunkId(x, z));
		chunk.addBlock(block);
	}
	

}
