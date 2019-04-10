package lioncraftserver.comobjects;

import java.io.Serializable;

import lioncraftserver.tools.ServerGlobals;
import lioncraftserver.tools.Tools;

public class Block implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2573260215978505535L;
	
	private String blockid;
	private int x,y,z;				// position
	private int relx,relz;			// Position in the chunk
	private int blocktype=1;		// soort block
	private String text;			// optional text on a block
	private boolean immutable=true;
	private String chunkid;	// Het chunkid
	
	public Block() {

	}

	public Block(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.relx=x % ServerGlobals.CHUNKBLOCKS;
		this.relz=z % ServerGlobals.CHUNKBLOCKS;
		chunkid=Tools.getChunkId(x, z);
		blockid=Tools.getBlockId(x, y, z);
	}

	
	public Block(int x, int y, int z,int blocktype) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.blocktype=blocktype;
		chunkid=Tools.getChunkId(x, z);
		blockid=Tools.getBlockId(x, y, z);
	}
	
	public String getBLockID()
	{
		return blockid;
	}

	public int getRelx() {
		return relx;
	}

	public int getRelz() {
		return relz;
	}

	public int getBlocktype() {
		return blocktype;
	}
	
	public String getChunkID()
	{
		return chunkid;
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
	
	
	
}
