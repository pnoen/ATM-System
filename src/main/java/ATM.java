import java.util.Scanner;

//Pass the date in the constructor
//Check the isLost for confiscate
//Create the main loop
//Create checkers methods for Card checks
//Create a card instance
//Create a function that reads the csv, for each of the lines create a card and then store cards in an arraylist
public class ATM {
    int PINattempts = 0; //To count how many times pin has been entered
    int funds = 0; //amount of money in ATM
    private boolean running = true;

    public void confiscate(){
        //confiscate the card, make it invalid
    }

    public String enoughBalance(){
        return "Not enough money";
    }

    public int checkBalance(){
        return 0;
    }

    public void cancel(){
        //cancels any transaction happening.

    }

    public void maintenance(){
        //if user is ADMIN, allow adding cash into system.

    }

    public String noFunds(){
        cancel();
        return "Not enough money inside ATM";
    }



//While true loop, ends when we need it to
//Entry message before the loop
//Scanner for Card number, if number is valid
//Scan for pin, check if pins counter that has a max of 3
//Present options
//Scan for input

    public static void main(String[] args){
        // Create the atm and start it.
        ATM atm = new ATM();
        while (atm.running) {
            // Ask for card number
            Scanner input = new Scanner(System.in);
            System.out.print("Enter your card number: ");
            int cardNum = input.nextInt();
            // Check if the card number is valid
            // If card exist, ask for PIN
                // If PIN is correct
                    // Check if the card is valid to use
                        // If valid, provide the user with the 4 options (Withdraw, deposit, check balance, exit account)
                // If incorrect, (After 3 failed attempts - lock the card, print an apology, reset the loop to ask for another card)
                    // Ask for another attempt

            // If card doesn't exist, continue the loop to ask for another card

        }

    }


}
