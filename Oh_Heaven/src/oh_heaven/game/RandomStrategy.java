package oh_heaven.game;

import oh_heaven.game.Oh_Heaven.Suit;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.Card;


public class RandomStrategy implements IPlayerStrategy{
	public void leadingTurn(Player player) {
		GameManager.getInstance().selectCard(randomCard(player.getHand()));
	}
	public void turn(Player player, Suit suit) {
		leadingTurn(player);
	}
	
    // return random Card from Hand
    public static Card randomCard(Hand hand){
        int x = GameManager.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
