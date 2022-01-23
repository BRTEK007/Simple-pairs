package com.mygdx.game;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

class MyPair {
    public int x;
    public int y;

    public MyPair(int _x, int _y){
        x = _x;
        y = _y;
    }

    public void set(int _x, int _y){
        x = _x;
        y = _y;
    }

}

public class Bot {

    private Array<Integer> unknownIds;//ids of not known cards
    private Array<MyPair> knownCards;//known cards, [id, key]
    private MyPair knownPair;//matched pair, [id1, id2] -> [-1, id2] -> [-1, -1]

    private int memorySize;//how many cards can be held in known

    public Bot(int _cardCount, int _memorySize){
        unknownIds = new Array<>();

        for(int i = 0; i < _cardCount; i++){
            unknownIds.add(i);
        }

        knownCards = new Array<>();

        knownPair = new MyPair(-1,-1);

        memorySize = _memorySize;
    }

    public void registerCard(int _id, int _key){
        if(!unknownIds.contains(_id, true)) return;

        unknownIds.removeValue(_id, true);

        knownCards.add(new MyPair(_id, _key));

        if(knownCards.size > memorySize){//time to forget...
            MyPair r = knownCards.random();
            unknownIds.add(r.x);//push its id back to unknown
            knownCards.removeValue(r, true);
        }
    }

    public int getMove(){//return id of the cards to pick up
        if(knownPair.x == -1 && knownPair.y == -1)//search for pair if not have any
            searchForPair();

        int move;

        if(knownPair.x != -1){//give known pair priority, if not try random unknown
            move = knownPair.x;
            knownPair.x = -1;
        }else if(knownPair.y != -1){
            move = knownPair.y;
            knownPair.y = -1;
        }else{
            move = unknownIds.random();
        }

        return move;
    }

    public void forgetCard(int _id){//card was picked by Bot or by Player
        unknownIds.removeValue(_id, true);

        if(knownPair.x == _id || knownPair.y == _id) {
            knownPair.x = -1;
            knownPair.y = -1;
        }

        for (Iterator<MyPair> iter = knownCards.iterator(); iter.hasNext();) {
            MyPair p = iter.next();
            if(p.x == _id){
                iter.remove();
                break;
            }
        }

    }

    private void searchForPair(){//look for matching cards in known cards

        for(int i = 0; i < knownCards.size; i++){
            MyPair card1 = knownCards.get(i);

            for(int j = i+1; j < knownCards.size; j++){
                MyPair card2 = knownCards.get(j);

                if(card1.y == card2.y){//cards have same key
                    knownPair.set(card1.x, card2.x);//set known pair to their indexes
                    knownCards.removeValue(card1, true);//remove these 2 cards from known
                    knownCards.removeValue(card2, true);
                    return;
                }
            }

        }

    }



}
