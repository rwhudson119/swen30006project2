package oh_heaven.game;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public class NPC extends Player{
	
	private IPlayerStrategy strategy;
	
	public NPC(PlayerType type) {
		this.strategy = PlayerStrategyFactory.getInstance().createStrategy(type);
	}
	
	public void turn(Hand trick, Suit trump, Card winningCard, GameManager gm) {
		if(trump == null) {
			strategy.leadingTurn(this, gm);
		} else {
			strategy.turn(this, trick, trump, winningCard, gm);
		}
	}

}
