package com.sande.lioncraft.blockcase;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.jme3.asset.AssetNotFoundException;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.sande.lioncraft.Globals;
import com.sun.prism.TextureMap;

public class LetterBlock extends BasicBlock {


	private Random randje=new Random();
	
	
	String content="emtpy";
	
	protected Geometry creatBlock()
	{
		Box box = new Box(Globals.BLOCKRADIUS,Globals.BLOCKRADIUS,Globals.BLOCKRADIUS);
		Geometry cubeSpatial = new Geometry("LetterBlock", box);	
		
		Material material = new Material(Globals.assetmanager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap",wrapTexture("Dit is een hele dikke text en ik hoop dat er dan ook voldoende informatie op kkomt te staan en dat het leesbaar is"));
		
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
		
		return new StringBuilder().append("C ").append(created).append(" U ").append(reused).append("  R ").append(returned).append("  LetterBlock").toString();
	}
	
	
	public Texture2D makeTextImage(String text)
	{
		BufferedImage image=new BufferedImage(512,512,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setFont(new Font("SansSerif", Font.PLAIN, 70));

		g2d.setColor(Color.GREEN);

		g2d.drawString(text, 0, 70);

		Texture2D text2d=createTexture(image,g2d);
		
		return text2d;
	}
	
	
	public Texture2D createTexture(BufferedImage img, Graphics2D g)
	{
		if (g != null)
			g.dispose();
		AWTLoader loader = new AWTLoader();
		Texture2D tex = new Texture2D(loader.load(img, true)); // changes to
																// image
																// parameter in
																// buffer,
																// affect the
																// texture.

		tex.setMagFilter(Texture.MagFilter.Nearest);
		tex.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
		return tex;
	}

	
	public Texture2D drawTexture(String text)
	{
		int stringSize=text.length();
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = img.createGraphics();
	    Font font = new Font("Arial", Font.PLAIN, 70);
	    g2d.setFont(font);
	    FontMetrics fm = g2d.getFontMetrics();
	    int width = fm.stringWidth(text);
	    int height = fm.getHeight();
	    g2d.dispose();

	    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    g2d = img.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	    g2d.setFont(font);
	    fm = g2d.getFontMetrics();
	    g2d.setColor(Color.GREEN);
	    g2d.drawString(text, 0, fm.getAscent());
	    g2d.dispose();
	    
	    AWTLoader loader = new AWTLoader();
	    Texture2D tex = new Texture2D(loader.load(img, true));

	    tex.setMagFilter(Texture.MagFilter.Nearest);
	    tex.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
	    return tex;
		
	}
	
	
	public Texture2D wrapTexture(String text)
	{
		int imageSize=1024;
		int stringSize=text.length();
		char[] textList=text.toCharArray();
		
		BufferedImage img = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = img.createGraphics();
	    
	    Font font = new Font("Arial", Font.PLAIN, 70);
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
		
	}
	
	
	
	public void setText(String content)
	{
		this.content=content;
	}

}
