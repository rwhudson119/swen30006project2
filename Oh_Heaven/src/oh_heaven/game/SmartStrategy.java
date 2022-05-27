package oh_heaven.game;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public class SmartStrategy implements IPlayerStrategy{

	@Override
	public void leadingTurn(Player player) {
		// leads with random card.
		int x = GameManager.random.nextInt(player.getHand().getNumberOfCards());
		GameManager.getInstance().selectCard(player.getHand().get(x));
	}

	@Override
	public void turn(Player player, Hand trick, Suit trump, Card winningCard) {

		ArrayList<Card> playable = new ArrayList<>();
		Suit lead = (Suit) trick.get(0).getSuit();
		
		// finds playable cards by checking for any cards of lead suit
		if(player.hand.getNumberOfCardsWithSuit(lead) == 0) {
			playable = player.getHand().getCardList();
		} else {
			playable = player.getHand().getCardsWithSuit(lead);
		}

		// looks for any cards that would beat the current winning card
		ArrayList<Card> winningCards = new ArrayList<>();
		for(Card card : playable) {
			if(isWinning(card, winningCard, trump)) {
				winningCards.add(card);
			}
		}
	
		if(winningCards.isEmpty()) { // can not win - play lowest card
			GameManager.getInstance().selectCard( lowestCard(playable) );
		} else { // place least valuable winning card
			GameManager.getInstance().selectCard( lowestCard(winningCards) );
		}
	}
	// Uses logic from GameManager to check if trial card beats winning card.
	private boolean isWinning(Card trial, Card winning, Suit trump) {
		
		boolean trumped = (winning.getSuit() == trump);
		boolean higherRank = (trial.getRankId() < winning.getRankId()); // reversed order
		boolean sameSuit = (trial.getSuit() == winning.getSuit());
		
		if( (sameSuit && higherRank) || (trial.getSuit() == trump && !trumped) ) {
			return true;
		}
		return false;
	}
	// Finds lowest ranked card in list of cards.
	private Card lowestCard(ArrayList<Card> possibleCards) {
		Card lowest = null;
		for(Card card : possibleCards) {
			// RankId is reversed.
			if(lowest == null || card.getRankId() > lowest.getRankId()) {
				lowest = card;
			}
		}
		return lowest;
	}
}
