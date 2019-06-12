package lioncraftserver.comobjects;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sande.lioncraft.Globals;
import com.sande.lioncraft.blockcase.BlockType;




// Een chunk is een verzamelink Blocken met een positie en ID, niks meer dan dat;
public class Chunk implements Serializable{
	

	private static final long serialVersionUID = -1835756401347723638L;
	private String Chunkid;
	private int x,z;
	Map<String,Block> blockList=new HashMap<>();
	
	
	public Chunk(int x,int z) {
		Chunkid=new StringBuilder().append(x).append('X').append(z).toString();
		this.x=x;
		this.z=z;
		
		//Block floor=new Block(x*Globals.chunkblocks,0,z*Globals.chunkblocks,BlockType.COMPUTERFLOOR.getID());
		
		for(int telx=0;telx<Globals.chunkblocks;telx++)
		{
			for(int telz=0;telz<Globals.chunkblocks;telz++)
			{
				Block floor=new Block(telx+(x*Globals.chunkblocks),0,telz+(z*Globals.chunkblocks),BlockType.COMPUTERFLOOR.getID());
				blockList.put(floor.getBLockID(), floor);
			}
		}
		
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
	
	
	public void delBlock(String blockid)
	{
		blockList.remove(blockid);
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
	
	
/*	public void buildChunk()
	{
		Node chunkNode=new Node();
		chunkNode.setName(Chunkid);
		blockList.forEach((key,block) -> {
			Geometry newBlock = Globals.blockManager.getBlock(block.getBlocktype());
			newBlock.setUserData("chunkid", this.Chunkid);
			newBlock.setUserData("blockid",block.getBLockID());
			newBlock.setLocalTranslation(block.getX(), block.getY(),block.getZ());
			
			if(block.getBlocktype()==BlockType.LETTERBLOCK.getID())
			{
				Material blockMaterial=newBlock.getMaterial();
				blockMaterial.setTexture("ColorMap", wrapTexture(block.getText()));
			}
			chunkNode.attachChild(newBlock);
			
		} );
		
		Globals.rootNode.attachChild(chunkNode);
	}

	
	private Texture2D wrapTexture(String text)
	{
		int imageSize=128;
		int stringSize=text.length();
		char[] textList=text.toCharArray();
		
		BufferedImage img = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = img.createGraphics();
	    
	    Font font = new Font("Arial", Font.PLAIN, 14);
	    g2d.setFont(font);
	    FontMetrics fm = g2d.getFontMetrics();
	    g2d.setColor(Color.GREEN);
	    int height = fm.getHeight();
	    int subtotal=0;
	    int offset=0;
	    int charsToWrite=0;
	    for(int tel=0;tel<stringSize;tel++)
	    {
	    	subtotal=subtotal+fm.charWidth(textList[tel]);
	    	charsToWrite++;
	    	if(subtotal>imageSize)
	    	{
	    		tel--;
	    		g2d.drawChars(textList, offset, charsToWrite, 0, height);
	    		subtotal=0;
	    		charsToWrite=0;
	    		height=height+fm.getHeight();
	    		offset=tel;
	    	}
	    }
	    g2d.drawChars(textList, offset, charsToWrite, 0, height);
	    
	    g2d.dispose();

	    AWTLoader loader = new AWTLoader();
	    Texture2D tex = new Texture2D(loader.load(img, true));

	    tex.setMagFilter(Texture.MagFilter.Nearest);
	    tex.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
	    return tex;
		
	}*/
	
	
}
