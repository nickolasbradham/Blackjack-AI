package nbradham.blackjack;

import java.util.ArrayList;
import java.util.HashSet;

import nbradham.blackjack.Game.Action;

sealed abstract class Player permits TerminalPlayer {

	Game game;
	int coin;

	Player(final int startCoin) {
		coin = startCoin;
	}

	abstract short getBet();

	abstract Action getAction(final ArrayList<TableCard> firstHand, final HashSet<Action> actions);
}