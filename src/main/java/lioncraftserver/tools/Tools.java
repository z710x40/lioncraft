package lioncraftserver.tools;



public class Tools
{
	
	public static String getChunkId(int posx,int posz)
	{
		int chunkPosX= (int)(posx / (ServerGlobals.CHUNKBLOCKS) );
		if(posx<0)chunkPosX--;
		int chunkPosZ= (int)(posz  / (ServerGlobals.CHUNKBLOCKS) );
		if(posz<0)chunkPosZ--;
		return new StringBuilder().append(chunkPosX).append('X').append(chunkPosZ).toString();
	}
	
	
	
	
	public static String getBlockId(int x,int y,int z)
	{
		
		return new StringBuilder().append(x).append('X').append(y).append('X').append(z).toString();
	}

	
}
