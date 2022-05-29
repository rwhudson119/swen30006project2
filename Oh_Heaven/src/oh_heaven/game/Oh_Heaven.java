package oh_heaven.game;

// Oh_Heaven.java
import java.util.Properties;


public class Oh_Heaven {
	
  public enum Suit
  {
    SPADES, HEARTS, DIAMONDS, CLUBS
  }

  public enum Rank
  {
    // Reverse order of rank importance (see rankGreater() below)
	// Order of cards is tied to card images
	ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
  }
  
  



  public Oh_Heaven(Properties properties)
  {
    new GameManager(properties);
  }

  public static void main(String[] args)
  {
	// System.out.println("Working Directory = " + System.getProperty("user.dir"));
	final Properties properties;
	if (args == null || args.length == 0) {
		properties = PropertiesLoader.loadPropertiesFile(null);
	} else {
		properties = PropertiesLoader.loadPropertiesFile(args[0]);
	}

    new Oh_Heaven(properties);
  }

}
