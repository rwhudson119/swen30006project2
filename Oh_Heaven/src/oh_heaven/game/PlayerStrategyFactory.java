package oh_heaven.game;

public class PlayerStrategyFactory {
	
	private static PlayerStrategyFactory instance = null;

	private PlayerStrategyFactory() {
	}
	
	public static PlayerStrategyFactory getInstance() {
		if(instance == null) {
			instance = new PlayerStrategyFactory();
		}
		return instance;
	}
	
	public IPlayerStrategy createStrategy(PlayerType type) {
		switch(type) {
			case random:
				return new RandomStrategy();
			case legal:
				return new LegalStrategy();
			case smart:
				return new SmartStrategy();
			default:
				return null;
		}
	}

}
