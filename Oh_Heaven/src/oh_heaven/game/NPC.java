package oh_heaven.game;
import oh_heaven.game.Oh_Heaven.Suit;

public class NPC extends Player{
	
	private PlayerType type;
	private IPlayerStrategy strategy;
	
	public NPC(PlayerType type) {
		this.type = type;
		this.strategy = PlayerStrategyFactory.getInstance().createStrategy(type);
		
	}
	
	public void turn(Suit suit) {
		if(suit == null) {
			strategy.leadingTurn(this);
		} else {
			strategy.turn(this, suit);
		}
	}

	@Override
	public void setListener() {
	}

}
