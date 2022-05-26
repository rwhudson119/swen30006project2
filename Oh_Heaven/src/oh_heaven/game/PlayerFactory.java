package oh_heaven.game;

public class PlayerFactory {
	private static PlayerFactory instance = null;
	

	
	private PlayerFactory() {
	}
	
	public static PlayerFactory getInstance() {
		if(instance == null) {
			instance = new PlayerFactory();
		}
		return instance;
	}
	
	public Player createPlayer(PlayerType type) {
		if(type == PlayerType.human) {
			return new HumanPlayer();
		} else {
			return  new NPC(type);
		}
	}
}
