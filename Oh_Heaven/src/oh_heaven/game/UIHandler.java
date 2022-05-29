package oh_heaven.game;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.Color;
import java.awt.Font;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jcardgame.TargetArea;

public class UIHandler {
	
	private final int handWidth = 400;
	private final int trickWidth = 40;
	private Actor[] scoreActors = {null, null, null, null };
	private final Location trickLocation = new Location(350, 350);
	private Location hideLocation = new Location(-500, - 500);
	Font bigFont = new Font("Serif", Font.BOLD, 36);
	
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

	public UIHandler() {
		// TODO Auto-generated constructor stub
	}
	
	public void updateTrickUI(Hand trick, GameManager gm) {
		trick.setView(gm, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
		trick.draw();
	}
	
	public void hideTrickUI(Hand trick, GameManager gm) {
		trick.setView(gm, new RowLayout(hideLocation, 0));
		trick.draw();
	}
	
	public void initCardUI(Player[] players, int nbPlayers, GameManager gm) {
		RowLayout[] layouts = new RowLayout[nbPlayers];
	    for (int i = 0; i < nbPlayers; i++) {
	      layouts[i] = new RowLayout(handLocations[i], handWidth);
	      layouts[i].setRotationAngle(90 * i);
	      // layouts[i].setStepDelay(10);
	      players[i].getHand().setView(gm, layouts[i]);
	      players[i].getHand().setTargetArea(new TargetArea(trickLocation));
	      players[i].getHand().draw();
	    }
	}
	
	public void initScoreUI(Player[] players, int nbPlayers, GameManager gm, Color bgColor) {
		for (int i = 0; i < nbPlayers; i++) {
			 // scores[i] = 0;
			 String text = "[" + String.valueOf(players[i].score) + "]" + String.valueOf(players[i].getTrick()) + "/" + String.valueOf(players[i].bid);
			 scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
			 gm.addActor(scoreActors[i], scoreLocations[i]);
		 }
	}
	
	public void updateScoreUI(Player[] players, int player, GameManager gm, Color bgColor) {
		gm.removeActor(scoreActors[player]);
		String text = "[" + String.valueOf(players[player].score) + "]" + String.valueOf(players[player].getTrick()) + "/" + String.valueOf(players[player].bid);
		scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		gm.addActor(scoreActors[player], scoreLocations[player]);
	}

}
