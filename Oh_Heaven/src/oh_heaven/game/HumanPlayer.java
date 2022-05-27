package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public class HumanPlayer extends Player{
	
	public void setListener() {
		CardListener cardListener = new CardAdapter()  // Human Player plays card
			    {
			      public void leftDoubleClicked(Card card) { 
			    	  	GameManager.getInstance().selectCard(card);
			    	  	
			    	  	hand.setTouchEnabled(false); 
			      	}
			    };
		hand.addCardListener(cardListener);
	}

}
