package lioncraftserver.tools;


public class ServerGlobals {

	public final static float    BLOCKRADIUS=1f;						// Straal van een block, alleen voor create block
	public final static float    BLOCKSIZE=BLOCKRADIUS*2;				// De ribbe van een block, puur voor transformatie
	public final static int      CHUNKBLOCKS=9;							// Aantal blokken in de chunk ribbe
	public final static int      chunkheigth=64;						// Hoogte in blokken
	public final static float    CHUNKRADIUS=BLOCKRADIUS*CHUNKBLOCKS;	// Absolute lengte blokken
	public final static float    CHUNKSIZE=CHUNKRADIUS*2;
	
	public final static int      chunkFieldSize=8;						// Aantal zichtbare chunks
	
	
	public final static float flooroffset=CHUNKRADIUS/2-BLOCKRADIUS;	// FloorBock middelpunt block zit in het midden
	
	

	
	
}
