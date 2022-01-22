package com.mygdx.game;

public class GameObserver {

   private float time;
   private int mistakes;

   public GameObserver(){
        time = 0;
        mistakes = 0;
   }

   public void update(float _delta){
        time += _delta;
   }

   public int getTime(){
        return Math.round(time);
   }

   public int getMistakes(){
        return mistakes;
   }

}
