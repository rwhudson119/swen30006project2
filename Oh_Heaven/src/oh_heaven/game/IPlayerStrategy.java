package oh_heaven.game;
import oh_heaven.game.Oh_Heaven.Suit;

public interface IPlayerStrategy {
	public void leadingTurn(Player player);
	public void turn(Player player, Suit suit);
}
