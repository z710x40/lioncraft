package com.sande.lioncraft;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.sande.lioncraft.dbconnector.DbConnector;


public class Globals {

	public final static float    BLOCKRADIUS=0.5f;						// Straal van een block, alleen voor create block
	public final static float    BLOCKSIZE=BLOCKRADIUS*2;				// De ribbe van een block, puur voor transformatie
	public final static int      chunkblocks=9;							// Aantal blokken in de chunk ribbe
	public final static int      chunkheigth=64;						// Hoogte in blokken
	public final static float    CHUNKRADIUS=BLOCKRADIUS*chunkblocks;	    // Absolute lengte blokken
	public final static float    CHUNKSIZE=CHUNKRADIUS*2;
	
	public final static int      chunkFieldSize=8;						// Aantal zichtbare chunks
	
	public final static Vector3f offset=new Vector3f(Globals.BLOCKRADIUS,Globals.BLOCKRADIUS,Globals.BLOCKRADIUS);					// Offset middelpunt block zit in het midden
	public final static float flooroffset=CHUNKRADIUS/2-BLOCKRADIUS;	// FloorBock middelpunt block zit in het midden
	
	
	public static AssetManager assetmanager;
	public static BulletAppState bulletAppState;
	public static CharacterControl player;
	public static DbConnector database;

	
	
}
