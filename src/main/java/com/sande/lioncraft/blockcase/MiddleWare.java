package com.sande.lioncraft.blockcase;


import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.sande.lioncraft.Globals;

public class MiddleWare extends BasicBlock {


	private static String appNameImage="block_images/"+MiddleWare.class.getSimpleName().toLowerCase()+".png";
	
	protected Geometry creatBlock()
	{
		Box box = new Box(Globals.BLOCKRADIUS,Globals.BLOCKRADIUS,Globals.BLOCKRADIUS);
		Geometry cubeSpatial = new Geometry("ApplicationBlock", box);	
		
		Material material = new Material(Globals.assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap",loadTexture(appNameImage));
		
		//Material material = new Material(Globals.assetmanager, "Common/MatDefs/Light/Lighting.j3md");
		//material.setColor("Diffuse", ColorRGBA.Red);
		//material.setColor("Ambient", new ColorRGBA(randje.nextFloat(),randje.nextFloat(),randje.nextFloat(),randje.nextFloat()));
		//material.setBoolean("UseMaterialColors", true);
		
		cubeSpatial.setMaterial(material);
		cubeSpatial.setShadowMode(ShadowMode.CastAndReceive);
		//cubeSpatial.setLocalTranslation(Globals.offset);
		
		return cubeSpatial;
	}
	
	
	@Override
	protected String stats() {
		
		return new StringBuilder().append("C ").append(created).append(" U ").append(reused).append("  R ").append(returned).append("  ApplicationBlock").toString();
	}
	


}
