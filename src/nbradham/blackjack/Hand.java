package nbradham.blackjack;

import java.util.ArrayList;

final class Hand {

	final ArrayList<TableCard> cards;
	short bet;

	Hand(final short setBet, final ArrayList<TableCard> setHand) {
		bet = setBet;
		cards = setHand;
	}
}