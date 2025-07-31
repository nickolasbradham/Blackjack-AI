package nbradham.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import nbradham.blackjack.Card.Rank;

final class Game {

	static enum Action {
		DOUBLE, HIT, SPLIT, STAND, SURRENDER
	}

	private static final long SEED = System.currentTimeMillis();
	private static final Random RAND = new Random(SEED);
	private static final ArrayList<Card> DECK = new ArrayList<>();
	static {
		final Card.Rank[] ranks = Card.Rank.values();
		for (Card.Suit s : Card.Suit.values())
			for (Card.Rank r : ranks)
				DECK.add(new Card(s, r));
	}

	private final ArrayList<TableCard> firstHand = new ArrayList<>(), dealHand = new ArrayList<>();
	private final Stack<Card> shoe = new Stack<>();
	private final Player player;

	Game(final Player setPlayer) {
		(player = setPlayer).game = this;
	}

	final void start() {
		System.out.printf("Seed: %d%n", SEED);
		final short bet = player.getBet();
		player.coin -= bet;
		shoe.addAll(DECK);
		Collections.shuffle(shoe, RAND);
		final Rank a = dealCard(firstHand), b = dealCard(firstHand);
		dealHand.add(new TableCard(shoe.pop(), false));
		dealCard(dealHand);
		if (a.value + b.value != 21) {
			final HashSet<Action> actions = new HashSet<>();
			actions.add(Action.HIT);
			actions.add(Action.STAND);
			actions.add(Action.SURRENDER);
			if (player.coin > bet) {
				actions.add(Action.DOUBLE);
				if (a == b)
					actions.add(Action.SPLIT);
			}
			// TODO Continue.
			System.out.println(player.getAction(firstHand, actions));
		}
	}

	private final Rank dealCard(final ArrayList<TableCard> hand) {
		final TableCard card = new TableCard(shoe.pop());
		hand.add(card);
		return card.card.rank();
	}

	final TableCard[] getDealersHand() {
		return dealHand.parallelStream().map(tc -> tc.revealed ? tc : new TableCard(null, false))
				.toArray(i -> new TableCard[i]);
	}
}