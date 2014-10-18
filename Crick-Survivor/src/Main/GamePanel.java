package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import GameState.GameStateManager;
import Handlers.Keys;

// @SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {
	// dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	// game thread
	private Thread thread;
	private boolean isRunning;
	private int fps =60;
	private long targetTime = 1000 / fps;
	
	// image
	private BufferedImage image;
	private Graphics2D graphics; 
	
	// game state manager
	private GameStateManager gameStateManager;
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE ));
		requestFocus();
	}
	
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	public void init(){
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) image.getGraphics();
		isRunning = true;
		gameStateManager = new GameStateManager();
	}
	
	public void run(){
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(isRunning){
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0){
				wait = 5;
			}
			
			try{
				Thread.sleep(wait);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void update(){
		gameStateManager.update();
	}
	
	private void draw(){
		gameStateManager.draw(graphics);
	}
	
	private void drawToScreen() {
		Graphics graphics2 = getGraphics();
		graphics2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		graphics2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
    public void keyPressed(KeyEvent key) {
        if(key.isControlDown()) {
            if(key.getKeyCode() == KeyEvent.VK_R) {
                //recording = !recording;
                return;
            }
            if(key.getKeyCode() == KeyEvent.VK_S) {
                //screenshot = true;
                return;
            }
        }
        Keys.keySet(key.getKeyCode(), true);
    }
    public void keyReleased(KeyEvent key) {
        Keys.keySet(key.getKeyCode(), false);
    }
}
