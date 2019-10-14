package de.stylextv.dither.util;

import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class MP4Writer {
	
	public String file,encodingSpeed,imageEncoder;
	public double fps;
	public int maxBufferSize;
	public boolean debug;
	
	private CopyOnWriteArrayList<MP4Frame> inputList=new CopyOnWriteArrayList<MP4Frame>();
	private boolean stop=false;
	public boolean finished=false, processFinished=false;
	
	public MP4Writer(String file, double fps, String encodingSpeed, String imageEncoder, int maxBufferSize, boolean debug) {
		this.file=file;
		this.fps=fps;
		this.encodingSpeed=encodingSpeed;
		this.imageEncoder=imageEncoder;
		this.maxBufferSize=maxBufferSize;
		this.debug=debug;
	}
	
	public boolean canAddToBuffer() {
		return inputList.size()<maxBufferSize;
	}
	public void waitForBuffer() {
		while(!canAddToBuffer()) {try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
	}
	public void waitForBuffer(int sleepTime) {
		while(!canAddToBuffer()) {try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
	}
	public void addToBuffer(MP4Frame input) {
		if(inputList.size()<maxBufferSize) {
			inputList.add(input);
		}
	}
	public int getBufferSize() {
		return inputList.size();
	}
	public void waitFor() {
		while(!processFinished) {try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
	}
	private int framesTotal=-1;
	public void waitForFinish() {
		while(inputList.size()>0) {try {
			if(framesTotal==-1) framesTotal=inputList.size();
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
	}
	
	private byte[] byteBuffer = new byte[1920*1080*4];
	
	public OutputStream ffmpegOutput=null;
	public void start() {
		stop=false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				
				try {
					File fi=new File(file);
					if(fi.exists()) fi.delete();
					
					String cmd="ffmpeg -threads 0 -framerate "+fps+" -f rawvideo -pix_fmt rgba -video_size "+1920+"x"+1080+" -i -"+" "+"-vcodec libx264 -crf 15 -q:a 100 -pix_fmt yuv420p -preset "+encodingSpeed+" -r "+fps+" "+file;
					
			        Process p = Runtime.getRuntime().exec(cmd);
			        
			        ffmpegOutput = p.getOutputStream();
			        new Thread(new Runnable() {
			    		@Override
			    		public void run() {
			    			try {
			    			    String line;
			    			    BufferedReader in2 = new BufferedReader(
			    			            new InputStreamReader(p.getErrorStream()));
			    			    while ((line = in2.readLine()) != null) {
			    			        if(debug) System.out.println(file+": "+line);
			    			    }
			    			    in2.close();
			    			    processFinished=true;
			    			} catch (IOException e) {
			    				System.out.println("ERROR IN FFMPEG WRITER LOGGER");
			    				e.printStackTrace();
			    			}
			    		}
			    	}).start();
			        
			        while(!(stop&&inputList.isEmpty())) {
			        	if(inputList.size()>0) {
			        		
			        		final MP4Frame frame=inputList.remove(0);
							long before=System.currentTimeMillis();
							
					        DataBufferByte dbb=(DataBufferByte) frame.image.getData().getDataBuffer();
					        
			            	byte[] bytes = dbb.getData();
			            	for(int i=0; i<bytes.length; i++) {
			            		int channel=i%4;
			            		int pixel=i/4*4;
			            		channel=-(channel-3);
			            		byteBuffer[i]=bytes[pixel+channel];
			            	}
							if(!stop) ffmpegOutput.write(byteBuffer, 0, byteBuffer.length);
			        		long after=System.currentTimeMillis();
			        		System.out.println("#"+frame.frameID+" SENT; SENDING TOOK: "+(after-before));
			        		
			        	} else {
			        		Thread.sleep(2500);
			        	}
			        }
				    finished=true;
					if(ffmpegOutput!=null) {
						try {
							ffmpegOutput.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				  } catch(Exception ex) {ex.printStackTrace();}
				
				
			}
		}).start();
	}
	public void stop() {
		stop=true;
	}
	
}
