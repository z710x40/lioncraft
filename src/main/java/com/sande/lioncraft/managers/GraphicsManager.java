package com.sande.lioncraft.managers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.BlockType;

import lioncraftserver.comobjects.Chunk;

public class GraphicsManager {

	
	private static GraphicsManager INSTANCE;
	private Node rootNode;
	private BlockManager blockManager=BlockManager.GetBlockManager();
	
	private Map<String,Chunk> chunkList=new HashMap<>();
	private Map<String,Node> chunkNodeList=new HashMap<>();
	private Chunk tempChunk;
	private Node tempNode;
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private GraphicsManager(Node rootNode) {
		this.rootNode=rootNode;
	}

	
	public static GraphicsManager getInstance(Node rootNode)
	{
		if(INSTANCE==null)
		{
			INSTANCE=new GraphicsManager(rootNode);
		}
		return INSTANCE;
	}
	
	
	public void buildChunk(Chunk chunk)
	{
		Node chunkNode=new Node();
		chunkNode.setName(chunk.getChunkid());
		chunk.getBlockList().forEach((key,block) -> {
			Geometry newBlock = Globals.blockManager.getBlock(block.getBlocktype());
			newBlock.setUserData("chunkid", chunk.getChunkid());
			newBlock.setUserData("blockid",block.getBLockID());
			newBlock.setLocalTranslation(block.getX(), block.getY(),block.getZ());
			
			if(block.getBlocktype()==BlockType.LETTERBLOCK.getID())
			{
				Material blockMaterial=newBlock.getMaterial();
				blockMaterial.setTexture("ColorMap", wrapTexture(block.getText()));
			}
			chunkNode.attachChild(newBlock);
			
		} );
		
		rootNode.attachChild(chunkNode);
		
		chunkList.put(chunk.getChunkid(), chunk);
		chunkNodeList.put(chunk.getChunkid(), chunkNode);
	}
	
	
	public void removeChunk(String chunkId)
	{
		tempNode=chunkNodeList.get(chunkId);
		if(tempNode==null) 
			{
			//log.error("removeChunk() chunk not found:"+chunkId);
				return;
			}
		
		tempNode.getChildren().forEach(geom -> blockManager.popBlock((Geometry)geom));
		tempNode.detachAllChildren();
		rootNode.detachChild(tempNode);
		chunkNodeList.remove(chunkId);
		tempNode=null;
	}
	
	
	
	private Texture2D wrapTexture(String text)
	{
		int imageSize=128;
		int stringSize=text.length();
		char[] textList=text.toCharArray();
		
		BufferedImage img = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = img.createGraphics();
	    
	    Font font = new Font("Arial", Font.PLAIN, 14);
	    g2d.setFont(font);
	    FontMetrics fm = g2d.getFontMetrics();
	    g2d.setColor(Color.GREEN);
	    int height = fm.getHeight();
	    int subtotal=0;
	    int offset=0;
	    int charsToWrite=0;
	    for(int tel=0;tel<stringSize;tel++)
	    {
	    	subtotal=subtotal+fm.charWidth(textList[tel]);
	    	charsToWrite++;
	    	if(subtotal>imageSize)
	    	{
	    		tel--;
	    		g2d.drawChars(textList, offset, charsToWrite, 0, height);
	    		subtotal=0;
	    		charsToWrite=0;
	    		height=height+fm.getHeight();
	    		offset=tel;
	    	}
	    }
	    g2d.drawChars(textList, offset, charsToWrite, 0, height);
	    
	    g2d.dispose();

	    AWTLoader loader = new AWTLoader();
	    Texture2D tex = new Texture2D(loader.load(img, true));

	    tex.setMagFilter(Texture.MagFilter.Nearest);
	    tex.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
	    return tex;
		
	}
	
	
	public List<String> getCurrentChunkIdList()
	{
		List<String> tempList=new ArrayList<>();
		List<Spatial> spatialList=rootNode.getChildren();
		for(Spatial spatial:spatialList)
		{
			if(spatial instanceof Node)
			{
				tempList.add(spatial.getName());
			}
		}
		
		return tempList;
	}
	
	
	
	public void addChunkToCollision(String chunkId)
	{
		tempNode=chunkNodeList.get(chunkId);
		if(tempNode==null) 
			{
				return;
			}
		
		tempNode.getChildren().forEach(geom -> makeCollision((Geometry)geom));
	}
	
	
	private void makeCollision(Geometry block) {
		CollisionShape cubeCollision=CollisionShapeFactory.createBoxShape(block);
		RigidBodyControl rigidBodyControl=new RigidBodyControl(cubeCollision,0);
		rigidBodyControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
		block.addControl(rigidBodyControl);
		Globals.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
	
}


	public void clearAllCollisions() {
		Globals.bulletAppState.getPhysicsSpace().removeAll(rootNode);
	}
	
	public void addCharacterToCollision()
	{
		//Globals.bulletAppState.getPhysicsSpace().add(Globals.player);
	}
	
	
	public void stats()
	{
		System.out.println("ChunkList");
		chunkList.forEach((K,V) -> System.out.printf(" %s",K));
		System.out.println("\nChunkNodeList");
		chunkNodeList.forEach((K,V) -> System.out.printf(" %s",K));
		System.out.println("---");
	}
}
