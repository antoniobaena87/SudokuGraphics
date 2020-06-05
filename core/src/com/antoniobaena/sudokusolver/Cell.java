/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniobaena.sudokusolver;

import java.util.HashMap;

/**
 *
 * @author antonio
 */
public class Cell {
    private int posx, posy;
    
    private int number = -1;
    private HashMap<Integer, Integer> possibleNumbers;
    private HashMap<Integer, Integer> toRemove;
    
    boolean isHiddenPair;
    
    public Cell(int number, int posx, int posy){
        this.posx = posx;
        this.posy = posy;
        possibleNumbers = new HashMap<>();
        toRemove = new HashMap<>();
        
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
    
    public int getPosx(){
        return posx;
    }
    public int getPosy(){
        return posy;
    }
    
    public int getNumber(){
        return number;
    }
    
    public int getPossibleNumbersSize(){
        return possibleNumbers.size();
    }
    
    public boolean isHiddenPair(){
        return isHiddenPair;
    }
    
    public void setHiddenPair(boolean hiddenPair){
        this.isHiddenPair = hiddenPair;
    }
    
    public void addPossibleNumberToRemove(int number){
        toRemove.put(number, number);
    }
    
    /**
     * For testing purposes only.
     * @return 
     */
    public HashMap<Integer, Integer> getPossibleNumbers(){
        return possibleNumbers;
    }
    
    public boolean hasPossibleNumber(int number){
        return possibleNumbers.containsKey(number);
    }
    
    /**
     * 
     * @param number
     * @return -1 if there are still more than one key in possibleNumbers
     *          the number that is set if there is only one remaining key
     * 
     * For example, if the HashMap contains two keys, 1 and 2, and the method is called
     * to remove the number 1, that means there is only one option for this cell's number, which
     * is the number 2. Then, the number 2 will be returned. If there were still more numbers, then
     * the method would return -1.
     */
    public int removePossibleNumber(int number, Board board){
        assert(possibleNumbers.size() > 1);
        assert(possibleNumbers.get(number) != null);
        
        possibleNumbers.remove(number);
        if(possibleNumbers.size() == 1){
            for(int key:possibleNumbers.keySet()){
                System.out.println("Set number by discarding " + key + " in row " + posx + ", column " + posy);
                this.number = key;
                possibleNumbers.clear();
                board.substractMissingNumber();
                
                return number;
            }
        }
        
        return -1;
    }
    
    public void setNumber(int number, Board board){
        System.out.println("Set number " + number + " in row " + posx + ", column " + posy);
        this.number = number;
        possibleNumbers.clear();
        board.substractMissingNumber();
    }
}
