/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniobaena.sudokusolver;

import com.antoniobaena.sudokugraphics.Manager;

import java.util.HashMap;

/**
 * This class represents the sudoku. It's built using cells of the class Cell.java.<br>
 * Each cell contains a number or a list of possible numbers. Each time a cell gets its
 * number solved, the missingNumbers variable decreases by one. Once this variable reaches zero,
 * the sudoku is solved.
 * 
 * @see Cell
 * @author antonio
 */
public class Board {
    
    final int BOARD_SIZE = 9;
    
    Manager manager;
    
    Cell[][] cells = new Cell[BOARD_SIZE][BOARD_SIZE];
    private int missingNumbers = BOARD_SIZE * BOARD_SIZE;
    
    /**
     * 
     * @param board int[][] containing the numbers of the sudoku in it's initial state.
     * Zero if no number is present
     */
    public Board(int[][] board, Manager manager){
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
        // Create a manager instance
        this.manager = manager;
    }
    
    public boolean isSolved(){
        if(missingNumbers == 0) return true;
        else return false;
    }
    
    public void substractMissingNumber(){
        missingNumbers -= 1;
    }
    
    public void printBoard(){
        StringBuilder board = new StringBuilder();
        for(int i = 0; i < 9; i++){
            board.append(" ");
            for(int j = 0; j < 9; j++){
                board.append("----");
            }
            board.append("\n");
            for(int j = 0; j < 9; j++){
                if(cells[i][j].getNumber() == -1){
                    board.append(" | ").append(0);
                }else board.append(" | ").append(cells[i][j].getNumber());
            }
            board.append("\n");
        }
        
        System.out.println(board);
    }
    
    public boolean loopBoard(){
        //System.out.println("\nMissing numbers: " + missingNumbers);
        //printBoard();
        int prevMissingNumbers = missingNumbers;
        for(Cell[] row:cells){
            for(Cell cell:row){
                manager.startStep();
                manager.addAction(cell.getPosx(), cell.getPosy(), Manager.PossibleActions.setSelected, cell.getNumber());
                manager.endStep();
                if(cell.getNumber() == -1){  // No need to iterate if the cell already contains a number
                    iterRow(cell);
                    iterCol(cell);
                    iterInner(cell);
                }
                
                manager.startStep();
                manager.addAction(cell.getPosx(), cell.getPosy(), Manager.PossibleActions.setSelected, cell.getNumber());
                manager.endStep();
                
                /*
                if(cell.getPossibleNumbersSize() > 0){
                    System.out.println("\nCell " + cell.getPosx() + " " + cell.getPosy());
                    System.out.println("Possible numbers: " + cell.getPossibleNumbers().keySet());
                    System.out.println("Number: " + cell.getNumber());
                }
                */
            }
        }
        if (prevMissingNumbers == missingNumbers){
            //System.out.println("\nChecking lone numbers");
            prevMissingNumbers = missingNumbers;
            loneNumber();
        }
        findHiddenPairs();
        if(prevMissingNumbers == missingNumbers){
            //System.out.println("\nChecking hidden pairs");
            //findHiddenPairs();
        }
        
        // Update cells
        /*
        for(Cell[] row:cells){
            for(Cell cell:row){
                cell.update();
            }
        }
        */
        
        if(missingNumbers == 0) return true;
        return false;
    }
    
    private void iterRow(Cell currentCell){
        for(int j = 0; j < BOARD_SIZE; j++){
            if(j == currentCell.getPosy()) continue; // do not check itself
            manager.startStep();
            manager.addAction(currentCell.getPosx(), j, Manager.PossibleActions.setIterated, 0);
            if(cells[currentCell.getPosx()][j].getNumber() != -1){  // the cell has a number set
                int number = cells[currentCell.getPosx()][j].getNumber();
                if(currentCell.hasPossibleNumber(number)){
                    currentCell.removePossibleNumber(number, this);
                    manager.addAction(currentCell.getPosx(), currentCell.getPosy(), Manager.PossibleActions.removePossibleNumber, number);
                }
            }
            manager.endStep();
        }
    }
    
    private void iterCol(Cell currentCell){
        for(int i = 0; i < BOARD_SIZE; i++){
            if(i == currentCell.getPosx()) continue; // do not check itself
            manager.startStep();
            manager.addAction(i, currentCell.getPosy(), Manager.PossibleActions.setIterated, 0);
            if(cells[i][currentCell.getPosy()].getNumber() != -1){  // the cell has a number set
                int number = cells[i][currentCell.getPosy()].getNumber();
                if(currentCell.hasPossibleNumber(number)){
                    currentCell.removePossibleNumber(number, this);
                    manager.addAction(currentCell.getPosx(), currentCell.getPosy(), Manager.PossibleActions.removePossibleNumber, number);
                }
            }
            manager.endStep();
        }
    }
    
    private void iterInner(Cell currentCell){
        for(int i = (currentCell.getPosx() / 3) * 3; i < (currentCell.getPosx() / 3) * 3 + 3; i++){
            for(int j = (currentCell.getPosy() / 3) * 3; j < (currentCell.getPosy() / 3) * 3 + 3; j++){
                if(i == currentCell.getPosx() && j == currentCell.getPosy()) continue; // do not check itself
                manager.startStep();
                manager.addAction(i, j, Manager.PossibleActions.setIterated, 0);
                if(cells[i][j].getNumber() != -1){  // the cell has a number set
                    int number = cells[i][j].getNumber();
                    if(currentCell.hasPossibleNumber(number)){
                        currentCell.removePossibleNumber(number, this);
                        manager.addAction(currentCell.getPosx(), currentCell.getPosy(), Manager.PossibleActions.removePossibleNumber, number);
                    }
                }
                manager.endStep();
            }
        }
    }
    
    // REVIEW
    private void checkDiagonal(){
        for(int i = 0; i < BOARD_SIZE; i++){
            if(cells[i][i].getNumber() != -1) continue;
            cellPossibleNumbers:for(int possibleNumber:cells[i][i].getPossibleNumbers().keySet()){
                int number = -1;
                for(int j = i + 1; j < BOARD_SIZE - i; j++){
                    if(cells[j][j].getNumber() != -1){
                        // If the cell contains a set number and is the same as the possible number, then that possible
                        // number isn't a possibility and it shall be removed from possibleNumbers
                        if(cells[j][j].getNumber() == possibleNumber){
                            cells[j][j].addPossibleNumberToRemove(possibleNumber);
                            continue cellPossibleNumbers;
                        }
                    }
                    for(int otherPossibleNumber:cells[j][j].getPossibleNumbers().keySet()){
                        if(otherPossibleNumber == possibleNumber){
                            continue cellPossibleNumbers;
                        }
                    }
                    number = possibleNumber;
                }
                if(number != -1){
                    cells[i][i].setNumber(number, this);
                    manager.startStep();
                    manager.addAction(i, i, Manager.PossibleActions.setNumber, number);
                    manager.endStep();
                    break cellPossibleNumbers;
                }
            }
        }
    }
    
    /**
     * Check if a possible number in a cell is present as a possible number
     * in another cell within the same inner 3x3 square. If present only in this one cell, 
     * then that number has to be set in this cell.<br>
     * 
     * It also checks if a number is present only in the same column or row, thus meaning
     * that the number has to be in the cells withing that inner square belonging to that 
     * row or column. Thus, this number can be removed as a possible number in every cell 
     * of that row or column from outside that inner square.<br>
     */
    private void loneNumber(){
        method:for(Cell[] row:cells){
            for(Cell cell:row){
                outer:for(int possibleNumber:cell.getPossibleNumbers().keySet()){
                    boolean sameRow = true;
                    boolean sameCol = true;
                    boolean foundRepeated = false;
                    for(int i = (cell.getPosx() / 3) * 3; i < (cell.getPosx() / 3) * 3 + 3; i++){
                        inner:for(int j = (cell.getPosy() / 3) * 3; j < (cell.getPosy() / 3) * 3 + 3; j++){
                            if(i == cell.getPosx() && j == cell.getPosy()) continue; // do not check itself
                            if(cells[i][j].getNumber() == possibleNumber) continue outer;
                            for(int otherPossibleNumber:cells[i][j].getPossibleNumbers().keySet()){
                                if(otherPossibleNumber == possibleNumber){  // the number is present somewhere else
                                    foundRepeated = true;
                                    if(i != cell.getPosx()){
                                        sameRow = false;
                                    }
                                    if(j != cell.getPosy()){
                                        sameCol = false;
                                    }
                                    
                                    if(!sameCol && !sameRow) continue outer;
                                }
                            }
                        }
                    }
                    if(!foundRepeated){
                        cell.setNumber(possibleNumber, this);
                        manager.startStep();
                        manager.addAction(cell.getPosx(), cell.getPosy(), Manager.PossibleActions.setNumber, possibleNumber);
                        manager.endStep();
                        break outer;
                    }
                    if(sameRow){
                        assert(sameCol = false);
                        // assume the number goes in that row within that inner square
                        for(int j = 0; j < BOARD_SIZE; j++){
                            if(j < (cell.getPosy() / 3) * 3 || j > (cell.getPosy() / 3) * 3 + 3){
                                cells[cell.getPosx()][j].removePossibleNumber(possibleNumber, this);
                            }
                        }
                    }
                    if(sameCol){
                        assert(sameRow = false);
                        // assume the number goes in that column within that inner square
                        for(int i = 0; i < BOARD_SIZE; i++){
                            if(i < (cell.getPosx() / 3) * 3 || i >= (cell.getPosx() / 3) * 3 + 3){
                                cells[i][cell.getPosy()].removePossibleNumber(possibleNumber, this);
                                manager.startStep();
                                manager.addAction(i, cell.getPosy(), Manager.PossibleActions.removePossibleNumber, possibleNumber);
                                manager.endStep();
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * If two numbers are present in two cells within the same row/column/inner block, 
     * then those two numbers must be in those two cells and can be removed from any other cells in 
     * the row/column/inner square.
     * 
     * @return 1 if the method found a new hidden pair, 0 otherwise
     */
    private void findHiddenPairs(){  // TODO: check also rows and columns
        System.out.println("Finding hidding pairs");
        method:for(Cell[] row:cells){
            for(Cell cell:row){
                if(cell.isHiddenPair) continue; // if already checked as hidden pair, don't check again
                HashMap<Integer, Integer> pair = new HashMap<>();
                outer:for(int possibleNumber:cell.getPossibleNumbers().keySet()){ // loop through possible numbers in cell
                    int repeated = 0;
                    for(int i = (cell.getPosx() / 3) * 3; i < (cell.getPosx() / 3) * 3 + 3; i++){  // loop the inner square
                        for(int j = (cell.getPosy() / 3) * 3; j < (cell.getPosy() / 3) * 3 + 3; j++){
                            if (cells[i][j].isHiddenPair) continue;  // don't check a cell that's already marked as hidden pair
                            if(cells[i][j].getPossibleNumbers().containsKey(possibleNumber)){
                                if(pair.size() > 0){
                                    for(int key:pair.keySet()){
                                        if(cells[i][j].getPossibleNumbers().containsKey(key)){
                                            repeated += 1;
                                        }else continue outer;  // if the number is present but not with the possible pair
                                    }
                                }else repeated += 1;
                                if(repeated > 2){
                                    continue outer; // not valid, check the next possible number
                                }
                            }
                        }
                    }
                    if(repeated == 2) pair.put(possibleNumber, possibleNumber);
                }
                if(pair.size() == 2){
                    // found a hidden pair inside an inner block. Loop again inner block and remove any other
                    // possible number in those two cells where the hidden pair was found. Also remove the hidden pair
                    // numbers from any other cell in the block, row or column
                    for(int i = (cell.getPosx() / 3) * 3; i < (cell.getPosx() / 3) * 3 + 3; i++){
                        loop:for(int j = (cell.getPosy() / 3) * 3; j < (cell.getPosy() / 3) * 3 + 3; j++){
                            boolean cellContainsPair = false;
                            for(int possibleNumber:cells[i][j].getPossibleNumbers().keySet()){
                                if(pair.containsKey(possibleNumber)){
                                    cellContainsPair = true;
                                    break;
                                }
                            }
                            if(cellContainsPair){
                                cells[i][j].getPossibleNumbers().clear();
                                for(int pairNum:pair.keySet()){
                                    cells[i][j].getPossibleNumbers().put(pairNum, pairNum);
                                }
                                cells[i][j].setHiddenPair(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
