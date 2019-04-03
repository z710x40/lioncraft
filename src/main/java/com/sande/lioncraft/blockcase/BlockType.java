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
	LETTERBLOCK(70);

	
	private int id;

	BlockType(int id){
	    this.id = id;
	  }

	  public int getID(){
	    return id;
	  }

}
