package com.sande.lioncraft.managers;


import com.jme3.scene.Geometry;
import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.Application;
import com.sande.lioncraft.blockcase.BlockType;
import com.sande.lioncraft.blockcase.Brick;
import com.sande.lioncraft.blockcase.ComputerFloor;
import com.sande.lioncraft.blockcase.LinuxBlock;
import com.sande.lioncraft.blockcase.MiddleWare;
import com.sande.lioncraft.blockcase.ServerBlock;
import com.sande.lioncraft.blockcase.TestBlock;
import com.sande.lioncraft.blockcase.TextBlock;


// Singelton class voor blockken management
public class BlockManager {

	
	private static BlockManager INSTANCE;
	
	TestBlock testBlock=new TestBlock();
	ComputerFloor computerFloor=new ComputerFloor();
	ServerBlock serverBlock=new ServerBlock();
	TextBlock letterBlock=new TextBlock();
	Brick brickBlock=new Brick();
	Application application=new Application();
	MiddleWare middleware=new MiddleWare();
	LinuxBlock linuxBlock=new LinuxBlock();
	
	private BlockManager() {
		
	}

	public Geometry getBlock(int blockType)
	{
		switch(BlockType.getBlock(blockType))
		{

		case TESTBLOCK:		return testBlock.getBlock();
		case COMPUTERFLOOR:	return computerFloor.getBlock();
		case SERVERBLOCK:	return serverBlock.getBlock();
		case LETTERBLOCK:	return letterBlock.getBlock();
		case BRICKBLOCK:	return brickBlock.getBlock();
		case LINUXBLOCK:	return linuxBlock.getBlock();
					
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
		if(reuseBlock.getName().equals("LetterBlock"))letterBlock.popBlock(reuseBlock);
		if(reuseBlock.getName().equals("BrickBlock"))brickBlock.popBlock(reuseBlock);
		if(reuseBlock.getName().equals("LinuxBlock"))linuxBlock.popBlock(reuseBlock);
	}
	
	
	public void printStats()
	{
		System.out.println(testBlock.stats());
		System.out.println(serverBlock.stats());
		System.out.println(computerFloor.stats());
	}
	
	
}
