package com.sande.lioncraft;

import java.util.HashMap;
import java.util.Iterator;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.sande.lioncraft.blockcase.BlockType;
import com.sande.lioncraft.dbconnector.DbConnector;
import com.sande.lioncraft.managers.NetworkConnector;
import com.sande.lioncraft.managers.NetworkManager;
import com.sande.lioncraft.managers.OrderManager;
import com.sande.lioncraft.managers.VisibleChunkField;
import com.sande.lioncraft.storage.ChunkOrg;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;



public class LionCraft extends SimpleApplication implements ActionListener,ScreenController{

	
	HashMap<String,ChunkOrg> visualChunks=new HashMap<>();

	String currentChunkID="";
	
	VisibleChunkField visibleChunkField;
	
	BitmapText chunkInfoText;
	BitmapText locationInfoText;
	CharacterControl player;
	CollisionResults collisionResults = new CollisionResults();
	
	private boolean left = false, right = false, up = false, down = false;
	
	private Vector3f walkDirection = new Vector3f();
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();
	
	private Spatial defaultSky; 		// Pointer naar de skybox
	
	// De GUI interface
	private Nifty nifty;
	private TextRenderer message,blockname;
	private int blockmode=0;	// 0 niks, 1=create 2=remove
	
	public LionCraft() {
		
		// Start the managers
		OrderManager.getInstance();
		NetworkManager.getInstance();
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		LionCraft lioncraft=new LionCraft();
		lioncraft.init();
		lioncraft.start();
	}

	
	private void init() {
		NetworkConnector nwConnector=NetworkConnector.getConnector();
		if(!nwConnector.connect("192.168.178.20", 2016))
			{
			 System.out.println("Cannot connect to the lioncraft server");
			 System.exit(1);
			};
	}



	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		System.out.println("Init graphical engine");
		Globals.assetmanager=this.assetManager;
		flyCam.setMoveSpeed(50);
		initKeys();
		
		
		DbConnector dbConnect=new DbConnector(Globals.chunkblocks);
		Globals.database=dbConnect;
		Globals.rootNode=rootNode;
		// Maak een nieuwe wereld
		dbConnect.makeWorld();
		
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		chunkInfoText = new BitmapText(guiFont, false);
		chunkInfoText.setSize(guiFont.getCharSet().getRenderedSize());
		chunkInfoText.setText("XXX");
		chunkInfoText.setLocalTranslation(0, this.settings.getHeight(), 0);
		chunkInfoText.setColor(ColorRGBA.Black);
		guiNode.attachChild(chunkInfoText);
		
		locationInfoText = new BitmapText(guiFont, false);
		locationInfoText.setSize(guiFont.getCharSet().getRenderedSize());
		locationInfoText.setText("XXX");
		locationInfoText.setLocalTranslation(0, this.settings.getHeight()-locationInfoText.getLineHeight(), 0);
		locationInfoText.setColor(ColorRGBA.Black);
		//guiNode.attachChild(locationInfoText);
		
		
		initCrossHairs();
		
		/* testbox
        this.viewPort.setBackgroundColor(ColorRGBA.LightGray);
        Box tBox=new Box(1,100,1);
        Geometry tGeom=new Geometry("test",tBox);
		Material material = new Material(Globals.assetmanager, "Common/MatDefs/Light/Lighting.j3md");
		material.setColor("Diffuse", ColorRGBA.Red);
		material.setColor("Ambient", ColorRGBA.Cyan);
		tGeom.setMaterial(material);
		tGeom.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(tGeom); */
        
        
        // Verlichting
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.13F, -0.4F, 0.9F).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.Gray);
        rootNode.addLight(al);
        
        
        // Create the physics
        BulletAppState bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        Globals.bulletAppState=bulletAppState;
        
        stateManager.attach(bulletAppState);
        
        // Make the character
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.0f, 4f, 1);
        //BoxCollisionShape capsuleShape=new BoxCollisionShape(new Vector3f(1.5f, 6f, 1f));
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(new Vector3f(0,-30f,0));
        player.setPhysicsLocation(new Vector3f(0, 20, 0));
        //player.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        Globals.player=player;
    
        bulletAppState.getPhysicsSpace().add(player);
        //bulletAppState.setDebugEnabled(true);
        
        
     // Zet een standaard achtergrond lucht
        defaultSky = SkyFactory.createSky(assetManager,
        								  assetManager.loadTexture("background/daylight/DaylightBox_Right.bmp"),
        								  assetManager.loadTexture("background/daylight/DaylightBox_Left.bmp"), 
        								  assetManager.loadTexture("background/daylight/DaylightBox_Back.bmp"),
                						  assetManager.loadTexture("background/daylight/DaylightBox_Front.bmp"), 
                						  assetManager.loadTexture("background/daylight/DaylightBox_Top.bmp"), 
                						  assetManager.loadTexture("background/daylight/DaylightBox_Bottom.bmp"));
     	getRootNode().attachChild(defaultSky);
     	System.out.println("Init done, ready to place the first block");
        
        visibleChunkField=new VisibleChunkField(rootNode);
        for(int tel=0;tel<20;tel++)
        {
        	if(visibleChunkField.updateChunkField(0, 0))continue;
        }
        
        
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        
        nifty = niftyDisplay.getNifty();
        
        nifty.fromXml("interface/HelloJme.xml", "start", this);
        Element msg=nifty.getScreen("start").findElementById("msg");
        message=msg.getRenderer(TextRenderer.class);
        
        Element block=nifty.getScreen("start").findElementById("blockname");
        blockname=block.getRenderer(TextRenderer.class);
        this.guiViewPort.addProcessor(niftyDisplay);
        
	}
	
	
	@Override
    public void simpleUpdate(float tpf) {
		//Camera thiscam=getCamera();
		Vector3f campos=cam.getLocation();
		
		int chunkPosX= (int)((campos.x + Globals.CHUNKRADIUS) / (Globals.CHUNKSIZE) );
		if(campos.x+ Globals.CHUNKRADIUS<0)chunkPosX--;
		int chunkPosZ= (int)((campos.z + Globals.CHUNKRADIUS)  / (Globals.CHUNKSIZE) );
		if(campos.z+ Globals.CHUNKRADIUS<0)chunkPosZ--;
		
		
		// Update de chunk field
		visibleChunkField.updateChunkField(chunkPosX, chunkPosZ);
		
		chunkInfoText.setText(new StringBuilder().append("Chunk ").append(chunkPosX).append('X').append(chunkPosZ).toString());
		//locationInfoText.setText(new StringBuilder().append("location X:").append(campos.x).append(" Z:").append(campos.z).toString());
		//System.out.println(" "+campos.x+ " "+campos.z);
		
		// Beweeg de player
		camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
	}
	
	
	
	
/*	public List<String> getLocalChunkField(int chunkPosX, int chunkPosZ)
	{
		List<String> chunckFieldList=new ArrayList<>();
		for(int x=-Globals.chunkFieldSize;x<Globals.chunkFieldSize;x++)
		{
			for(int z=-Globals.chunkFieldSize;z<Globals.chunkFieldSize;z++)
			{
				chunckFieldList.add(new StringBuilder().append(chunkPosX+x).append('X').append(chunkPosZ+z).toString());
			}
		}		
		return chunckFieldList;
	}*/
	
	
	public void initKeys()
	{
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
	    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
	    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
	    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
	    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
	    
	    inputManager.addMapping("Create", new KeyTrigger(KeyInput.KEY_C));
	    inputManager.addMapping("Remove", new KeyTrigger(KeyInput.KEY_R));
	    inputManager.addMapping("Neutral", new KeyTrigger(KeyInput.KEY_N));
	    inputManager.addMapping("BlockSelect", new KeyTrigger(KeyInput.KEY_I));
	    
	    
	    inputManager.addListener(this, "Left");
	    inputManager.addListener(this, "Right");
	    inputManager.addListener(this, "Up");
	    inputManager.addListener(this, "Down");
	    inputManager.addListener(this, "Jump");
	    inputManager.addListener(this, "Create");
	    inputManager.addListener(this, "Remove");
	    inputManager.addListener(this, "Neutral");
	    inputManager.addListener(this, "BlockSelect");
	    
	    
	    inputManager.addMapping("select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
	    inputManager.addMapping("delete", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
	    inputManager.addListener(this, "select");
	    inputManager.addListener(this, "delete");
	    
	}

	protected void initCrossHairs() {
	    setDisplayStatView(false);
	    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
	    BitmapText ch = new BitmapText(guiFont, false);
	    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
	    ch.setText("+"); // crosshairs
	    ch.setLocalTranslation( // center
	      settings.getWidth() / 2 - ch.getLineWidth()/2,
	      settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
	    guiNode.attachChild(ch);
	  }
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		
		//System.out.println("Key pressed "+Globals.bulletAppState.getPhysicsSpace().getRigidBodyList().size());
		
		// Voor het plaatsen van een block
		if (name.equals("select") && isPressed) {
			//System.out.println("Mouse Button clicked");
			if (blockmode == 1) {
				collisionResults.clear();
				CollisionResult closest=null;
				// 2. Aim the ray from cam loc to cam direction.
				Ray ray = new Ray(cam.getLocation(), cam.getDirection());
				rootNode.collideWith(ray, collisionResults);
				Iterator<CollisionResult> cResult=collisionResults.iterator();
				while(cResult.hasNext())
				{
					closest=cResult.next();
					if(!closest.getGeometry().getName().equals("Sky"))
						{ System.out.println("closest "+closest.getGeometry().getName());
						  break;
						}
				}
				
				if (closest != null) {
					Geometry target = closest.getGeometry();
					Vector3f hitPoint = closest.getContactPoint();
					System.out.println(" Node " + target.getName() + " chunk " + target.getUserData("chunkid"));
					System.out.println("x:" + hitPoint.getX() + " y:" + hitPoint.getY() + " z:" + hitPoint.getZ());
					if (target.getUserData("chunkid") != null) {
						visibleChunkField.addNewBlock(hitPoint,BlockType.getCurBlock()    );
					}
				}
			}

		}
		
		// voor het verwijderen van een block
		if (name.equals("delete") && isPressed) 
		{
			collisionResults.clear();
			CollisionResult closest=null;
			// 2. Aim the ray from cam loc to cam direction.
			Ray ray = new Ray(cam.getLocation(), cam.getDirection());
			rootNode.collideWith(ray, collisionResults);
			Iterator<CollisionResult> cResult=collisionResults.iterator();
			while(cResult.hasNext())
			{
				closest=cResult.next();
				if(!closest.getGeometry().getName().equals("Sky"))
					{ System.out.println("closest "+closest.getGeometry().getName());
					  break;
					}
			}
			if(closest.getGeometry().getName().equals("ComputerFloor"))return;
			
			visibleChunkField.removeBlock(closest.getGeometry());
			return;
		}
		
		
		
		
		if (name.equals("Left")) {
		      left = isPressed;
		    } 
		else if (name.equals("Right")) {
		      right= isPressed;
		    } 
		else if (name.equals("Up")) {
		      up = isPressed;
		    } 
		else if (name.equals("Down")) {
		      down = isPressed;
		    } 
		else if (name.equals("Jump")) {      
		      if (isPressed) { player.jump(new Vector3f(0,20f,0));}
		    }
		
		if(name.equals("Create"))
		{
			message.setText("Create object");
			this.blockmode=1;
		}
		
		if(name.equals("Remove"))
		{
			message.setText("Remove object");
			this.blockmode=2;
		}
		
		if(name.equals("Neutral"))
		{
			message.setText("Neutral");
			this.blockmode=0;
		}
		
		if(name.equals("BlockSelect") && isPressed)
		{
			blockname.setText(BlockType.next().name());
			//this.blockmode=0;
		}
	}



	@Override
	public void bind(Nifty nifty, Screen screen) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}
	

}
