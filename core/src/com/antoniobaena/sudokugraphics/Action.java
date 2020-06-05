/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniobaena.sudokugraphics;

/**
 *
 * @author antonio
 */
class Action {
    int[] cell;
    
    private Manager.PossibleActions action;
    private int number;  // for both setNumber and removePossibleNumber
    
    public Action(int posx, int posy, Manager.PossibleActions action, int number){
        cell = new int[] {posx, posy};
        if(action == Manager.PossibleActions.setNumber){
            if(number == -1) throw new IllegalArgumentException("Invalid number to set: -1");
            setNumber(number);
        }else if(action == Manager.PossibleActions.removePossibleNumber){
            if(number == -1) throw new IllegalArgumentException("Invalid possible number to remove: -1");
            removeNumber(number);
        }else if(action == Manager.PossibleActions.setSelected){
            this.action = action;
        }else if(action == Manager.PossibleActions.setIterated){
            this.action = action;
        }else if(action == Manager.PossibleActions.setHiddenPair){
            this.action = action;
        }
    }
    
    private void setNumber(int number){
        action = Manager.PossibleActions.setNumber;
        this.number = number;
    }
    
    private void removeNumber(int number){
        action = Manager.PossibleActions.removePossibleNumber;
        this.number = number;
    }
    
    protected Manager.PossibleActions getAction(){
        return action;
    }
    
    protected int getPosx(){
        return cell[0];
    }
    
    protected int getPosy(){
        return cell[1];
    }
    
    protected int getNumber(){
        return number;
    }
    
    @Override
    public String toString(){
        return "Cell: [" + cell[0] + "][" + cell[1] + "], Action: " + String.valueOf(getAction()) + ", Number: " + number;
    }
}
