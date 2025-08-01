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

	private final ArrayList<TableCard> dealHand = new ArrayList<>();
	private final Stack<Card> shoe = new Stack<>();
	private final Player player;
	private short bet;

	Game(final Player setPlayer) {
		(player = setPlayer).game = this;
	}

	final void start() {
		System.out.printf("Seed: %d%n", SEED);
		bet = player.getBet();
		player.coin -= bet;
		shoe.addAll(DECK);
		Collections.shuffle(shoe, RAND);
		final ArrayList<TableCard> firstHand = new ArrayList<>();
		final Rank a = dealCard(firstHand), b = dealCard(firstHand);
		dealHand.add(new TableCard(shoe.pop(), false));
		dealCard(dealHand);
		if (a.value + b.value != 21) {
			final HashSet<Action> actions = new HashSet<>();
			actions.add(Action.HIT);
			actions.add(Action.STAND);
			actions.add(Action.SURRENDER);
			if (canDouble()) {
				actions.add(Action.DOUBLE);
				if (a == b)
					actions.add(Action.SPLIT);
			}
			Action act = player.getAction(firstHand, actions);
			final ArrayList<Hand> hands = new ArrayList<>();
			hands.add(new Hand(bet, firstHand));
			if (act == Action.SPLIT) {
				player.coin -= bet;
				final ArrayList<TableCard> second = new ArrayList<>();
				second.add(firstHand.removeLast());
				hands.add(new Hand(bet, second));
				actions.remove(Action.SPLIT);
			} else
				actions.remove(Action.DOUBLE);
			for (Hand hand : hands) {
				if (hand.cards.size() != 2) {
					dealCard(hand.cards);
					if (handSum(hand) == 21)
						continue;
					actions.add(Action.SURRENDER);
					if (canDouble())
						actions.add(Action.DOUBLE);
					else
						actions.remove(Action.DOUBLE);
				}
				loop: do {
					switch (act) {
					case Action.DOUBLE:
						player.coin -= bet;
						hand.bet += bet;
						dealCard(hand.cards);
						break loop;
					case Action.HIT:
						actions.remove(Action.DOUBLE);
						actions.remove(Action.SURRENDER);
						dealCard(hand.cards);
						if (handSum(hand) > 20)
							break loop;
						break;
					case Action.STAND:
						break loop;
					case Action.SURRENDER:
						player.end(bet / 2);
					}
					act = player.getAction(hand.cards, actions);
				} while (true);
				//TODO Dealer and win.
			}
		} else
			// TODO BlackJack
			;
	}

	private final Rank dealCard(final ArrayList<TableCard> hand) {
		final TableCard card = new TableCard(shoe.pop());
		hand.add(card);
		return card.card.rank();
	}

	private final boolean canDouble() {
		return player.coin >= bet;
	}

	final TableCard[] getDealersHand() {
		return dealHand.parallelStream().map(tc -> tc.revealed ? tc : new TableCard(null, false))
				.toArray(i -> new TableCard[i]);
	}

	private static final byte handSum(final Hand hand) {
		byte sum = 0, aces = 0;
		for (final TableCard tc : hand.cards) {
			final Rank rank = tc.card.rank();
			if (rank == Rank.ACE)
				++aces;
			if ((sum += rank.value) > 21 && --aces > -1)
				sum -= 10;
		}
		return sum;
	}
}