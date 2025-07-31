package nbradham.blackjack;

final record Card(Suit suit, Rank rank) {
	static enum Suit {
		CLUBS('C'), DIAMONDS('D'), HEARTS('H'), SPADES('S');

		private final char character;

		private Suit(final char setChar) {
			character = setChar;
		}
	};

	static enum Rank {
		ACE(11, "A"), N2(2, "2"), N3(3, "3"), N4(4, "4"), N5(5, "5"), N6(6, "6"), N7(7, "7"), N8(8, "8"), N9(9, "9"),
		N10(10, "10"), JACK(10, "J"), QUEEN(10, "Q"), KING(10, "K");

		final int value;
		private final String string;

		private Rank(final int setVal, final String setString) {
			value = setVal;
			string = setString;
		}
	};

	@Override
	public final String toString() {
		return rank.string + suit.character;
	}
}