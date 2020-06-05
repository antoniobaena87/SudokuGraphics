package com.antoniobaena.sudokugraphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.antoniobaena.sudokugraphics.Board;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Sudoku extends Game {

    Board board;
    Manager manager;
    
    static final int WORLD_WIDTH = 1100;
    static final int WORLD_HEIGHT = 1100;
    OrthographicCamera camera;
    Viewport viewPort;
    
    SpriteBatch batch;
        
    @Override
    public void create () {
        
        int[][] boardArray = new int[][]{
            {6,7,1,0,0,8,0,0,5},
            {0,3,0,0,0,0,0,0,8},
            {0,0,0,2,0,0,0,6,0},
            {0,0,0,0,9,0,0,0,1},
            {0,4,0,3,8,5,0,7,0},
            {8,0,0,0,1,0,0,0,0},
            {0,8,0,0,0,3,0,0,0},
            {9,0,0,0,0,0,0,4,0},
            {2,0,0,8,0,0,3,5,9}
        };
        
        manager = new Manager();
        // Solve the sudoku and fill the manager with the steps needed
        com.antoniobaena.sudokusolver.Board prevBoard = new com.antoniobaena.sudokusolver.Board(boardArray, manager);
        while(!prevBoard.isSolved()){
            prevBoard.loopBoard();
        }
        float aspectRatio = 1.0f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera = new OrthographicCamera();
        viewPort = new FitViewport(WORLD_WIDTH * aspectRatio, WORLD_HEIGHT, camera);
        viewPort.apply();
        camera.setToOrtho(false);

        batch = new SpriteBatch();

        board = new Board(boardArray, manager);
    }

    @Override
    public void resize(int width, int height){
        viewPort.update(width, height);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2 - 100, 0);
        camera.update();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        float dt = Gdx.graphics.getDeltaTime();
        board.update(dt);

        batch.begin();
        board.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
    }
}
