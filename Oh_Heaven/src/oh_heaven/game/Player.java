package oh_heaven.game;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.Card;
import oh_heaven.game.Oh_Heaven.Suit;

public abstract class Player {
	
	public Hand hand = new Hand(GameManager.getInstance().getDeck());
	public int bid = 0;
	public int score = 0;
	
	public Player() {
	}
	
	public void addToHand(Card card) {
		hand.insert(card, false);
	}
	
	public void sortHand(Hand.SortType sortType) {
		hand.sort(sortType, true);
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public void turn(Suit suit) {
		
	}
	
	public void setBid(int bid) {
		this.bid = bid;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public void resetScore() {
		this.score = 0;
	}

	public abstract void setListener();
}
