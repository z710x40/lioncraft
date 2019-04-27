package com.sande.lioncraft.blockcase;

public enum BlockType {
	
	TESTBLOCK(0),
	SERVERBLOCK(1),
	COMPUTERFLOOR(2),
	LETTERA(65),
	LETTERB(66),
	LETTERC(67),
	LETTERD(68),
	LETTERE(69),
	LETTERBLOCK(70), 
	BRICKBLOCK(71);

	
	private int id;
	private static int number=0;

	BlockType(int id){
	    this.id = id;
	  }

	public int getID(){
	    return id;
	  }
	  
	public static BlockType next()
	{
		number=number+1;
		if(number>=BlockType.values().length)number=0;
		return BlockType.values()[number];
	}

}
