package de.stylextv.dither.render;

import java.awt.Color;
import java.awt.image.BufferedImage;

import de.stylextv.dither.util.MathUtil;
import de.stylextv.dither.util.SimplexNoise;

public class BubblePit extends VideoObject {
	
	private BufferedImage buffer1,buffer2;
	private int width, height, seed;
	
	public BubblePit(double x, double y, int width, int height) {
		super(x, y);
		this.width=width;
		this.height=height;
		buffer1=new BufferedImage(width+100, height, BufferedImage.TYPE_INT_RGB);
		buffer2=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		seed=MathUtil.getRandom().nextInt(10000);
	}
	
	@Override
	public void onRender() {
		double z=currentFrameIndex*4;
		for(int x=0; x<buffer1.getWidth(); x++) {
			for(int y=0; y<buffer1.getHeight(); y++) {
				int r=calcPoint(z, x-25, y, seed);
				int g=calcPoint(z, x-25+100, y, seed);
				int b=calcPoint(z, x-25+150, y, seed);
				buffer1.setRGB(x, y, new Color(r,g,b).getRGB());
			}
		}
		
		for(int x=0; x<buffer2.getWidth(); x++) {
			for(int y=0; y<buffer2.getHeight(); y++) {
				int rx=(int) (x+25);
				int x0=(int) (rx-25);
				int x1=(int) (rx+25);
				Color c0=new Color(buffer1.getRGB(x0, y));
				Color c1=new Color(buffer1.getRGB(x1, y));
				
				int r=c0.getRed();
				int g=c1.getGreen();
				int b=c1.getBlue();
				
				buffer2.setRGB(x, y, new Color(r,g,b).getRGB());
			}
		}
		
		canvasGraphics.translate(compX, compY);
		canvasGraphics.drawImage(buffer2, 0,0,width,height, null);
		canvasGraphics.translate(-compX, -compY);
	}
	
	private static int calcPoint(double z, double x, double y, double seed) {
		double f=1080;
		double d=SimplexNoise.noise(x/f+seed, y/f+seed, z/f);
		d=Math.abs(d);
		return (int) Math.round(d*255);
	}
	
}
