package de.stylextv.dither.util;

import java.awt.image.BufferedImage;

public class MP4Frame {
	
	public int frameID;
	public BufferedImage image;
	
	public MP4Frame(BufferedImage image, int frameID) {
		this.frameID=frameID;
		this.image=image;
	}
	
}
