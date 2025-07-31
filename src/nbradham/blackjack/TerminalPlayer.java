package nbradham.blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

import nbradham.blackjack.Game.Action;

final class TerminalPlayer extends Player {

	private static final Scanner IN = new Scanner(System.in);

	public TerminalPlayer(final int startCoin) {
		super(startCoin);
	}

	@Override
	final short getBet() {
		short bet = 0;
		do {
			System.out.printf("Place bet (Balance: %d): ", coin);
			try {
				bet = Short.parseShort(IN.nextLine());
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse bet.");
			}
			if (bet > coin || bet < 1) {
				System.out.println("Invalid bet.");
				bet = 0;
			}
		} while (bet == 0);
		return bet;
	}

	@Override
	final Action getAction(final ArrayList<TableCard> hand, final HashSet<Action> actions) {
		System.out.printf("Current hand: %s%nDealer's hand: %s%nAvailable actions: %s%n", hand,
				Arrays.toString(game.getDealersHand()), actions.parallelStream().map(a -> {
					switch (a) {
					case Action.DOUBLE:
						return "(D)ouble Down";
					case Action.HIT:
						return "(H)it";
					case Action.SPLIT:
						return "(S)plit";
					case Action.STAND:
						return "S(t)and";
					case Action.SURRENDER:
						return "S(u)rrender";
					}
					return null;
				}).collect(Collectors.joining(", ")));
		Action act = null;
		do {
			System.out.print("Choose action: ");
			switch (Character.toLowerCase(IN.nextLine().charAt(0))) {
			case 'd':
				act = Action.DOUBLE;
				break;
			case 'h':
				act = Action.HIT;
				break;
			case 's':
				act = Action.SPLIT;
				break;
			case 't':
				act = Action.STAND;
				break;
			case 'u':
				act = Action.SURRENDER;
				break;
			}
			if (!actions.contains(act)) {
				System.out.println("Invalid action.");
				act = null;
			}
		} while (act == null);
		return act;
	}
}