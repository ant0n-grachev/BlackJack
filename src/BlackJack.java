import java.util.ArrayList;
import java.util.Scanner;

public class BlackJack {

    private Deck deck;
    private ArrayList<Card> player;
    private ArrayList<Card> dealer;

    Scanner kb;

    public BlackJack() {
        deck = new Deck();
        player = new ArrayList<>();
        dealer = new ArrayList<>();
        kb = new Scanner(System.in);
    }

    public static void main(String[] args) {
        BlackJack game = new BlackJack();
        game.run();
    }

    private void run() {
        // dealCards()
        deck.shuffle();

        player.add(deck.getCard());
        dealer.add(deck.getCard());
        player.add(deck.getCard());
        dealer.add(deck.getCard());

        System.out.println("dealer hand:\t"+dealer.get(0)+" [?]");
        System.out.println("player hand:\t"+player.get(0)+" "+player.get(1));

        System.out.println();
        System.out.println("hit or stay?");
        String response = kb.nextLine();
        if(response.equals("hit")) {
            player.add(deck.getCard());
        }
        System.out.println("player hand:\t"+player.get(0)+" "+player.get(1)+" "+player.get(2));

        //while(response.equals("hit")) {...}
        //do{...}while(logic);

        /*
        for(int i = 0; i < player.size(); i++) {\
            //not a complete statement below
            //print what is below
            player.get(i)?
        }

         */




        // playerTurn()
        // dealerTurn()
        // calcHandValue()
    }


}
