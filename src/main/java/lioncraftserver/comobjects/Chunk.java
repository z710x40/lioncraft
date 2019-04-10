package lioncraftserver.comobjects;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.Btype;



// Een chunk is een verzamelink Blocken met een positie en ID, niks meer dan dat;
public class Chunk implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1835756401347723638L;
	
	private String Chunkid;
	private int x,z;
		
	Map<String,Block> blockList=new HashMap<>();
	
	public Chunk(int x,int z) {
		Chunkid=new StringBuilder().append(x).append('X').append(z).toString();
		this.x=x;
		this.z=z;
		
		
		Block floor=new Block(x*Globals.chunkblocks,0,z*Globals.chunkblocks,Btype.COMPUTERFLOOR);
		blockList.put(floor.getBLockID(), floor);
	}
	
	
	public Chunk(String chunkid) {
		this.x=Integer.parseInt(chunkid.split("X")[0]);
		this.z=Integer.parseInt(chunkid.split("X")[1]);
		this.Chunkid=chunkid;
	}
	
	
	public void addBlock(Block newBlock)
	{
		blockList.put(newBlock.getBLockID(), newBlock);
	}
	
	
	public String getChunkid() {
		return Chunkid;
	}


	public int getX() {
		return x;
	}


	public int getZ() {
		return z;
	}


	public Map<String, Block> getBlockList() {
		return blockList;
	}


	@Override
	public String toString() {
		return "Chunk [Chunkid=" + Chunkid + ", x=" + x + ", z=" + z + ", blockList=" + blockList + "]";
	}


	public void destruct() {
		// TODO Auto-generated method stub
		
	}

	
	

}
