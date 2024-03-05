import org.jetbrains.annotations.NotNull;

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
        playerBankroll = (int)(Math.random() * 4000) + 1000;
    }

    public static void main(String[] args) {
        BlackJack game = new BlackJack();
        game.run();
    }

    private void run() {
        while (playerBankroll > 0) {
            initializeRound();

            playerTurn();

            if (!gameOver) {
                dealerTurn();
                determineWinner();
            }

            System.out.println();
        }
        System.out.println("Game over! Your bankroll is empty.");
    }

    private void initializeRound() {
        deck = new Deck();
        player = new ArrayList<>();
        dealer = new ArrayList<>();
        gameOver = false;

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

        kb.nextLine();
        System.out.println();

        deck.shuffle();
        dealInitialCards();
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

    private void handleHit() {
        player.add(deck.getCard());
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

    private void playerTurn() {
        boolean firstMove = false;
        while (true) {
            if (betAmount * 2 <= playerBankroll && !firstMove) System.out.println("Hit, stay or double down?");
            else System.out.println("Hit or stay?");
            String response = kb.nextLine().toLowerCase();
            switch (response) {
                case "hit", "h" -> {
                    handleHit();
                    if (gameOver) return;
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
                    betAmount *= 2;
                    handleHit();
                    return;
                }
                case "stay", "s" -> {
                    return;
                }
                default -> System.out.println("Invalid input.\n");
            }
            firstMove = true;
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
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            System.out.println("Player wins!");
            playerBankroll += betAmount;
        } else {
            System.out.println("It's a tie! Push!");
        }
    }

    private int calculateHandValue(@NotNull ArrayList<Card> hand) {
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

        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }
}
