package gfx;

import java.awt.image.BufferedImage;

import map.GameMap;

public class OneTimeAnimation extends AnimationManager{
    public boolean running = true;
    public OneTimeAnimation(BufferedImage image){
        super(image);
    }
    public void update() {
        if (running){
            currentFrameTime++;

            if (currentFrameTime >= updatesPerFrame){
                currentFrameTime = 0;
                frameIndex++;
    
                if (frameIndex >= currentAnimationSheet.getHeight() / height - 1){
                    running = false;
                }
            }
        }
    }
}
