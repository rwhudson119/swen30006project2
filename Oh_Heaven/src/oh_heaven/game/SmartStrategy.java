package oh_heaven.game;

import java.util.ArrayList;
import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.Oh_Heaven.Suit;

public class SmartStrategy implements IPlayerStrategy{

	private final int DECK_SIZE = 52;
	private ArrayList<Card> playedCards = new ArrayList<>();
	
	@Override
	public void leadingTurn(Player player, GameManager gm) {
		// leads with random card.
		int x = GameManager.random.nextInt(player.getHand().getNumberOfCards());
		gm.selectCard(player.getHand().get(x));
	}

	@Override
	public void turn(Player player, Hand trick, Suit trump, Card winningCard, GameManager gm) {

		updatePlayedCards(trick);
		
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
	
		Card playCard;
		if(winningCards.isEmpty()) { // can not win - play lowest card
			playCard = lowestCard(playable, trump);
		} else { // place least valuable winning card
			playCard = lowestCard(winningCards, trump);
		}
		gm.selectCard(playCard);
		playedCards.add(playCard);
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
	private Card lowestCard(ArrayList<Card> possibleCards, Suit trump) {
		Card lowest = null, lowestNonTrump = null;
		for(Card card : possibleCards) {
			// RankId is reversed.
			if(lowest == null || card.getRankId() > lowest.getRankId()) {
				// Avoids playing trump suit if possible
				if(card.getSuit() != trump) {
					lowestNonTrump = card;
				}
				lowest = card;
			}
		}
		if(lowestNonTrump != null) {
			return lowestNonTrump;
		}
		return lowest;
	}
	// Stores played cards for future smarter AI decisions.
	private void updatePlayedCards(Hand trick) {
		for(Card card : trick.getCardList()) {
			if(playedCards.size() == DECK_SIZE) { // new deck started.
				playedCards.clear();
			}
			playedCards.add(card);
		}
		return;
	}
}
