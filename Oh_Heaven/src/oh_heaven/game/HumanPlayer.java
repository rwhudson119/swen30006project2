package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public class HumanPlayer extends Player{
	
	public void turn(Hand trick, Suit trump, Card winningCard, GameManager gm) {
		this.hand.setTouchEnabled(true);
	};
	
	public void setListener(GameManager gm) {
		CardListener cardListener = new CardAdapter()  // Human Player plays card
			    {
			      public void leftDoubleClicked(Card card) { 
			    	  	gm.selectCard(card);
			    	  	
			    	  	hand.setTouchEnabled(false); 
			      	}
			    };
		hand.addCardListener(cardListener);
	}

}
