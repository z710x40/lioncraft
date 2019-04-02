package com.sande.lioncraft.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.BlockManager;
import com.sande.lioncraft.blockcase.BlockType;
import com.sande.lioncraft.dbconnector.Block;



// Een chunk is een verzamelink Blocken met een positie en ID, niks meer dan dat;
public class Chunk {

	
	BlockManager blockManager=BlockManager.GetBlockManager();
	ArrayList<Geometry> geometrykList=new ArrayList<>();	// opslage van de grafische objecten
	ArrayList<Block> blockList=new ArrayList<>();			// opslag van de positie van de grafische objecten
	
	private Node chunkNode=new Node();
	
	private String Chunkid;
	//private int x=0;
	//private int y=0;
	
	private float posx,posz;
	private int x,z;
		

	
	public Chunk(int x,int z) {
		
		Chunkid=new StringBuilder().append(x).append('X').append(z).toString();
		
		//System.out.println("New Chunk with ID "+Chunkid);
		posx=x*Globals.CHUNKSIZE;
		posz=z*Globals.CHUNKSIZE;
		chunkNode.setName("chunk");
		chunkNode.setLocalTranslation(Globals.offset);
		this.x=x*Globals.chunkblocks;
		this.z=z*Globals.chunkblocks;
	}
	

	public String getChunkid() {
		return Chunkid;
	}

/*	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}*/

	public Node getChunkNode()
	{
		return chunkNode;
	}
	
	
	public float getPosx() {
		return posx;
	}

	public float getPosz() {
		return posz;
	}

	
	public void destruct()
	{
		//System.out.println("Destruction of "+this.Chunkid);
		
		geometrykList.forEach(serverBlock -> blockManager.popBlock(serverBlock));
		geometrykList.clear();
		chunkNode.detachAllChildren();
	
		//System.out.println("chunk "+Chunkid+ " has elemts "+geometrykList.size());
	}
	
	// Test om te zien of het werkt
	public void RandomFill() 
	{
		blockList.clear();
		Random rand =new Random();
		//int heigth=rand.nextInt(3);
		//int heigth=1;
		
		
		blockList.add(new Block(this.Chunkid,0,0,0,BlockType.COMPUTERFLOOR));

		for(int x=0;x<Globals.chunkblocks-3;x++)
		{
			for(int z=0;z<Globals.chunkblocks-3;z++)
			{
				int heigth=rand.nextInt(8);
				for(int y=1;y<heigth;y++)
				{
					
					if(y==0)blockList.add(new Block(this.Chunkid,x,y,z,BlockType.COMPUTERFLOOR));
					else blockList.add(new Block(this.Chunkid,x,y,z,BlockType.SERVERBLOCK));
				}
			}
		}
		
			
		//blockList.add(new Block(0,0,0,1));
		//blockList.add(new Block(0,1,0,1));

	}
	
	
	// Vull alles met een blocklist uit de database
	public void DatabaseFill()
	{
		List<Block> fillList=Globals.database.getChunk(this.Chunkid);
		blockList.clear();
		fillList.forEach(block -> blockList.add(block));
		//blockList.add(new Block(this.Chunkid,0,1,0,BlockType.TESTBLOCK));
		//blockList.add(new Block(this.Chunkid,4,1,4,BlockType.TESTBLOCK));
		
		blockList.add(new Block(this.Chunkid,x,0,z,BlockType.COMPUTERFLOOR));
	}


	// Maak de node met de blocken vertaald naar Geometries
	public Spatial place() {
		
		for(Block block:blockList)
		{
			Geometry newBLock = blockManager.getBlock(block.getType());
			newBLock.setLocalTranslation((block.getX() * Globals.BLOCKSIZE), block.getY() * Globals.BLOCKSIZE,(block.getZ() * Globals.BLOCKSIZE));
			//newBLock.setName(new StringBuilder().append(Chunkid).append(';').append(block.getX()).append('x').append(block.getY()).append('x').append(block.getZ()).toString());
			
			geometrykList.add(newBLock);
			chunkNode.attachChild(newBLock);
		}
		
		return chunkNode;
	}


	public Object makeCollision() {
		
		List<Spatial> blockList=chunkNode.getChildren();
		for(Spatial block:blockList)
		{
			CollisionShape cubeCollision=CollisionShapeFactory.createBoxShape(block);
			RigidBodyControl rigidBodyControl=new RigidBodyControl(cubeCollision,0);
			block.addControl(rigidBodyControl);
			//rigidBodyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
			//rigidBodyControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
			Globals.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		}
		
		return null;
	}
	

}
