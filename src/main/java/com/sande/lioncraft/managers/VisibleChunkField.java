package com.sande.lioncraft.managers;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.BlockType;

import lioncraftserver.comobjects.ChunkListRecord;
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
	NetworkConnector nwConnector;
	BlockManager blockManager=BlockManager.GetBlockManager();
	Node rootNode;
	RequestRecord reqRec=new RequestRecord();
	OrderManager orderManager=OrderManager.getInstance();
	
	int visible;
	
	ChunkFieldManager chunkFieldManager;
	
	
	public VisibleChunkField(Node rootNode) 
	{
		nwConnector=NetworkConnector.getConnector();	// Init de connector
		visible=Globals.chunkFieldSize;					// aantal zichtbare chunks rondom het centrale punt van de camera
		this.rootNode=rootNode;
		
		chunkFieldManager=new ChunkFieldManager(Globals.chunkFieldSize);
		updateChunkField(0, 0);
		
		
		
	}
	
	
	// Make an update of the chunk field
	public boolean updateChunkField(int x, int z)	// Geef het huidige chunkfield mee
	{
		boolean errorFlag=true;
		// Kijk of een update nodig is
		if(x==currentChunkX & z==currentChunkZ)
		{
			return false;		// Nee, keer false terug
		}
		
		System.out.println("Visible Field update");
		currentChunkX=x;	// Update met nieuwe coordinaten
		currentChunkZ=z;
		
		// Laat de chunkmanager een nieuw veld maken
		chunkFieldManager.moveCenterAt(currentChunkX, currentChunkZ);
		
		
		// Leeg de chunks die worden verwijderd
		chunkFieldManager.getChunkIdToRemoveList().forEach(chunkId -> chunkcStorage.getChunk(chunkId).destruct());
		
		
		// Verwijder de geometries die er bij horen
		chunkFieldManager.getChunkIdToRemoveList().forEach(chunkId -> graphicsManager.removeChunk(chunkId));
		
		
		// Haal alles van het netwerk op
		RequestRecord rq=RequestRecord.builder().withRequesttype(1).withChunkids(chunkFieldManager.getChunkIdToAddList()).build();	// Maar een request record aan
		nwConnector.writeRecord(rq);																								// Stuur deze naar de server
		
		for(String chunkId:chunkFieldManager.getChunkIdToAddList())
		{
			orderManager.putRequestRecord(rq);
		}
		
		
		
		ChunkListRecord chunkToList=null;				// Leeg de chunklist
		Object record=getChunkListFromTheNetwork(10);		// Lees de ontvangen chunklist
		if(record instanceof ChunkListRecord)		
		{
			chunkToList=(ChunkListRecord)record;
		}
		if(chunkToList==null)
		{
			System.out.println("Server could not provide a full chunklist");
			return false;
		}
		
		// Plaats de grafische objecten
		chunkToList.list.forEach(chunk -> graphicsManager.buildChunk(chunk));
		
		// maak de collision group voor de dichtsbijzinde group
		graphicsManager.clearAllCollisions();
		chunkFieldManager.getChunkIdToCollideList().forEach(chunkId -> graphicsManager.addChunkToCollision(chunkId));
		graphicsManager.addCharacterToCollision();

		//graphicsManager.stats();
		//BlockManager.GetBlockManager().printStats();
		return errorFlag;
		
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
		reqRec.setRequesttype(2);
		reqRec.setBlockid(Tools.getBlockId(x, y, z));
		reqRec.setBlockType(blockType.getID());
		nwConnector.writeRecord(reqRec);
	}


	public void removeBlock(Geometry geometry) {
		
		blockManager.popBlock(geometry);
		rootNode.detachChild(geometry);
	 
		chunkcStorage.getChunk(geometry.getUserData("chunkid")).delBlock(geometry.getUserData("blockid"));
		reqRec.setRequesttype(3);
		reqRec.setBlockid(geometry.getUserData("blockid"));
		reqRec.setChunkid(geometry.getUserData("chunkid"));
		nwConnector.writeRecord(reqRec);
		this.updateChunkField(currentChunkX, currentChunkZ);
	}
	
	
	Object getChunkListFromTheNetwork(int timeout)
	{
		for(int tel=0;tel<timeout;tel++)
		{
			Object result=nwConnector.readRecord();
			if(result!=null)return result;
		}
		System.out.println("getChunkListFromTheNetwork to many read attempts");
		return null;
	}
	
}
