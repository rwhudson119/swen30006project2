package oh_heaven.game;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.Card;
import oh_heaven.game.Oh_Heaven.Suit;

public abstract class Player {
	
	public Hand hand;
	public int bid = 0;
	public int score = 0;
	
	public void setListener(GameManager gm) {};
	
	public void turn(Hand trick, Suit trump, Card winningCard, GameManager gm) {};
	
	public void addToHand(Card card) {
		hand.insert(card, false);
	}
	
	public void sortHand(Hand.SortType sortType) {
		hand.sort(sortType, true);
	}
	
	public Hand getHand() {
		return hand;
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
	
	public void resethand(GameManager gm) {
		hand = new Hand(gm.getDeck());
	}
}
