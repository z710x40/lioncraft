package com.sande.lioncraft.blockcase;



import com.jme3.asset.AssetNotFoundException;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.sande.lioncraft.Globals;

public class TestBlock extends BasicBlock {

	
	@Override
	protected Geometry creatBlock() {
		Box box = new Box(Globals.BLOCKRADIUS, Globals.BLOCKRADIUS, Globals.BLOCKRADIUS);

		Geometry cubeSpatial = new Geometry("TestBlock", box);

		Material material = new Material(Globals.assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");

		material.setTexture("ColorMap",loadTexture());
		cubeSpatial.setMaterial(material);
		
		cubeSpatial.setShadowMode(ShadowMode.CastAndReceive);
		//cubeSpatial.setLocalTranslation(Globals.offset);
		
		return cubeSpatial;
	}

	@Override
	protected String stats() {
		
		return new StringBuilder().append("C ").append(created).append(" U ").append(reused).append("  R ").append(returned).append("  TestBlock").toString();
	}

	
	private static Texture loadTexture() {
		Texture nodeTexture=null;
		try{
			nodeTexture=Globals.assetmanager.loadTexture("block_images/testblock.png");
		}
		catch (AssetNotFoundException e){
			System.out.println("Could not find asset block_images/testblock.png");
		}
		return nodeTexture;
	}
		

}
