package oh_heaven.game;

public class PlayerStrategyFactory {
	
	private static PlayerStrategyFactory instance = null;

	private PlayerStrategyFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static PlayerStrategyFactory getInstance() {
		if(instance == null) {
			instance = new PlayerStrategyFactory();
		}
		return instance;
	}
	
	public IPlayerStrategy createStrategy(PlayerType type) {
		if(type == PlayerType.random){
			return new RandomStrategy();
		} else if(type == PlayerType.legal){
			return new LegalStrategy();
		} else if(type == PlayerType.smart) {
			return new SmartStrategy();
		} else {
			return null;
		}
		
	}

}
