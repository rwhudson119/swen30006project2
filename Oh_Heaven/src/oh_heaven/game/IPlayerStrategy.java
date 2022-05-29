package oh_heaven.game;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public interface IPlayerStrategy {
	public void leadingTurn(Player player, GameManager gm);
	public void turn(Player player, Hand trick, Suit trump, Card winningCard, GameManager gm);
}