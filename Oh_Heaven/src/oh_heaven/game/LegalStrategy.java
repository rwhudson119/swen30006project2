package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public class LegalStrategy implements IPlayerStrategy{

	public LegalStrategy() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void leadingTurn(Player player, GameManager gm) {
		// TODO Auto-generated method stub
		gm.selectCard(Utility.randomCard(player.getHand()));
	}

	@Override
	public void turn(Player player, Hand trick, Suit trump, Card winningCard, GameManager gm) {
		Suit lead = (Suit) trick.get(0).getSuit();
		gm.selectCard(suitedCard(player.getHand(), lead, player));
	}
	
	public Card suitedCard(Hand hand, Suit suit, Player player){
		if(hand.getNumberOfCardsWithSuit(suit) > 0) {
			return hand.getCardsWithSuit(suit).get(0);
		} else {
	        return Utility.randomCard(hand);
		}
    }
}
