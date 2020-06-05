/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniobaena.sudokugraphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import java.util.HashMap;

/**
 *
 * @author antonio
 */
class Cell{
    
    private Texture backgroundTexture;
    private Texture selectedTexture;
    private Texture iterateTexture;
    private Texture hiddenPairTexture;
    private Sprite backgroundSprite;
    private Sprite animationSprite;
    private Sprite selectedSprite;
    private Sprite hiddenPairSprite;
    
    private float iterateAlpha;
    private float selectedAlpha;
    private float hiddenPairAlpha;
    
    private final int SIZE = 100;
    
    // Clocks and booleans for animations
    private float iterateAnimationTimer;
    private boolean iterateAnimation;
    private float setNumberAnimationTimer;
    private boolean setNumberAnimation;
    
    private int posx, posy;  // position within board [i][j]
    private static int gapx, gapy;  // gapx are the vertical black lines, gapy the horizontal
    private int worldx, worldy;  // x and y position within world
    
    private BitmapFont numFont;
    private BitmapFont[] possibleFonts = new BitmapFont[Board.BOARD_SIZE];
    
    private int number = -1;
    private HashMap<Integer, Integer> possibleNumbers;
    
    boolean isHiddenPair;
    boolean isSelected;
    
    public Cell(int number, int posx, int posy){
        super();
        initFont();
        
        this.posx = posx;
        this.posy = posy;
        worldx = posy * SIZE;
        worldy = (9 - posx) * SIZE;  // so position 0 is at the top of the screen and 9 at the bottom
        
        backgroundTexture = new Texture(Gdx.files.internal("cell.png"));
        selectedTexture = new Texture(Gdx.files.internal("selected.png"));
        iterateTexture = new Texture(Gdx.files.internal("iteration.png"));
        hiddenPairTexture = new Texture(Gdx.files.internal("hiddenPair.png"));
        
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setTexture(backgroundTexture);
        backgroundSprite.setSize(SIZE, SIZE);
        setPosition();
        animationSprite = new Sprite(iterateTexture);
        animationSprite.setPosition(backgroundSprite.getX(), backgroundSprite.getY());
        selectedSprite = new Sprite(selectedTexture);
        selectedSprite.setPosition(backgroundSprite.getX(), backgroundSprite.getY());
        hiddenPairSprite = new Sprite(hiddenPairTexture);
        hiddenPairSprite.setPosition(backgroundSprite.getX(), backgroundSprite.getY());
        
        possibleNumbers = new HashMap<>();
        
        if(number != 0) this.number = number;
        else{
            possibleNumbers.put(1, 1);
            possibleNumbers.put(2, 2);
            possibleNumbers.put(3, 3);
            possibleNumbers.put(4, 4);
            possibleNumbers.put(5, 5);
            possibleNumbers.put(6, 6);
            possibleNumbers.put(7, 7);
            possibleNumbers.put(8, 8);
            possibleNumbers.put(9, 9);
        }
    }
    
    private void initFont(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pacifico.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 64;
        numFont = generator.generateFont(parameter);
        numFont.setColor(Color.BLACK);
        
        parameter.size = 18;
        for(int i = 0; i < Board.BOARD_SIZE; i++){
            possibleFonts[i] = generator.generateFont(parameter);
            possibleFonts[i].setColor(Color.BLACK);
        }
        generator.dispose();
    }
    
    private void setPosition(){
        int gap = 10;
        
        if(posx == 0) gapy = -20;  // initialize it so the first row will be drawn with a black border on top of it
        
        if(posy % 3 == 0) gap = 20;
        gapx += gap;
        
        worldx += gapx;
        worldy += gapy;
        
        backgroundSprite.setPosition(worldx, worldy);
        
        if(posy == Board.BOARD_SIZE - 1){
            gapx = 0;
            gap = 10;
            if(posx == 2 || posx == 5 || posx == 8) gap = 20;
            gapy -= gap;  // substract cause y decreases as we go down the screen
        }
    }
    
    public void draw(Batch batch){
        // Draw the sprites
        backgroundSprite.draw(batch);
        if(iterateAnimation) animationSprite.draw(batch, iterateAlpha);
        if(selectedAlpha > 0) selectedSprite.draw(batch, selectedAlpha);
        if(isHiddenPair) hiddenPairSprite.draw(batch, hiddenPairAlpha);
        
        // Draw the number on it, if the number isn't -1 (meaning it's set)
        String num;
        if(number != -1){
            numFont.draw(batch, String.valueOf(number), worldx + SIZE * 0.35f, worldy + SIZE * 0.9f);
        }else{
            for(int key:possibleNumbers.keySet()){
                int fposx = 0;
                int fposy = 0;
                if(key == 2 || key == 5 || key == 8) fposy = 20;
                else if(key == 3 || key == 6 || key == 9) fposy = 40;
                if(key == 4 || key == 5 || key == 6) fposx = 20;
                else if(key == 7 || key == 8 || key == 9) fposx = 40;
                
                possibleFonts[key-1].draw(batch, String.valueOf(key), worldx + 10 + fposx, worldy + 10 + SIZE * 0.9f - fposy);
            }
        }
    }
    
    protected int getPosx(){
        return posx;
    }
    protected int getPosy(){
        return posy;
    }
    
    protected void removePossibleNumber(int number){
        possibleNumbers.remove(number);
    }
    
    protected void setNumber(int number){
        this.number = number;
        possibleNumbers.clear();
    }
    
    public void update(float dt){
        
        if(iterateAnimation){
            iterateAlpha -= 2f * dt;
            if(iterateAlpha <= 0) iterateAnimation = false;
        }
        if(setNumberAnimation){
            iterateAlpha -= 0.1f * dt;
            if(iterateAlpha <= 0) setNumberAnimation = false;
        }
        if(!isSelected && selectedAlpha > 0){
            selectedAlpha -= 1f * dt;
        }
    }
    
    public void startIterateAnimation(){
        iterateAnimation = true;
        animationSprite.setTexture(iterateTexture);
        iterateAlpha = 1;
    }
    
    public void startSetNumberAnimation(){
        setNumberAnimation = true;
        //animationSprite.setTexture();
        iterateAlpha = 1;
    }
    
    public void setHiddenPair(){
        if(!isHiddenPair){
            isHiddenPair = true;
            hiddenPairAlpha = 1;
        }
        if(isHiddenPair) isHiddenPair = false;
    }
    
    /**
     * Sets the texture for when the cell is selected for discarding possible numbers
     */
    public void setSelected(){
        if(!isSelected){
            isSelected = true;
            selectedAlpha = 1;
        }
        else{
            isSelected = false;
        }
    }
}
