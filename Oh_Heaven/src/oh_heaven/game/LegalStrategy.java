package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public class LegalStrategy implements IPlayerStrategy{

	public LegalStrategy() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void leadingTurn(Player player) {
		// TODO Auto-generated method stub
		GameManager.getInstance().selectCard(randomCard(player.getHand()));
	}

	@Override
	public void turn(Player player, Hand trick, Suit trump, Card winningCard) {
		Suit lead = (Suit) trick.get(0).getSuit();
		GameManager.getInstance().selectCard(suitedCard(player.getHand(), lead, player));
	}
	
	public static Card randomCard(Hand hand){
        int x = GameManager.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
	
	public static Card suitedCard(Hand hand, Suit suit, Player player){
		if(hand.getNumberOfCardsWithSuit(suit) > 0) {
			return hand.getCardsWithSuit(suit).get(0);
		} else {
	        int x = GameManager.random.nextInt(hand.getNumberOfCards());
	        return hand.get(x);
		}
    }
}
