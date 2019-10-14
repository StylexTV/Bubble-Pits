package de.stylextv.dither.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class VideoObject {
	
	protected double compX;
	protected double compY;
	
	protected BufferedImage canvas;
	protected Graphics2D canvasGraphics;
	protected int canvasWidth, canvasHeight,currentFrameIndex;
	
	public VideoObject(double x, double y) {
		this.compX=x;
		this.compY=y;
	}
	
	public void setupRender(BufferedImage canvas, Graphics2D canvasGraphics, int canvasWidth, int canvasHeight, int currentFrameIndex) {
		this.canvas=canvas;
		this.canvasGraphics=canvasGraphics;
		this.canvasWidth=canvasWidth;
		this.canvasHeight=canvasHeight;
		this.currentFrameIndex=currentFrameIndex;
	}
	public abstract void onRender();
	
	public double getCompX() {
		return compX;
	}
	public double getCompY() {
		return compY;
	}
	public void setCompX(double x) {
		this.compX = x;
	}
	public void setCompY(double y) {
		this.compY = y;
	}
	
}
