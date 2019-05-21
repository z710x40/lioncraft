package com.sande.lioncraft.blockcase;


import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.sande.lioncraft.Globals;

public class ComputerFloor extends BasicBlock {

	
	
	
	@Override
	protected Geometry creatBlock() {
		Box box = new Box(Globals.CHUNKRADIUS,Globals.BLOCKRADIUS,Globals.CHUNKRADIUS);
		Geometry cubeSpatial = new Geometry("ComputerFloor", box);	
		Material material = new Material(Globals.assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap",loadTexture("block_images/computerfloor.jpg"));
		
		//Material material = new Material(Globals.assetmanager, "Common/MatDefs/Light/Lighting.j3md");
		//material.setColor("Diffuse", ColorRGBA.Red);
		//material.setColor("Ambient", new ColorRGBA(randje.nextFloat(),randje.nextFloat(),randje.nextFloat(),randje.nextFloat()));
		//material.setBoolean("UseMaterialColors", true);
		
		cubeSpatial.setMaterial(material);
		//cubeSpatial.setLocalTranslation(new Vector3f(Globals.chunksize*Globals.BLOCKSIZE/2,Globals.BLOCKSIZE,Globals.chunksize*Globals.BLOCKSIZE/2));

		return cubeSpatial;
	}
	
	@Override
	public String stats() {
		
		return new StringBuilder().append("C ").append(created).append(" U ").append(reused).append("  R ").append(returned).append("  ComputerFloor").toString();
	}
	
	
			
	
}
