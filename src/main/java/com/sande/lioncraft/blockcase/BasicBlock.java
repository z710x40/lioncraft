package com.sande.lioncraft.blockcase;

import java.util.ArrayDeque;

import com.jme3.scene.Geometry;
import com.sande.lioncraft.Globals;

public abstract class BasicBlock {

	
	int created;
	int reused;
	int returned;
	
	private ArrayDeque<Geometry> serverBlockList=new ArrayDeque<>();
	
	public BasicBlock() {
		
	}

	protected abstract Geometry creatBlock();
	protected abstract String stats();
	
	
	// Get a block form the storage or create one
	public Geometry getBlock()
	{
		if(serverBlockList.isEmpty())
		{
			created++;
			return creatBlock();
		}
		reused++;
		return serverBlockList.removeLast();
	}
	
	
	// Geef een block terug aan de lijst
	public void popBlock(Geometry serverBlock)
	{
		returned++;
		
		Globals.bulletAppState.getPhysicsSpace().remove(serverBlock);
		
		
		
		serverBlockList.add(serverBlock);
	}
	
	
	
}
