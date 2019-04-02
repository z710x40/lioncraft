package com.sande.lioncraft.dbconnector;

import com.sande.lioncraft.blockcase.BlockType;

public class Block {

	
	private int x,y,z;
	private BlockType type;
	private String chunkID;
	
	
	public Block(String chunkID,int x, int y, int z, BlockType type) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.chunkID=chunkID;
	}


	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public int getZ() {
		return z;
	}


	public BlockType getType() {
		return type;
	}


	public String getChunkID() {
		return chunkID;
	}
	
	

	
	
	
	

}
