package oh_heaven.game;


import java.awt.Font;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import oh_heaven.game.Oh_Heaven.Rank;
import oh_heaven.game.Oh_Heaven.Suit;
import ch.aplu.jcardgame.*;

@SuppressWarnings("serial")
public class GameManager extends CardGame {
	
	
	final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

	static public int seed = 30006;
	static Random random = new Random(seed);
	private final String version = "1.0";
	  public final int nbPlayers = 4;
	  public int nbStartCards = 13;
	  public int nbRounds = 3;
	  public int madeBidBonus = 10;
	  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

	  private final Location textLocation = new Location(350, 450);
	  private Location trumpsActorLocation = new Location(50, 50);
	  private boolean enforceRules=false;

	  public void setStatus(String string) { setStatusText(string); }

	private PlayerType[] playerType = new PlayerType[nbPlayers];
	private Player[] players = new Player[nbPlayers];
	
	private PlayerFactory playerFactory = PlayerFactory.getInstance();

	Font bigFont = new Font("Serif", Font.BOLD, 36);
	
	private UIHandler ui;

	private Card selected;

	public GameManager(Properties properties) {
		super(700, 700, 30);
	    setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
	    setStatusText("Initializing...");
	    if(properties.getProperty("seed") != null) {
	    	seed = Integer.parseInt(properties.getProperty("seed"));
	    	random = new Random(seed);
	    }
	    if(properties.getProperty("madeBidBonus") != null) {
	    	madeBidBonus = Integer.parseInt(properties.getProperty("madeBidBonus"));
	    }
	    if(properties.getProperty("nbStartCards") != null) {
	    	nbStartCards = Integer.parseInt(properties.getProperty("nbStartCards"));
	    }
	    if(properties.getProperty("rounds") != null) {
	    	nbRounds = Integer.parseInt(properties.getProperty("rounds"));
	    }
	    if(properties.getProperty("enforceRules") != null) {
	    	enforceRules = Boolean.parseBoolean(properties.getProperty("enforceRules"));
	    }
	  	for(int i=0;i<nbPlayers;i++) {
	  		playerType[i] = PlayerType.valueOf(properties.getProperty("players." + i));
	  	}
	  	System.out.println(seed + " " + nbStartCards + " " + nbRounds + " " + enforceRules);
	  	this.ui = new UIHandler();
	  	initPlayers();
		initScores();
		ui.initScoreUI(players, nbPlayers, this, bgColor);
	    for (int i=0; i <nbRounds; i++) {
	      initTricks();
	      initRound();
	      playRound();
	      updateScores();
	    };
	    for (int i=0; i <nbPlayers; i++) ui.updateScoreUI(players, i, this, bgColor);
	    int maxScore = 0;
	    for (int i = 0; i <nbPlayers; i++) if (players[i].score > maxScore) maxScore = players[i].score;
	    Set <Integer> winners = new HashSet<Integer>();
	    for (int i = 0; i <nbPlayers; i++) if (players[i].score == maxScore) winners.add(i);
	    String winText;
	    if (winners.size() == 1) {
	    	winText = "Game over. Winner is player: " +
	    			winners.iterator().next();
	    }
	    else {
	    	winText = "Game Over. Drawn winners are players: " +
	    			String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
	    }
	    addActor(new Actor("sprites/gameover.gif"), textLocation);
	    setStatusText(winText);
	    refresh();
	}
	  
	 

	private void initScores() {
		for (int i = 0; i < nbPlayers; i++) {
			players[i].resetScore();
	 	}
	}
	
	private void updateScores() {
		 for (int i = 0; i < nbPlayers; i++) {
			 players[i].addScore(players[i].getTrick());
			 if (players[i].getTrick() == players[i].bid) players[i].addScore(madeBidBonus);
		 }
	}
	
	private void initTricks() {
		 for (int i = 0; i < nbPlayers; i++) {
			 players[i].setTrick(0);
		 }
	}
	
	private void initRound() {
		/*hands = new Hand[nbPlayers];
		for (int i = 0; i < nbPlayers; i++) {
			hands[i] = new Hand(deck);
		}*/
		dealingOut(nbPlayers, nbStartCards);
		for (int i = 0; i < nbPlayers; i++) {
			players[i].sortHand(Hand.SortType.SUITPRIORITY);
			if(playerType[i] == PlayerType.human) {
				players[i].setListener(this);
			}
		}
		// Set up human player for interaction
		/*CardListener cardListener = new CardAdapter()  // Human Player plays card
			    {
			      public void leftDoubleClicked(Card card) { selected = card; hands[0].setTouchEnabled(false); }
			    };
		hands[0].addCardListener(cardListener);*/
		 // graphics
		ui.initCardUI(players, nbPlayers, this);
	//    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
	//      hands[i].setVerso(true);			// You do not need to use or change this code.
	    // End graphics
	}				

	private void playRound() {
	// Select and display trump suit
		final Suit trumps = Utility.randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
	    addActor(trumpsActor, trumpsActorLocation);
	// End trump suit
	Hand trick;
	int winner;
	Card winningCard;
	Suit lead;
	int nextPlayerindex = random.nextInt(nbPlayers);
	Player nextPlayer; // randomly select player to lead for this round
	initBids(trumps, nextPlayerindex);
	// initScore();
	for (int i = 0; i < nbPlayers; i++) ui.updateScoreUI(players, i, this, bgColor);
	for (int i = 0; i < nbStartCards; i++) {
		nextPlayer = players[nextPlayerindex];
		trick = new Hand(deck);
		selected = null;
		// if (false) {
		nextPlayer.turn(null, null, null, this);
	    if (playerType[nextPlayerindex] == PlayerType.human) {  
			setStatus("Player " + nextPlayerindex + " double-click on card to lead.");
	    } else {
			setStatusText("Player " + nextPlayerindex + " thinking...");
	        //delay(thinkingTime);
	    }
	    while (null == selected) delay(100);
	    // Lead with selected card
	    	ui.updateTrickUI(trick, this);
			selected.setVerso(false);
			// No restrictions on the card being lead
			lead = (Suit) selected.getSuit();
			selected.transfer(trick, true); // transfer to trick (includes graphic effect)
			winner = nextPlayerindex;
			winningCard = selected;
		// End Lead
		for (int j = 1; j < nbPlayers; j++) {
			if (++nextPlayerindex >= nbPlayers) nextPlayerindex = 0;  // From last back to first
			selected = null;
			nextPlayer = players[nextPlayerindex];
			// if (false) {
			nextPlayer.turn(trick, trumps, winningCard, this);
	        if (playerType[nextPlayerindex] == PlayerType.human) {
	    		setStatus("Player " + nextPlayerindex + " double-click on card to follow.");
	        } else {
		        setStatusText("Player " + nextPlayerindex + " thinking...");
		        //delay(thinkingTime);
	        }
	        while (null == selected) delay(100);
	        // Follow with selected card
	        	ui.updateTrickUI(trick, this);
				selected.setVerso(false);  // In case it is upside down
				// Check: Following card must follow suit if possible
					if (selected.getSuit() != lead && nextPlayer.getHand().getNumberOfCardsWithSuit(lead) > 0) {
						 // Rule violation
						 String violation = "Follow rule broken by player " + nextPlayerindex + " attempting to play " + selected;
						 System.out.println(violation);
						 if (enforceRules) 
							 try {
								 throw(new BrokeRuleException(violation));
								} catch (BrokeRuleException e) {
									e.printStackTrace();
									System.out.println("A cheating player spoiled the game!");
									System.exit(0);
								}  
					 }
				 // End Check
				 selected.transfer(trick, true); // transfer to trick (includes graphic effect)
				 System.out.println("winning: " + winningCard);
				 System.out.println(" played: " + selected);
				 // System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
				 // System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " + (13 -    selected.getRankId()));
				 if ( // beat current winner with higher card
					 (selected.getSuit() == winningCard.getSuit() && Utility.rankGreater(selected, winningCard)) ||
					  // trumped when non-trump was winning
					 (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
					 System.out.println("NEW WINNER");
					 winner = nextPlayerindex;
					 winningCard = selected;
				 }
			// End Follow
		}
		delay(600);
		ui.hideTrickUI(trick, this);	
		nextPlayerindex = winner;
		setStatusText("Player " + nextPlayerindex + " wins trick.");
		players[nextPlayerindex].addTrick(1);
		ui.updateScoreUI(players, nextPlayerindex, this, bgColor);
	}
	removeActor(trumpsActor);
	}



	private void initBids(Suit trumps, int nextPlayer) {
		int[] bids = new int[nbPlayers];
		int total = 0;
		for (int i = nextPlayer; i < nextPlayer + nbPlayers; i++) {
			 int iP = i % nbPlayers;
			 bids[iP] = nbStartCards / 4 + random.nextInt(2);
			 total += bids[iP];
		 }
		 if (total == nbStartCards) {  // Force last bid so not every bid possible
			 int iP = (nextPlayer + nbPlayers) % nbPlayers;
			 if (bids[iP] == 0) {
				 bids[iP] = 1;
			 } else {
				 bids[iP] += random.nextBoolean() ? -1 : 1;
			 }
		 }
		 for (int i = 0; i<nbPlayers; i++) {
			 players[i].setBid(bids[i]);
		 }
		// for (int i = 0; i < nbPlayers; i++) {
		// 	 bids[i] = nbStartCards / 4 + 1;
		//  }
	 }
	
	private void initPlayers() {
		for (int i = 0;i<nbPlayers; i++) {
			players[i] = playerFactory.createPlayer(playerType[i]);
		}
	}
	
	private void dealingOut(int nbPlayers, int nbCardsPerPlayer) {
		for(int i = 0; i<nbPlayers; i++) {
			players[i].resethand(this);
		}  
		Hand pack = deck.toHand(false);
		  // pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
		  for (int i = 0; i < nbCardsPerPlayer; i++) {
			  for (int j=0; j < nbPlayers; j++) {
				  if (pack.isEmpty()) return;
				  Card dealt = Utility.randomCard(pack);
			      // System.out.println("Cards = " + dealt);
			      dealt.removeFromHand(false);
			      players[j].addToHand(dealt);
				  // dealt.transfer(hands[j], true);
			  }
		  }
	  }

	public void selectCard(Card card) {
		this.selected = card;
	}
	
	public Deck getDeck() {
		return this.deck;
	}
	
}
