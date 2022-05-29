package oh_heaven.game;

import oh_heaven.game.Oh_Heaven.Suit;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.Card;


public class RandomStrategy implements IPlayerStrategy{
	public void leadingTurn(Player player, GameManager gm) {
		gm.selectCard(randomCard(player.getHand()));
	}
	@Override
	public void turn(Player player, Hand trick, Suit trump, Card winningCard, GameManager gm) {
		leadingTurn(player, gm);
	}
	
    // return random Card from Hand
    public static Card randomCard(Hand hand){
        int x = GameManager.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
