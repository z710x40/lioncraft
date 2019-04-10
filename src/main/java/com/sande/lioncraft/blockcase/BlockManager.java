package com.sande.lioncraft.blockcase;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;
import com.sande.lioncraft.Globals;


// Singelton class voor blockken management
public class BlockManager {

	
	private static BlockManager INSTANCE;
	
	TestBlock testBlock=new TestBlock();
	ComputerFloor computerFloor=new ComputerFloor();
	ServerBlock serverBlock=new ServerBlock();
	TextBlock letterBlock=new TextBlock();
	Brick brickBlock=new Brick();
	
	private BlockManager() {
		
	}

	public Geometry getBlock(int blockType)
	{
		switch(blockType)
		{

		case Btype.TESTBLOCK:		return testBlock.getBlock();
		case Btype.COMPUTERFLOOR:	return computerFloor.getBlock();
		case Btype.SERVERBLOCK:	return serverBlock.getBlock();
		case Btype.TEXTBLOCK:	return letterBlock.getBlock();
		case Btype.BRICK:	return brickBlock.getBlock();
					
		}
		
		return new TestBlock().getBlock();
	}
	
	
	
	public static BlockManager GetBlockManager()
	{
		if(INSTANCE==null)
		{
			INSTANCE=new BlockManager();
		}
		return INSTANCE;
	}

	public void popBlock(Geometry reuseBlock) {
		
		for(int tel=0;tel<reuseBlock.getNumControls();tel++)
		{
			reuseBlock.removeControl(reuseBlock.getControl(tel));
		}
		
		Globals.bulletAppState.getPhysicsSpace().removeAll(reuseBlock);
		if(reuseBlock.getName().equals("ServerBlock"))serverBlock.popBlock(reuseBlock);
		if(reuseBlock.getName().equals("ComputerFloor"))computerFloor.popBlock(reuseBlock);
		if(reuseBlock.getName().equals("TestBlock"))testBlock.popBlock(reuseBlock);
		if(reuseBlock.getName().equals("LetterBlock"))testBlock.popBlock(reuseBlock);
		if(reuseBlock.getName().equals("BrickBlock"))testBlock.popBlock(reuseBlock);
	}
	
	
	public void printStats()
	{
		System.out.println(testBlock.stats());
		System.out.println(serverBlock.stats());
		System.out.println(computerFloor.stats());
	}
	
	
}
