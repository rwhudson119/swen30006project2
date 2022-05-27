package oh_heaven.game;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
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
	  private final int handWidth = 400;
	  private final int trickWidth = 40;
	  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	  private final Location[] handLocations = {
				  new Location(350, 625),
				  new Location(75, 350),
				  new Location(350, 75),
				  new Location(625, 350)
		  };
	  private final Location[] scoreLocations = {
				  new Location(575, 675),
				  new Location(25, 575),
				  new Location(575, 25),
				  // new Location(650, 575)
				  new Location(575, 575)
		  };
	  private Actor[] scoreActors = {null, null, null, null };
	  private final Location trickLocation = new Location(350, 350);
	  private final Location textLocation = new Location(350, 450);
	  private final int thinkingTime = 2000;
	  private Location hideLocation = new Location(-500, - 500);
	  private Location trumpsActorLocation = new Location(50, 50);
	  private boolean enforceRules=false;

	  public void setStatus(String string) { setStatusText(string); }
	  
	private int[] tricks = new int[nbPlayers];
	private PlayerType[] playerType = new PlayerType[nbPlayers];
	private Player[] players = new Player[nbPlayers];
	
	private PlayerFactory playerFactory = PlayerFactory.getInstance();

	Font bigFont = new Font("Serif", Font.BOLD, 36);
	
	private static GameManager instance = null;
	
	public static GameManager getInstance() {
		if(instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	private Card selected;
	
	private GameManager() {
		super(700, 700, 30);
	}

	public void start(Properties properties) {
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
	  	initPlayers();
		initScores();
	    initScore();
	    for (int i=0; i <nbRounds; i++) {
	      initTricks();
	      initRound();
	      playRound();
	      updateScores();
	    };
	    for (int i=0; i <nbPlayers; i++) updateScore(i);
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

	// return random Enum value
	  public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
	      int x = random.nextInt(clazz.getEnumConstants().length);
	      return clazz.getEnumConstants()[x];
	  }

	  // return random Card from Hand
	  public static Card randomCard(Hand hand){
	      int x = random.nextInt(hand.getNumberOfCards());
	      return hand.get(x);
	  }
	 
	  // return random Card from ArrayList
	  public static Card randomCard(ArrayList<Card> list){
	      int x = random.nextInt(list.size());
	      return list.get(x);
	  }
	  
	  
	  
	  public boolean rankGreater(Card card1, Card card2) {
		  return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
	  }

	private void initScores() {
		for (int i = 0; i < nbPlayers; i++) {
			players[i].resetScore();
	 	}
	}
	
	private void updateScores() {
		 for (int i = 0; i < nbPlayers; i++) {
			 players[i].addScore(tricks[i]);
			 if (tricks[i] == players[i].bid) players[i].addScore(madeBidBonus);
		 }
	}
	
	private void initTricks() {
		 for (int i = 0; i < nbPlayers; i++) {
			 tricks[i] = 0;
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
				players[i].setListener();
			}
		}
		// Set up human player for interaction
		/*CardListener cardListener = new CardAdapter()  // Human Player plays card
			    {
			      public void leftDoubleClicked(Card card) { selected = card; hands[0].setTouchEnabled(false); }
			    };
		hands[0].addCardListener(cardListener);*/
		 // graphics
	    RowLayout[] layouts = new RowLayout[nbPlayers];
	    for (int i = 0; i < nbPlayers; i++) {
	      layouts[i] = new RowLayout(handLocations[i], handWidth);
	      layouts[i].setRotationAngle(90 * i);
	      // layouts[i].setStepDelay(10);
	      players[i].getHand().setView(this, layouts[i]);
	      players[i].getHand().setTargetArea(new TargetArea(trickLocation));
	      players[i].getHand().draw();
	    }
	//    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
	//      hands[i].setVerso(true);			// You do not need to use or change this code.
	    // End graphics
	}				

	private void playRound() {
	// Select and display trump suit
		final Suit trumps = randomEnum(Suit.class);
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
	for (int i = 0; i < nbPlayers; i++) updateScore(i);
	for (int i = 0; i < nbStartCards; i++) {
		nextPlayer = players[nextPlayerindex];
		trick = new Hand(deck);
		selected = null;
		// if (false) {
	    if (playerType[nextPlayerindex] == PlayerType.human) {  // Select lead depending on player type
			nextPlayer.getHand().setTouchEnabled(true);
			setStatus("Player " + nextPlayerindex + " double-click on card to lead.");
			while (null == selected) delay(100);
	    } else {
			setStatusText("Player " + nextPlayerindex + " thinking...");
	        delay(thinkingTime);
	        nextPlayer.turn(null, null, null);
	        while (null == selected) delay(100);
	    }
	    // Lead with selected card
	        trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
			trick.draw();
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
	        if (playerType[nextPlayerindex] == PlayerType.human) {
	        	nextPlayer.getHand().setTouchEnabled(true);
	    		setStatus("Player 0 double-click on card to follow.");
	    		while (null == selected) delay(100);
	        } else {
		        setStatusText("Player " + nextPlayerindex + " thinking...");
		        delay(thinkingTime);
		        nextPlayer.turn(trick, trumps, winningCard);
		        while (null == selected) delay(100);
	        }
	        // Follow with selected card
		        trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
				trick.draw();
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
					 (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
					  // trumped when non-trump was winning
					 (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
					 System.out.println("NEW WINNER");
					 winner = nextPlayerindex;
					 winningCard = selected;
				 }
			// End Follow
		}
		delay(600);
		trick.setView(this, new RowLayout(hideLocation, 0));
		trick.draw();		
		nextPlayerindex = winner;
		setStatusText("Player " + nextPlayerindex + " wins trick.");
		tricks[nextPlayerindex]++;
		updateScore(nextPlayerindex);
	}
	removeActor(trumpsActor);
	}
	
	private void initScore() {
		 for (int i = 0; i < nbPlayers; i++) {
			 // scores[i] = 0;
			 String text = "[" + String.valueOf(players[i].score) + "]" + String.valueOf(tricks[i]) + "/" + String.valueOf(players[i].bid);
			 scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
			 addActor(scoreActors[i], scoreLocations[i]);
		 }
	  }

	private void updateScore(int player) {
		removeActor(scoreActors[player]);
		String text = "[" + String.valueOf(players[player].score) + "]" + String.valueOf(tricks[player]) + "/" + String.valueOf(players[player].bid);
		scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[player], scoreLocations[player]);
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
			players[i].resethand();
		}  
		Hand pack = deck.toHand(false);
		  // pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
		  for (int i = 0; i < nbCardsPerPlayer; i++) {
			  for (int j=0; j < nbPlayers; j++) {
				  if (pack.isEmpty()) return;
				  Card dealt = randomCard(pack);
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
