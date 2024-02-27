import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private ArrayList<Card> deck;

    public Deck() {
        this.deck = new ArrayList<>();

        for(int suit = 0; suit <= 3; suit++) {
            for(int value = 2; value <= 14; value++) {
                deck.add(new Card(suit, value));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card getCard() {
        if(!deck.isEmpty()) {
            return deck.remove(deck.size() - 1);
        }
        return null;
    }

}
