import java.util.ArrayList;
import java.util.Scanner;

public class BlackJack {

    private Deck deck;
    private ArrayList<Card> player;
    private ArrayList<Card> dealer;

    private boolean gameOver;

    private int playerBankroll;
    private int betAmount;

    Scanner kb;

    public BlackJack() {
        kb = new Scanner(System.in);
        playerBankroll = 1000;
    }

    public static void main(String[] args) {
        BlackJack game = new BlackJack();
        game.run();
    }

    private void run() {
        while (playerBankroll > 0) {
            deck = new Deck();
            player = new ArrayList<>();
            dealer = new ArrayList<>();
            gameOver = false;
            System.out.println("Current bankroll: $" + playerBankroll);
            System.out.print("Enter bet amount: $");
            betAmount = kb.nextInt();
            if (betAmount > playerBankroll) {
                System.out.println("You cannot bet more than your bankroll.\n");
                continue;
            }
            kb.nextLine();
            System.out.println();

            deck.shuffle();
            dealInitialCards();

            playerTurn();

            if (!gameOver) {
                dealerTurn();
                determineWinner();
            }

            System.out.println();
        }
        System.out.println("Game over! Your bankroll is empty.");
    }

    private void dealInitialCards() {
        player.add(deck.getCard());
        dealer.add(deck.getCard());
        player.add(deck.getCard());
        dealer.add(deck.getCard());

        System.out.println("Dealer's hand:\t" + dealer.get(0) + " [?]");
        System.out.println("Player's hand:\t" + player.get(0) + " " + player.get(1));
        System.out.println("Player's hand value:\t" + calculateHandValue(player));
    }

    private void playerTurn() {
        while (true) {
            System.out.println("Hit or stay?");
            String response = kb.nextLine().toLowerCase();
            if (response.equals("hit")) {
                System.out.println();
                player.add(deck.getCard());
                System.out.println("Player's hand:\t" + player);
                System.out.println("Player's hand value:\t" + calculateHandValue(player));
                if (calculateHandValue(player) > 21) {
                    gameOver = true;
                    System.out.println("Player busts! Dealer wins.");
                    playerBankroll -= betAmount;
                    return;
                }
                if (calculateHandValue(player) == 21) {
                    gameOver = true;
                    System.out.println("Player wins! Blackjack!");
                    playerBankroll += betAmount;
                    return;
                }
            } else if (response.equals("stay")) {
                return;
            } else {
                System.out.println("Invalid input. Please enter 'hit' or 'stay'.");
            }
        }
    }

    private void dealerTurn() {
        System.out.println("\nDealer's turn:");
        System.out.println("Dealer's hand:\t" + dealer);

        while (calculateHandValue(dealer) < 17) {
            dealer.add(deck.getCard());
            System.out.println("Dealer hits.");
            System.out.println();
            System.out.println("Dealer's hand:\t" + dealer);
        }

        if (calculateHandValue(dealer) > 21) {
            System.out.println("Dealer busts! Player wins.");
        } else {
            System.out.println("Dealer stands.");
        }
        System.out.println();
    }

    private void determineWinner() {
        int playerScore = calculateHandValue(player);
        int dealerScore = calculateHandValue(dealer);

        System.out.println("\nPlayer's hand value: " + playerScore);
        System.out.println("Dealer's hand value: " + dealerScore);

        if (playerScore > 21 || (dealerScore <= 21 && dealerScore > playerScore)) {
            System.out.println("Dealer wins!");
            playerBankroll -= betAmount;
        } else if (dealerScore > 21 || (playerScore <= 21 && playerScore > dealerScore)) {
            System.out.println("Player wins!");
            playerBankroll += betAmount;
        } else {
            System.out.println("It's a tie! Push!");
        }
    }

    private int calculateHandValue(ArrayList<Card> hand) {
        int value = 0;
        int numAces = 0;

        for (Card card : hand) {
            int cardValue = card.getValue();
            if (cardValue == 14) { // Ace
                numAces++;
                value += 11; // Assume Ace as 11 first
            } else if (cardValue >= 11 && cardValue <= 13) { // Face cards
                value += 10;
            } else {
                value += cardValue;
            }
        }

        // Adjust for Aces
        while (value > 21 && numAces > 0) {
            value -= 10; // Change Ace from 11 to 1
            numAces--;
        }

        return value;
    }
}
