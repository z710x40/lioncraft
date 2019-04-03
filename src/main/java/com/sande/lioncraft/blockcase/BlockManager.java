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
	LetterBlock letterBlock=new LetterBlock();
	
	private BlockManager() {
		
	}

	public Geometry getBlock(BlockType blockType)
	{
		switch(blockType)
		{

		case TESTBLOCK:		return testBlock.getBlock();
		case COMPUTERFLOOR:	return computerFloor.getBlock();
		case SERVERBLOCK:	return serverBlock.getBlock();
		case LETTERA:		return testBlock.getBlock();
		case LETTERB:		return testBlock.getBlock();
		case LETTERC:		return testBlock.getBlock();
		case LETTERD:		return testBlock.getBlock();
		case LETTERE:		return testBlock.getBlock();
		case LETTERBLOCK:	return letterBlock.getBlock();
					
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
	}
	
	
	public void printStats()
	{
		System.out.println(testBlock.stats());
		System.out.println(serverBlock.stats());
		System.out.println(computerFloor.stats());
	}
	
	
}
