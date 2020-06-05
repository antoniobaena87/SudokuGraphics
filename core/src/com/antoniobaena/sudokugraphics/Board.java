/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniobaena.sudokugraphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.ArrayList;

/**
 *
 * @author antonio
 */
class Board{
    
    private Manager manager;
    
    static final int BOARD_SIZE = 9;
    
    private Cell[][] cells = new Cell[BOARD_SIZE][BOARD_SIZE];
    
    private int missingNumbers = BOARD_SIZE * BOARD_SIZE;
    
    /**
     * 
     * @param board int[][] containing the numbers of the sudoku in it's initial state.
     * Zero if no number is present
     */
    public Board(int[][] board, Manager manager){
        super();
        this.manager = manager;
        // Check the format is valid
        if(board.length != BOARD_SIZE) throw new IllegalArgumentException();
        for(int i = 0; i < board.length; i++){
            
            if(board[i].length != BOARD_SIZE){
                throw new IllegalArgumentException();
            }
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] < 0 || board[i][j] > BOARD_SIZE){
                    throw new IllegalArgumentException();
                }
                // Create cell
                this.cells[i][j] = new Cell(board[i][j], i, j);
                if(board[i][j] != 0) missingNumbers -= 1;
            }
        }
    }
    
    public void draw(Batch batch){
        for(Cell[] row:cells){
            for(Cell cell:row){
                cell.draw(batch);
            }
        }
    }
    
    public Cell[][] getCells(){
        return cells;
    }
    
    public boolean isSolved(){
        if(missingNumbers == 0) return true;
        else return false;
    }
    
    public void substractMissingNumber(){
        missingNumbers -= 1;
    }
    
    public void update(float dt){
        for(Cell[] row:cells){
            for(Cell cell:row){
                cell.update(dt);
            }
        }
        // get next step
        ArrayList<Action> nextStep = manager.getNextStep();
        for(Action action:nextStep){
            executeAction(action);
        }
    }
    
    private void executeAction(Action action){
        //System.out.println(action);
        Cell cell = getCell(action);
        if(action.getAction() == Manager.PossibleActions.removePossibleNumber){
            cell.removePossibleNumber(action.getNumber());
        }else if(action.getAction() == Manager.PossibleActions.setSelected){
            cell.setSelected();
        }else if(action.getAction() == Manager.PossibleActions.setIterated){
            cell.startIterateAnimation();
        }else if(action.getAction() == Manager.PossibleActions.setNumber){
            cell.setNumber(action.getNumber());
        }else if(action.getAction() == Manager.PossibleActions.setHiddenPair){
            cell.setHiddenPair();
        }
    }
    
    private Cell getCell(Action action){
        return cells[action.getPosx()][action.getPosy()];
    }
    
}
