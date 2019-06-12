package com.sande.lioncraft.managers;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.BlockType;
import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.RequestRecord;
import lioncraftserver.tools.Tools;


// Deze class verzorgt de zichtbare chunks
public class VisibleChunkField {

	
	int fieldPosX;			// De positie van de camera gemeten in chunks
	int fieldPosZ;
	
	int currentChunkX=1;		// Het huidige centrale chuckField
	int currentChunkZ=1;
	
	ChunkStorageManager chunkcStorage=ChunkStorageManager.getChunkStorage();	// De centrale chunk database
	GraphicsManager graphicsManager=GraphicsManager.getInstance(Globals.rootNode);
	//NetworkConnector nwConnector;
	BlockManager blockManager=BlockManager.GetBlockManager();
	Node rootNode;
	
	
	//OrderManager orderManager =OrderManager.getInstance();
	
	List<Chunk> chunkList=new ArrayList<>();
	
	int visible;
	
	ChunkFieldManager chunkFieldManager;
	
	
	private Logger log = Logger.getLogger(this.getClass());
	
	
	public VisibleChunkField(Node rootNode) 
	{
		//nwConnector=NetworkConnector.getConnector();	// Init de connector
		visible=Globals.chunkFieldSize;					// aantal zichtbare chunks rondom het centrale punt van de camera
		this.rootNode=rootNode;
		
		chunkFieldManager=new ChunkFieldManager(Globals.chunkFieldSize);
		updateChunkField(0, 0);
		
		
		
	}
	
	
	// Make an update of the chunk field
	public boolean updateChunkField(int x, int z)	// Geef het huidige chunkfield mee
	{
		
		List<String> rootChunkIdList=graphicsManager.getCurrentChunkIdList();
		for(String chunkId:chunkFieldManager.getCurrentChunkIdList())
			{
			 if(!rootChunkIdList.contains(chunkId))
			 {
				Chunk chunk=chunkcStorage.getChunk(chunkId);
				if(chunk==null)continue;
				graphicsManager.buildChunk(chunk); 
			 }
			}
		
		// Kijk of een update nodig is
		if(x==currentChunkX & z==currentChunkZ)
		{
			return false;		// Nee, keer false terug
		}
		
		log.debug("Visible Field update");
		currentChunkX=x;	// Update met nieuwe coordinaten
		currentChunkZ=z;
		
		// Laat de chunkmanager een nieuw veld maken
		chunkFieldManager.moveCenterAt(currentChunkX, currentChunkZ);
		
		
		// Leeg de chunks die worden verwijderd
		for(String chunkId:chunkFieldManager.getChunkIdToRemoveList())
		{
			Chunk chunk=chunkcStorage.getChunk(chunkId);
			if(chunk!=null)chunk.destruct();	
		}
		
		
		// Verwijder de geometries die er bij horen
		chunkFieldManager.getChunkIdToRemoveList().forEach(chunkId -> graphicsManager.removeChunk(chunkId));
		
		// Plaats de nieuwe chunks
		for(String chunkId:chunkFieldManager.getChunkIdToAddList())
		{
			Chunk chunk=chunkcStorage.getChunk(chunkId);
			if(chunk==null)continue;
			graphicsManager.buildChunk(chunk);
			
		}
		
	
		graphicsManager.addChunksToCollision(chunkFieldManager.getChunkIdToCollideList());
		
		
		
		log.debug("New build chunk :"+chunkFieldManager.getChunkIdToAddList().size());
		
	
		return true;
		
	}
	
	
	

	public void addNewBlock(Vector3f hitPoint,BlockType blockType) {
		int x=(int)(hitPoint.getX()+0.5f);
		int y=(int)(hitPoint.getY()+1);
		int z=(int)(hitPoint.getZ()+0.5f);
		
		if(hitPoint.getX()<0)x--;
		if(hitPoint.getZ()<0)z--;
		Geometry newBlock = blockManager.getBlock(blockType.getID());
		newBlock.setUserData("chunkid", Tools.getChunkId(x,z));
		newBlock.setLocalTranslation(x,y,z);
		rootNode.attachChild(newBlock);
		System.out.println("block added at x:"+x+" y:"+y+" z:"+z+" type:"+blockType.name());
		RequestRecord reqRec=new RequestRecord();
		reqRec.setRequesttype(2);
		reqRec.setBlockid(Tools.getBlockId(x, y, z));
		reqRec.setBlockType(blockType.getID());
		//orderManager.putRequest(reqRec);
	}


	public void removeBlock(Geometry geometry) {
		
		blockManager.popBlock(geometry);
		rootNode.detachChild(geometry);
	 
		chunkcStorage.getChunk(geometry.getUserData("chunkid")).delBlock(geometry.getUserData("blockid"));
		RequestRecord reqRec=new RequestRecord();
		reqRec.setRequesttype(3);
		reqRec.setBlockid(geometry.getUserData("blockid"));
		reqRec.setChunkid(geometry.getUserData("chunkid"));
		//orderManager.putRequest(reqRec);
		this.updateChunkField(currentChunkX, currentChunkZ);
	}



	

	
}
