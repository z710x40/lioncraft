package com.sande.lioncraft.storage;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.scene.Node;
import com.sande.lioncraft.Globals;
import com.sande.lioncraft.landscape.ChunkStorage;

// Deze class verzorgt de zichtbare chunks
public class VisibleChunkField {

	
	int fieldPosX;			// De positie van de camera gemeten in chunks
	int fieldPosZ;
	
	int currentChunkX=1;		// Het huidige centrale chuckField
	int currentChunkZ=1;
	
	ChunkStorage chunkcStorage=new ChunkStorage();	// De centrale chunk database
	
	Node rootNode;
	
	
	int visible;
	
	ArrayList<String> currentChunkFieldIDList=new ArrayList<>();	// Huidige lijst met chunks
	ArrayList<String> chunksToReturn=new ArrayList<>();				// Chunks die objecten terug moeten geven
	ArrayList<String> chunksToAdd=new ArrayList<>();				// Chunks die moeten worden toegevoegd
	ArrayList<String> chunksToCollide=new ArrayList<>();			// Chunks die kunnen botsen
	ArrayList<String> newChunkFieldIDList=new ArrayList<>();		// De nieuwe chunklist na het bereiken van een nieuwe chunk
	
	
	
	public VisibleChunkField(Node rootNode) 
	{
		visible=Globals.chunkFieldSize;	// aantal zichtbare chunks rondom het centrale punt van de camera
		
		this.rootNode=rootNode;
		
		for(int telx=-100;telx<100;telx++)
		{
			for(int telz=-100;telz<100;telz++)
			{
				Chunk newChunk=new Chunk(telx,telz);		// Maak een nieuwe chunk
				newChunk.DatabaseFill();					// Vul deze met wat random zooi
				//newChunk.RandomFill();
				chunkcStorage.add(newChunk);				// Zet hem in de chunk storage
				
			}
		}
		//updateChunkField(0,0);
	}
	
	
	// Make an update of the chunk field
	public boolean updateChunkField(int x, int z)	// Geef het huidige chunkfield mee
	{
		// Kijk of een update nodig is
		if(x==currentChunkX & z==currentChunkZ)
		{
			return false;		// Nee, keer false terug
		}
		
		//System.out.println("Visible Field update");
		currentChunkX=x;	// Update met nieuwe coordinaten
		currentChunkZ=z;
		
		// Ja, maak een nieuwe chunklist aan op basis van de nieuwe coordinaten
		chunksToCollide.clear();
		for(int telx=-1;telx<2;telx++)
		{
			for(int telz=-1;telz<2;telz++)
			{
				
				chunksToCollide.add(new StringBuilder().append(currentChunkX+telx).append('X').append(currentChunkZ+telz).toString());
			}
		}
		
		// Maak een lijst van chunks waarmee gebotst kan worden
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
			rootNode.detachChildNamed(chunkID);
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
		chunksToAdd.forEach(chunkID -> rootNode.attachChild(chunkcStorage.getChunk(chunkID).place()));
		
		// maak de collision group voor de dichtsbijzinde group
		//System.out.println("Collision changed");
		Collection<PhysicsRigidBody> rigList=Globals.bulletAppState.getPhysicsSpace().getRigidBodyList();
		//System.out.println("Collision changed, number of bodies is "+rigList.size());
		rigList.forEach(rigidBody -> Globals.bulletAppState.getPhysicsSpace().removeCollisionObject(rigidBody));
		//Globals.bulletAppState.getPhysicsSpace().add(Globals.player);
		
		chunksToCollide.forEach(chunkID -> chunkcStorage.getChunk(chunkID).makeCollision());
		
		currentChunkFieldIDList.clear();
		currentChunkFieldIDList.addAll(newChunkFieldIDList);

		//BlockManager.GetBlockManager().printStats();
		return true;
		
	}
	

	
	
	private void dump(ArrayList<String> list)
	{
		System.out.print("Size "+list.size());
		list.forEach(elm -> System.out.print(elm+" "));
		System.out.println(System.lineSeparator());
	}
	

}
