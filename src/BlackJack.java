import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class representing the Blackjack game.
 */
public class BlackJack {

    private Deck deck;  // The deck of cards used in the game
    private ArrayList<Card> player;  // The player's hand
    private ArrayList<Card> dealer;  // The dealer's hand

    private boolean gameOver;  // Flag to indicate if the game is over

    private int playerBankroll;  // The player's bankroll
    private int betAmount;  // The amount of bet placed by the player

    Scanner kb;  // Scanner object for user input

    /**
     * Constructor for the Blackjack game.
     * Initializes the player's bankroll with a random value between 1000 and 5000.
     */
    public BlackJack() {
        kb = new Scanner(System.in);
        playerBankroll = (int)(Math.random() * 4000) + 1000;
    }

    /**
     * Main method to start the Blackjack game.
     */
    public static void main(String[] args) {
        BlackJack game = new BlackJack();
        game.run();
    }

    /**
     * Method to start and run the game.
     */
    private void run() {
        // Main game loop
        while (playerBankroll > 0) {
            // Initialize a new round of the game
            initializeRound();

            // Player's turn
            playerTurn();

            // If game is not over, proceed with dealer's turn and determine winner
            if (!gameOver) {
                dealerTurn();
                determineWinner();
            }

            System.out.println();
        }
        System.out.println("Game over! Your bankroll is empty.");
    }

    /**
     * Method to initialize a new round of the game.
     * Deals initial cards and prompts the player to place a bet.
     */
    private void initializeRound() {
        deck = new Deck();  // Create a new deck of cards
        player = new ArrayList<>();  // Initialize player's hand
        dealer = new ArrayList<>();  // Initialize dealer's hand
        gameOver = false;  // Reset game over flag

        // Prompt player to place a bet
        while (true) {
            System.out.println("Current bankroll: $" + playerBankroll);
            System.out.print("Enter bet amount: $");
            betAmount = kb.nextInt();
            if (betAmount > playerBankroll) {
                System.out.println("You cannot bet more than your bankroll.\n");
                continue;
            } else if (betAmount <= 0) {
                System.out.println("You cannot bet less or equal to zero.\n");
                continue;
            }
            break;
        }

        kb.nextLine();  // Consume newline character
        System.out.println();

        deck.shuffle();  // Shuffle the deck
        dealInitialCards();  // Deal initial cards to player and dealer
    }

    /**
     * Method to deal initial cards to the player and dealer.
     */
    private void dealInitialCards() {
        // Deal two cards each to player and dealer
        player.add(deck.getCard());
        dealer.add(deck.getCard());
        player.add(deck.getCard());
        dealer.add(deck.getCard());

        // Display initial hands and their values
        System.out.println("Dealer's hand:\t" + dealer.get(0) + " [?]");
        System.out.println("Player's hand:\t" + player.get(0) + " " + player.get(1));
        System.out.println("Player's hand value:\t" + calculateHandValue(player));
    }

    /**
     * Method to handle player's hit action.
     */
    private void handleHit() {
        player.add(deck.getCard());  // Deal a card to the player
        System.out.println("\nPlayer's hand:\t" + player);
        System.out.println("Player's hand value:\t" + calculateHandValue(player));
        if (calculateHandValue(player) > 21) {
            gameOver = true;
            System.out.println("Player busts! Dealer wins.");
            playerBankroll -= betAmount;
        }
        else if (calculateHandValue(player) == 21) {
            gameOver = true;
            System.out.println("Player wins! Blackjack!");
            playerBankroll += betAmount;
        }
    }

    /**
     * Method to handle the player's turn.
     */
    private void playerTurn() {
        boolean firstMove = false;  // Flag to track if it's the player's first move
        while (true) {
            // Prompt player for action (hit, stay, or double down)
            if (betAmount * 2 <= playerBankroll && !firstMove) System.out.println("Hit, stay or double down?");
            else System.out.println("Hit or stay?");
            String response = kb.nextLine().toLowerCase();
            switch (response) {
                case "hit", "h" -> {
                    handleHit();  // Handle hit action
                    if (gameOver) return;  // If game over, return from method
                }
                case "double down", "dd", "d d" -> {
                    if (firstMove) {
                        System.out.println("Invalid input.\n");
                        continue;
                    }
                    if (betAmount * 2 > playerBankroll) {
                        System.out.println("You cannot double down. Insufficient funds.\n");
                        continue;
                    }
                    betAmount *= 2;  // Double the bet amount
                    handleHit();  // Handle hit action
                    return;  // Return from method
                }
                case "stay", "s" -> {
                    return;  // Return from method
                }
                default -> System.out.println("Invalid input.\n");
            }
            firstMove = true;  // Set first move flag to true after the first move
        }
    }

    /**
     * Method to handle the dealer's turn.
     */
    private void dealerTurn() {
        System.out.println("\nDealer's turn:");
        System.out.println("Dealer's hand:\t" + dealer);

        // Dealer hits until hand value reaches 17 or more
        while (calculateHandValue(dealer) < 17) {
            dealer.add(deck.getCard());  // Deal a card to the dealer
            System.out.println("Dealer hits.");
            System.out.println();
            System.out.println("Dealer's hand:\t" + dealer);
        }

        // Display result of dealer's turn
        if (calculateHandValue(dealer) > 21) {
            System.out.println("Dealer busts! Player wins.");
        } else {
            System.out.println("Dealer stands.");
        }
        System.out.println();
    }

    /**
     * Method to determine the winner of the game.
     */
    private void determineWinner() {
        int playerScore = calculateHandValue(player);  // Get player's hand value
        int dealerScore = calculateHandValue(dealer);  // Get dealer's hand value

        // Display player's and dealer's hand values
        System.out.println("\nPlayer's hand value: " + playerScore);
        System.out.println("Dealer's hand value: " + dealerScore);

        // Determine winner based on hand values
        if (playerScore > 21 || (dealerScore <= 21 && dealerScore > playerScore)) {
            System.out.println("Dealer wins!");
            playerBankroll -= betAmount;
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            System.out.println("Player wins!");
            playerBankroll += betAmount;
        } else {
            System.out.println("It's a tie! Push!");
        }
    }

    /**
     * Method to calculate the value of a hand.
     * @param hand The hand for which to calculate the value
     * @return The value of the hand
     */
    private int calculateHandValue(@NotNull ArrayList<Card> hand) {
        int value = 0;  // Initialize hand value to 0
        int numAces = 0;  // Initialize number of aces to 0

        // Iterate through cards in the hand to calculate the value
        for (Card card : hand) {
            int cardValue = card.getValue();
            if (cardValue == 14) { // Ace
                numAces++;
                value += 11;
            } else if (cardValue >= 11 && cardValue <= 13) { // Face cards
                value += 10;
            } else {
                value += cardValue;  // Add card value to total hand value
            }
        }

        // Adjust value for aces if necessary to avoid bust
        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;  // Return the calculated hand value
    }
}
