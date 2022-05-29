package oh_heaven.game;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class Utility {

	// return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = GameManager.random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return random Card from Hand
    public static Card randomCard(Hand hand){
        int x = GameManager.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
 
    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list){
        int x = GameManager.random.nextInt(list.size());
        return list.get(x);
    }
  
  
  
    public static boolean rankGreater(Card card1, Card card2) {
	    return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }
}
