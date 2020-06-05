package com.mygdx.sudoku.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.antoniobaena.sudokugraphics.Sudoku;

public class DesktopLauncher {
	public static void main (String[] arg) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "Sudoku visual solver";
            config.foregroundFPS = 60;
            // width must be the size of the cells * 9 plus the spaces in between
            config.width = 800;  // remember, changing this doesn't change the viewport!!
            // height must be the size of the cells * 9 plus the spaces in between
            config.height = 800;
            new LwjglApplication(new Sudoku(), config);
	}
}
