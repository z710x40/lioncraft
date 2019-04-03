package com.sande.lioncraft.dbconnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.BlockType;
import com.sande.lioncraft.landscape.OpenSimplexNoise;

public class DbConnector {

	
	private final int WIDTH = 100;
	private final int HEIGHT = 100;
	private final double FEATURE_SIZE = 24;
	private final float chunksize;
	
	private Map<String,Block> blockdb=new HashMap<>();
	
	
	public DbConnector(float chunckmeasure) {
		this.chunksize=chunckmeasure;
	}

	

	public List<Block> getChunk(String chunkID)
	{
		List<Block> tempList=new ArrayList<>();
		blockdb.forEach( (key,Val) -> {if(Val.getChunkID().equals(chunkID))tempList.add(Val); });
		return tempList;
	}
	
	

	
	public void makeWorld() {

		/*OpenSimplexNoise noise = new OpenSimplexNoise();
		for (int z = 0; z < HEIGHT; z++)
		{
			for (int x = 0; x < WIDTH; x++)
			{
				double value = noise.eval(x / FEATURE_SIZE, z / FEATURE_SIZE, 0.0);
				int y=1+(int)(value*value*12);
				System.out.print("("+y+") ");
				String ID=new StringBuilder().append(x).append('X').append(y).append('X').append(z).toString();		// Maak het blockID
				
				int chunkPosX= (int)((x + Globals.CHUNKRADIUS) / (Globals.CHUNKSIZE) );
				if(x+ Globals.CHUNKRADIUS<0)chunkPosX--;
				int chunkPosZ= (int)((z + Globals.CHUNKRADIUS)  / (Globals.CHUNKSIZE) );
				if(z+ Globals.CHUNKRADIUS<0)chunkPosZ--;
				String ChunkID=new StringBuilder().append(chunkPosX).append('X').append(chunkPosZ).toString();
				
				blockdb.put(ID,new Block(ChunkID,x,y,z,BlockType.TESTBLOCK));
			}
			System.out.print(System.lineSeparator());
		} */
		
		
		for(int z=0;z<100;z++)
		{
			int x=0;
			String ID=new StringBuilder().append(x).append('X').append(1).append('X').append(z).toString();		// Maak het blockID
			
			int chunkPosX= (int)((x*Globals.BLOCKSIZE + Globals.CHUNKRADIUS) / (Globals.CHUNKSIZE) );
			if(x+ Globals.CHUNKRADIUS<0)chunkPosX--;
			int chunkPosZ= (int)((z*Globals.BLOCKSIZE + Globals.CHUNKRADIUS)  / (Globals.CHUNKSIZE) );
			if(z+ Globals.CHUNKRADIUS<0)chunkPosZ--;
			String ChunkID=new StringBuilder().append(chunkPosX).append('X').append(chunkPosZ).toString();
		
			
			blockdb.put(ID,new Block(ChunkID,x,1,z,BlockType.TESTBLOCK));
		}
		
		for(int x=0;x<100;x++)
		
		{
			int z=0;
			String ID=new StringBuilder().append(x).append('X').append(1).append('X').append(z).toString();		// Maak het blockID
			
			int chunkPosX= (int)((x*Globals.BLOCKSIZE + Globals.CHUNKRADIUS) / (Globals.CHUNKSIZE) );
			if(x+ Globals.CHUNKRADIUS<0)chunkPosX--;
			int chunkPosZ= (int)((z*Globals.BLOCKSIZE + Globals.CHUNKRADIUS)  / (Globals.CHUNKSIZE) );
			if(z+ Globals.CHUNKRADIUS<0)chunkPosZ--;
			
			String ChunkID=new StringBuilder().append(chunkPosX).append('X').append(chunkPosZ).toString();
			
			
			blockdb.put(ID,new Block(ChunkID,x,1,z,BlockType.LETTERBLOCK));
		}
		
		
		
		
	}
	
	
	
	
}
