package de.stylextv.dither.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.stylextv.dither.render.BubblePit;
import de.stylextv.dither.util.MP4Frame;
import de.stylextv.dither.util.MP4Writer;
import de.stylextv.dither.util.RenderUtil;

public class Main {
	
	private static BubblePit pit=new BubblePit(0, 0, 1920, 1080);
	
	public static void main(String[] args) {
		MP4Writer writer=new MP4Writer("output.mp4", 60, "medium", "PNG", 100, true);
		writer.start();
		int frames=60*60*5;
		for(int i=0; i<frames; i++) {
			BufferedImage image=new BufferedImage(1920, 1080, BufferedImage.TYPE_4BYTE_ABGR);
			renderImage(image,i);
			writer.waitForBuffer(5000);
			writer.addToBuffer(new MP4Frame(image, i));
			System.out.println("RENDERED IMAGE #"+i+" ("+(int)(i/(frames-1.0)*100)+"%)");
		}
	    writer.waitForFinish();
	    writer.stop();
	    writer.waitFor();
	}
	private static void renderImage(BufferedImage image, int currentFrameIndex) {
		Graphics2D graphics=(Graphics2D) image.getGraphics();
		RenderUtil.setRenderingHints(graphics);
		graphics.setColor(new Color(255,255,255));
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		pit.setupRender(image, graphics, image.getWidth(), image.getHeight(), currentFrameIndex);
		pit.onRender();
	}
	
}
