package com.sande.lioncraft.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.BlockManager;
import com.sande.lioncraft.network.NetworkConnector;

import lioncraftserver.comobjects.Chunk;
import lioncraftserver.comobjects.ChunkListRecord;
import lioncraftserver.comobjects.RequestRecord;

// Deze class verzorgt de zichtbare chunks
public class VisibleChunkField {

	
	int fieldPosX;			// De positie van de camera gemeten in chunks
	int fieldPosZ;
	
	int currentChunkX=1;		// Het huidige centrale chuckField
	int currentChunkZ=1;
	
	ChunkStorage chunkcStorage=ChunkStorage.getChunkStorage();	// De centrale chunk database
	NetworkConnector nwConnector;
	BlockManager blockManager=BlockManager.GetBlockManager();
	Node rootNode;
	
	
	int visible;
	
	ArrayList<String> currentChunkFieldIDList=new ArrayList<>();	// Huidige lijst met chunks
	ArrayList<String> chunksToReturn=new ArrayList<>();				// Chunks die objecten terug moeten geven
	ArrayList<String> chunksToAdd=new ArrayList<>();				// Chunks die moeten worden toegevoegd
	ArrayList<String> chunksToCollide=new ArrayList<>();			// Chunks die kunnen botsen
	ArrayList<String> newChunkFieldIDList=new ArrayList<>();		// De nieuwe chunklist na het bereiken van een nieuwe chunk
	
	
	public VisibleChunkField(Node rootNode) 
	{
		nwConnector=NetworkConnector.getConnector();	// Init de connector
		visible=Globals.chunkFieldSize;					// aantal zichtbare chunks rondom het centrale punt van de camera
		this.rootNode=rootNode;
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
		
		// Ja, maak een nieuwe chunklist aan op basis van de nieuwe coordinaten waarmee gebotst mag worden
		chunksToCollide.clear();
		for(int telx=-1;telx<2;telx++)
		{
			for(int telz=-1;telz<2;telz++)
			{
				chunksToCollide.add(new StringBuilder().append(currentChunkX+telx).append('X').append(currentChunkZ+telz).toString());
			}
		}
		
		// Maak een lijst van chunks die je kan zien
		newChunkFieldIDList.clear();
		for(int telx=-Globals.chunkFieldSize;telx<Globals.chunkFieldSize;telx++)
		{
			for(int telz=-Globals.chunkFieldSize;telz<Globals.chunkFieldSize;telz++)
			{
				
				newChunkFieldIDList.add(new StringBuilder().append(currentChunkX+telx).append('X').append(currentChunkZ+telz).toString());
			}
		}
		
		// Haal eerst alle chunks er uit die niet meer te zien zijn
		chunksToReturn.clear();
		for(String currentChunk:currentChunkFieldIDList)
		{
			if(!newChunkFieldIDList.contains(currentChunk))
			{
				chunksToReturn.add(currentChunk);
			}
		}
		
		// Leeg de chunks die worden verwijderd
		for(String chunkID:chunksToReturn)
		{
			chunkcStorage.getChunk(chunkID).destruct();
		}
		
		
		// Voeg de nieuwe chunks toe
		chunksToAdd.clear();
		for(String newChunk:newChunkFieldIDList)
		{
			if(!currentChunkFieldIDList.contains(newChunk))
			{
				chunksToAdd.add(newChunk);
			}
		}
		
		
		
		// Plaats de grafische objecten
		//chunksToAdd.forEach(chunkID -> rootNode.attachChild(chunkcStorage.getChunk(chunkID).place()));
		RequestRecord rq=RequestRecord.builder().withRequesttype(1).withChunkids(chunksToAdd).build();	// Maar een request record aan
		nwConnector.writeRecord(rq);																	// Stuur deze naar de server
		ChunkListRecord chunkToList=nwConnector.readChunkListRecord();
		if(chunkToList!=null)
		{
			chunkToList.list.forEach(chunk -> this.buildChunk(chunk));
		}
		else
		{	System.out.println("Server could not provide a full chunklist");
			errorFlag=false;
		}
		// maak de collision group voor de dichtsbijzinde group
		System.out.println("Clean ");
		Globals.bulletAppState.getPhysicsSpace().removeAll(rootNode);
		//Collection<PhysicsRigidBody> rigList=Globals.bulletAppState.getPhysicsSpace().getRigidBodyList();			//maak een lijst van wat er nu in de collision space zit
		//System.out.println("Collision changed, number of bodies is "+rigList.size());
		//rigList.forEach(rigidBody -> Globals.bulletAppState.getPhysicsSpace().removeCollisionObject(rigidBody));	// maak deze leeg
		
		// Loop langs alle zichtbare objecten
		for(Spatial spat:rootNode.getChildren())									// Loop lang de lijst met zichtbare objecten
		{
			Geometry block=(Geometry)spat;											// Converteer naar geometry
			String chunkid=block.getUserData("chunkid");							// Is het een chunk waar in gebotst kan worden
			if(chunkid!=null)
			{
				if(chunksToCollide.contains(chunkid))makeCollision(block);			// zit het chunkid in de collision list, maar deze dan botsbaar
				
				if(chunksToReturn.contains(chunkid))								// Zit het in de chunk in de return list.
				{
				 blockManager.popBlock(block);										// geef het terug aan de blockmanager
				 rootNode.detachChild(block);										// En haal het uit de grafische omgeving
				}
			}
		}
		Globals.bulletAppState.getPhysicsSpace().add(Globals.player);
		
		
		// Verwijder de geometries die er bij horen
		for(Spatial spat:rootNode.getChildren())
			{
				Geometry block=(Geometry)spat;
				String chunkid=block.getUserData("chunkid");
				if(chunkid!=null)
				{
					
					if(chunksToReturn.contains(chunkid))
						{
						blockManager.popBlock(block);
						 rootNode.detachChild(block);
						}
				}
			}
		
		
		currentChunkFieldIDList.clear();
		currentChunkFieldIDList.addAll(newChunkFieldIDList);

		//BlockManager.GetBlockManager().printStats();
		return errorFlag;
		
	}
	

	
	
	private void dump(ArrayList<String> list)
	{
		System.out.print("Size "+list.size()+" elms: ");
		list.forEach(elm -> System.out.print(elm+" "));
		System.out.println(System.lineSeparator());
	}
	
	
	// Het grafische gedeelte
	private void buildChunk(Chunk chunk)
	{

		chunk.getBlockList().forEach((key,block) -> {
			Geometry newBlock = blockManager.getBlock(block.getBlocktype());
			newBlock.setUserData("chunkid", chunk.getChunkid());
			newBlock.setLocalTranslation(block.getX(), block.getY(),block.getZ());
			rootNode.attachChild(newBlock);
			
			
			
		} );
	}
	
	
	private void makeCollision(Geometry block) {
			CollisionShape cubeCollision=CollisionShapeFactory.createBoxShape(block);
			RigidBodyControl rigidBodyControl=new RigidBodyControl(cubeCollision,0);
			rigidBodyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
			block.addControl(rigidBodyControl);
			Globals.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		
	}
	
}
