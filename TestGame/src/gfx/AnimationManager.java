package gfx;

import java.awt.Image;
import java.awt.image.BufferedImage;

import map.GameMap;

public class AnimationManager {
    protected BufferedImage currentAnimationSheet;
    protected int updatesPerFrame;
    protected int currentFrameTime;
    protected int frameIndex;
    protected int height;
    public AnimationManager(BufferedImage image){
        this.updatesPerFrame = 10;
        this.frameIndex = 0;
        this.currentFrameTime = 0;
        this.currentAnimationSheet = image;
    }
    public Image getSprite(){
        return currentAnimationSheet.getSubimage(
            0,
            frameIndex * GameMap.TILESIZE,
            GameMap.TILESIZE,
            GameMap.TILESIZE
        );
    }
    public Image getSprite(int x, int y, int width, int height){
        this.height = height;
        return currentAnimationSheet.getSubimage(
            x,
            frameIndex * height + y,
            width,
            height
        );
    }
    public void update() {
        currentFrameTime++;
        
        if (currentFrameTime >= updatesPerFrame){
            currentFrameTime = 0;
            frameIndex++;

            if (frameIndex >= currentAnimationSheet.getHeight() / height - 1){
                frameIndex = 0;
            }
        }
    }
}
