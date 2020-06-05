/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniobaena.sudokugraphics;

import java.util.ArrayList;

/**
 *
 * @author antonio
 */
public class Manager {
    
    private ArrayList<ArrayList<Action>> steps;
    private ArrayList<Action> currentStep;
    
    public static enum PossibleActions {
        setSelected,
        setIterated,
        setNumber,
        removePossibleNumber,
        setHiddenPair
    }
    
    private boolean newStepStarted;
    
    public Manager(){
        steps = new ArrayList<>();
        currentStep = new ArrayList<>();
    }
    
    public void startStep(){
        if(newStepStarted) throw new IllegalStateException("The previous step wasn't closed");
        newStepStarted = true;
    }
    
    public void endStep(){
        if(!newStepStarted) throw new IllegalStateException("The new step wasn't started");
        steps.add((ArrayList<Action>)currentStep.clone());
        newStepStarted = false;
        currentStep.clear();
    }
    
    public void addAction(int posx, int posy, PossibleActions action, int number){
        if(!newStepStarted) throw new IllegalStateException("The new stap wans't started");
        currentStep.add(new Action(posx, posy, action, number));
    }
    
    protected ArrayList<Action> getNextStep(){
        if(steps.size() > 0){
            ArrayList<Action> step = steps.get(0);
            steps.remove(0);
            return step;
        }
        else throw new IllegalStateException("The steps size is 0. No actions to perform.");
    }
}
