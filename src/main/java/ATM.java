import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//Pass the date in the constructor
//Check the isLost for confiscate
//Create the main loop
//Create checkers methods for Card checks
//Create a card instance
//Create a function that reads the csv, for each of the lines create a card and then store cards in an arraylist

public class ATM {
    private boolean running = true;
    private String currentDate;
    private String fileName;
    private List<Card> cards = new ArrayList<>();
    private Card currCard = null;

    public ATM(String currentDate, String fileName){
        this.currentDate = currentDate;
        this.fileName = fileName;
    }

    public void readCSV() throws ParseException {
        Scanner sc = null; // Set as null so it can be used outside the try catch
        try { // Calls an error if the csv file is not found
            sc = new Scanner(new File("src/main/java/cards.csv"));
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not load the database");
            System.exit(0);
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] details = line.split(",");
            // Create the cards
            Card card = createCard(details[0], details[1], details[2], details[3], details[4], details[5], details[6]);
            this.cards.add(card);
        }
    }


    public Card createCard(String cardID, String fullname, String pin, String currBalance, String issueDate, String expiryDate, String state) throws ParseException {
        //Converting the String to Date Format
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date iDate = sdf.parse(issueDate);
        Date eDate = sdf.parse(expiryDate);

        int cardIDInt = Integer.parseInt(cardID);
        int pinInt = Integer.parseInt(pin);
        int currBalanceInt = Integer.parseInt(currBalance);
        boolean currState = Boolean.parseBoolean(state);
        Card newCard = new Card(cardIDInt, fullname, pinInt, currBalanceInt, iDate, eDate, currState);

        return newCard;
    }

    public void confiscate(){
        //confiscate the card, make it invalid
    }

    public String enoughBalance(){
        return "Not enough money";
    }

    public int checkBalance(Card card){
        return card.getCurrBalance();
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

    public boolean checkValid(int cardID) throws ParseException {
        // Check if the card id has 5 digits. Since we take in an int, it doesn't keep leading zeros.
        if (Integer.toString(cardID).length() > 5) {
            System.out.println("Incorrect number of digits.");
            return false;
        }

        // Check through the system database if there is an existing card with the same id
        boolean validID = checkValidID(cardID);
        if (validID == false) {
            System.out.println("Not a valid card number.");
            return false;
        }

        // Check if the current date of the atm is after the issue date of the card
        // Check if the current date of the atm is before the expiry date of the card
        boolean validDate = checkDate();
        if (validDate == false) {
            System.out.println("Card date is invalid.");
            return false;
        }

        // Check if the card has been stolen
        boolean stolen = this.checkStolen();
        if (stolen == true) {
            System.out.println("This card is lost or has been stolen.");
            return false;
        }

        boolean blocked = this.checkBlockState();
        if (blocked == true) {
            System.out.println("This card is blocked.");
            return false;
        }

        return true;
    }

    public boolean checkValidID(int cardID) {
        for (Card c : this.cards) {
            if (c.getAccNo() == cardID) {
                this.currCard = c;
                return true;
            }
        }
        return false;
    }

    public boolean checkStolen() {
        return this.currCard.getIsLost();
    }
    
    public boolean checkDate() throws ParseException {
        boolean result = true;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date currDate = sdf.parse(this.currentDate);

        long iDiff = Math.abs(currDate.getTime() - this.currCard.getIssueDate().getTime());
        if(iDiff < 0){
            result = false;
        }
        long eDiff = Math.abs(this.currCard.getExpiryDate().getTime() - currDate.getTime());
        if(eDiff < 0){
            result = false;
        }

        return result;

    }

    public boolean checkBlockState(){
        return this.currCard.getBlockState();
    }

    public boolean checkPIN(int inputPin) {
        return this.currCard.getPIN() == inputPin;
    }

//While true loop, ends when we need it to
//Entry message before the loop
//Scanner for Card number, if number is valid
//Scan for pin, check if pins counter that has a max of 3
//Present options
//Scan for input

    public static void main(String[] args) throws ParseException {
        // Create the atm and start it.
        ATM atm = new ATM("01/01/2021", "cards.csv");
        atm.readCSV();
        Scanner cardInput = new Scanner(System.in);
        while (atm.running) {
            // Ask for card number - ensure that an int is given, not a different data type
            
            System.out.print("Enter your card number: ");
            int cardID = 0;
            // Check if the input given is an integer
            if (cardInput.hasNextInt()) {
                cardID = cardInput.nextInt();
//                input.close(); // stop the scanner
//                System.out.println("Closed");
            }
            else {
                cardInput.nextLine(); // clear the input reader
                System.out.println("Integers only!");
                System.out.println();
                continue; // this will send it back to the start of the loop, skipping the code after this
            }

            // Check if the card is valid
            boolean valid = atm.checkValid(cardID);
            if (valid == false) {
                continue;
            }
            int counter = 1;
            while (counter < 4){
                System.out.print("Enter your card pin: ");
                if (cardInput.hasNextInt()) {
                    int cardP = cardInput.nextInt();
                    boolean result = atm.checkPIN(cardP);
                    if(result){
                        System.out.println("Success");
                        break;
                    }
                    else{
                        System.out.println("Invalid Pin " + (3-counter) + " attempts remaining before the card is blocked");
                        counter++;
                    }



                }

                else {
                    cardInput.nextLine(); // clear the input reader
                    System.out.println("Integers only!");
                    System.out.println();
                    counter++;
                }
            }
            if(counter == 4){
                atm.currCard.setBlockState(true);
                System.out.println("This card has been blocked");
            }

            // If card exist, ask for PIN
                // If PIN is correct
                    // Check if the card is valid to use
                        // If valid, provide the user with the 4 options (Withdraw, deposit, check balance, exit account)
                            // if exit account is selected, return the user back to the home screen (where the user enters a card number)
                // If incorrect, (After 3 failed attempts - lock the card, print an apology, reset the loop to ask for another card)
                    // Ask for another attempt

            // If card doesn't exist, continue the loop to ask for another card

        }


    }


}
