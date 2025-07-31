package nbradham.blackjack;

final class TableCard {
	final Card card;
	boolean revealed;

	TableCard(final Card setCard) {
		this(setCard, true);
	}

	TableCard(final Card setCard, final boolean setRevealed) {
		card = setCard;
		revealed = setRevealed;
	}

	@Override
	public final String toString() {
		return revealed ? card.toString() : "##";
	}
}