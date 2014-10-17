package Entity;

import java.awt.image.BufferedImage;

public class Animation {
	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean isPlayedOnce;
	
	public Animation() {
		isPlayedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames){
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		isPlayedOnce = false;
	}
	
	public void setDelay(long delayNumber) {
		delay = delayNumber;
	}
	
	public int getFrame() {
		return currentFrame;
	}
	
	public void setFrame(int frameNumber) {
		currentFrame = frameNumber;
	}
	
	public BufferedImage getImage(){
		return frames[currentFrame];
	}
	
	public boolean hasPlayedOnce() {
		return isPlayedOnce;
	}
	
	public void update() {
		// no animation
		if(delay == -1){
			return;
		}
		
		long elapsedTime = (System.nanoTime() - startTime) / 1000000;
		if(elapsedTime > delay){
			currentFrame++;
			 startTime = System.nanoTime();
		}
		
		if(currentFrame == frames.length){
			currentFrame = 0;
			isPlayedOnce = true;
		}
	}
}
