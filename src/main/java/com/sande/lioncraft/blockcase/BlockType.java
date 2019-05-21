package com.sande.lioncraft.blockcase;

public enum BlockType {
	
	COMPUTERFLOOR(0),
	TESTBLOCK(1),
	SERVERBLOCK(2),
	LETTERBLOCK(3), 
	BRICKBLOCK(4),
	LINUXBLOCK(5);

	
	private int id;
	private static BlockType[] list=BlockType.values();
	private static int size=list.length;
	private static int number=1;

	BlockType(int id){
	    this.id = id;
	  }

	public int getID(){
	    return id;
	  }
	  
	public static BlockType next()
	{
		number=number+1;
		if(number>=size)number=1;
		return list[number];
	}
	
	public static BlockType getCurBlock()
	{
		return list[number];
	}
	
	public static BlockType getBlock(int blocknum)
	{
		return list[blocknum];
	}

}
