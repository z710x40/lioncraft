package com.sande.lioncraft.blockcase;




import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.sande.lioncraft.Globals;

public class TestBlock extends BasicBlock {

	
	@Override
	protected Geometry creatBlock() {
		Box box = new Box(Globals.BLOCKRADIUS, Globals.BLOCKRADIUS, Globals.BLOCKRADIUS);

		Geometry cubeSpatial = new Geometry("TestBlock", box);

		Material material = new Material(Globals.assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");

		material.setTexture("ColorMap",loadTexture("block_images/testblock.png"));
		cubeSpatial.setMaterial(material);
		
		cubeSpatial.setShadowMode(ShadowMode.CastAndReceive);
		//cubeSpatial.setLocalTranslation(Globals.offset);
		
		return cubeSpatial;
	}

	@Override
	public String stats() {
		
		return new StringBuilder().append("Created ").append(created).append(" Reused ").append(reused).append("  Returned ").append(returned).append("  TestBlock").toString();
	}



}
