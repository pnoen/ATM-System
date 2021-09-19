import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Pass the date in the constructor
 * Check the isLost for confiscate
 * Create the main loop
 * Create checkers methods for Card checks
 * Create a card instance
 * Create a function that reads the csv, for each of the lines create a card and then store cards in an arraylist
 */

public class ATM {
    private boolean running = true; // If the machine is running or not
    private String currentDate; // Setting the date of the ATM
    private List<Card> cards = new ArrayList<>(); // List of cards from .csv file
    private Card currCard = null; // Current card selected
    private int transactionCount = 0; // Receipt number, or transaction selection
    private int balanceATM; // ATM balance
    private String csvFile;

    // Setting the variables for the ATM
    public ATM(String currentDate, String csvFile, int balanceATM) {
        this.currentDate = currentDate;
        this.balanceATM = balanceATM;
        this.csvFile = "src/main/java/" + csvFile;
    }

    public void readCSV() throws ParseException {
        Scanner sc = null; // Set as null for use outside the try-catch block
        try {
            sc = new Scanner(new File(this.csvFile));
        }
        catch (FileNotFoundException e) { // Raise error if the .csv file is not found
            System.out.println("Error: Could not load the database.");
            System.exit(0);
        }

        // This loop converts the .csv file to Card objects to be used
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] details = line.split(",");
            // Create the cards
            Card card = createCard(details[0], details[1], details[2], details[3], details[4], details[5], details[6]);
            this.cards.add(card);
        }
    }

    // The function that creates a Card object through the method parameters
    public Card createCard(String cardID, String fullname, String pin, String currBalance, String issueDate, String expiryDate, String state) throws ParseException {
        //Converting the String to Date Format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date iDate = sdf.parse(issueDate);
        Date eDate = sdf.parse(expiryDate);

        int cardIDInt = Integer.parseInt(cardID);
        int pinInt = Integer.parseInt(pin);
        int currBalanceInt = Integer.parseInt(currBalance);
        boolean currState = Boolean.parseBoolean(state);
        Card newCard = new Card(cardIDInt, fullname, pinInt, currBalanceInt, iDate, eDate, currState);

        return newCard;
    }

    // Checks if the card is valid
    public boolean checkValid(int cardID) throws ParseException {
        // Check if the card ID has 5 digits, taken as int to remove leading zeros
        if (Integer.toString(cardID).length() > 5) {
            System.out.println("Incorrect number of digits.");
            return false;
        }

        // Check through the system database if there is an existing card with the same ID
        boolean validID = checkValidID(cardID);
        if (validID == false) {
            System.out.println("Not a valid card number.");
            return false;
        }

        // Check if the current date of the ATM is after the card's issue date and before its expiry date
        boolean validDate = checkDate();
        if (validDate == false) {
            System.out.println("Card date is invalid.");
            return false;
        }

        // Check if the card has been stolen
        boolean stolen = this.checkStolen();
        if (stolen == true) {
            System.out.println("Sorry, this card is lost or has been stolen.");
            return false;
        }

        // Check if the card has been blocked from incorrect PIN attempts
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
    
    public boolean checkDate() throws ParseException { // Checks to see if the date on the card is legitimate
        boolean result = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH); // Setting the format of the dates we are using
        Date currDate = sdf.parse(this.currentDate);

        long iDiff = Math.abs(currDate.getTime() - this.currCard.getIssueDate().getTime()); // Finding difference with the issue date
        if (iDiff < 0) {
            result = false;
        }
        long eDiff = Math.abs(this.currCard.getExpiryDate().getTime() - currDate.getTime()); // Finding difference with expiry date
        if (eDiff < 0) {
            result = false;
        }

        return result;
    }
    
    public boolean checkStolen() {
        return this.currCard.getIsLost();
    }

    public boolean checkBlockState() {
        return this.currCard.getBlockState();
    }

    public boolean checkPIN(int inputPin) {
        return this.currCard.getPIN() == inputPin;
    }

    public int checkBalance() {
        return this.currCard.getCurrBalance();
    }

    public boolean pinController(Scanner cardInput) { // Responsible for PIN input and logic
        int counter = 1;
        
        while (counter < 4) {
            System.out.println();
            System.out.print("Enter your card pin: ");
            if (cardInput.hasNextInt()) {
                int cardP = cardInput.nextInt();
                boolean result = this.checkPIN(cardP);

                if (result) { // If PIN is correct
                    System.out.println("Success!");
                    return true;
                }
                else { // If PIN is incorrect
                    System.out.println("Invalid PIN, " + (3-counter) + " attempts remaining before the card is blocked.");
                    counter++;
                }
            }

            else {
                cardInput.nextLine(); // Clear the input reader
                System.out.println("Integers only!");
                counter++;
            }
        }
        
        if (counter == 4) { // After 3 failed attempts - lock the card, print an apology, reset the loop and ask for another card
            this.currCard.setBlockState(true);
            System.out.println("Sorry, this card has been blocked.");
            return false;
        }
        return true;
    }
    public boolean adminPinController(Scanner cardInput, Admin admin) { // Checks if the admin PIN is correct, as the admin PIN differs to customer PINs
        System.out.println();
        System.out.print("Enter your card pin: ");
        if (cardInput.hasNextInt()) {
            int cardP = cardInput.nextInt();
            return cardP == admin.getPin();
        }

        return false;
    }

    public boolean withdrawController(Scanner cardInput) { // Controls the logic if the withdraw functionality
        System.out.print("\nWould you like to\n" +
                "1. Withdraw funds\n" +
                "2. Cancel\n" +
                "Please enter the number corresponding to the action: ");

        int option = 0;
        if (cardInput.hasNextInt()) {
            option = cardInput.nextInt();
        }

        if (option == 1) {
            System.out.print("\nHow much would you like to withdraw: ");
            if (cardInput.hasNextInt()) {
                int amount = cardInput.nextInt();
                if (amount <= 0) { // Checks if the withdrawal amount is illegal
                    System.out.println("Invalid withdraw amount.");
                    return false;
                }
                if (amount > this.currCard.getCurrBalance()) { // Checks if the customer has enough funds to withdraw the selected amount
                    System.out.println("Sorry, you dont have enough funds to withdrawn the amount selected.");
                    System.out.println("Your current account balance is: " + atm.checkBalance());
                    return false;
                }
                if(amount > this.balanceATM) { // Checks if there is enough cash in the ATM to withdraw the selected amount
                    System.out.println("There isn't enough cash in the ATM to withdraw.");
                    return false;
                }
                this.withdraw(amount);
                System.out.println("Card is now being ejected.");
                return true;
            }
        }
        
        else if (option == 2) {
            return false;
        }
        
        System.out.println("\nCorrect input only!");
        cardInput.nextLine(); // Clear the input reader
        return false;
    }

    public void withdraw(int amount) {
        this.transactionCount++; // Increase the number of overall transactions
        int finalAmount = this.currCard.getCurrBalance() - amount; // New balance on the card
        this.balanceATM = this.balanceATM - amount; // Decrease the amount in the ATM
        this.currCard.setCurrBalance(finalAmount); // Update the balance of the card
        System.out.println(" --------------- ");
        System.out.println("Receipt No: " +  this.transactionCount + "\n" + "Transaction type : Withdraw\n" +
                "Amount withdrawn: " + amount + "\n" + "Current Balance: " + this.currCard.getCurrBalance());
        System.out.println(" --------------- ");
    }

    public boolean depositController(Scanner cardInput) { // Controls the loop and logic of the deposit functionality
        System.out.print("\nWould you like to\n" +
                "1. Deposit funds\n" +
                "2. Cancel\n" +
                "Please enter the number corresponding to the action: ");

        int option = 0;
        if (cardInput.hasNextInt()) {
            option = cardInput.nextInt();
        }

        if (option == 1){
            System.out.print("\nHow much would you like to deposit: ");
            if (cardInput.hasNextInt()) {
                int amount = cardInput.nextInt();
                if (amount <= 0) { // Checks if the deposit amount is illegal
                    System.out.println("Invalid deposit amount.");
                    return false;
                }
                if (amount % 5 != 0) { // Checks if the deposit amount is not using Australian notes
                    System.out.println("Can only deposit Australian notes.");
                    return false;
                }

                this.deposit(amount);
                System.out.println("Card is now being ejected.");
                return true;
            }
        }
        
        else if (option == 2) {
            return false;
        }
        
        System.out.println("\nCorrect input only!");
        cardInput.nextLine(); // Clear the input reader
        return false;
    }

    public void deposit(int amount) {
        this.transactionCount++; // Increase the number of overall transactions
        int finalAmount = this.currCard.getCurrBalance() + amount; // New balance of the card
        this.balanceATM = this.balanceATM + amount; // Increase the amount of cash in the ATM
        this.currCard.setCurrBalance(finalAmount); // Increase the balance of the card
        System.out.println(" --------------- ");
        System.out.println("Receipt No: " +  this.transactionCount + "\n" + "Transaction type : Deposit\n" + 
                "Amount deposited: " + amount + "\n" + "Current Balance: " + this.currCard.getCurrBalance());
        System.out.println(" --------------- ");
    }

    // This function controls the addition of cash into the ATM via maintenance
    // This is the interface that asks the admin how much they would like to add to the current ATM balance
    public void adminAdd(Scanner cardInput, ATM atm, Admin admin){
        System.out.println(" --------------- ");
        System.out.println("Current ATM balance is: " + atm.getBalanceATM());
        System.out.println(" --------------- ");

        boolean isDone = false; // Checks if the loop is complete
        while(!(isDone)) {
            System.out.print("Welcome to the maintenance section! How much would you like to add to the ATM balance? ");
            int amount = 0;
            
            if (cardInput.hasNextInt()) { // Checks the user input
                amount = cardInput.nextInt();
                if (amount < 0){ // Checks if the amount is illegal
                    System.out.println("Error amount is invalid.");
                }
                else {
                    admin.addFunds(amount); // Adds the amount to the ATM balance
                    isDone = true; // Breaks the loop
                }
            }
        }
    }

    public int getBalanceATM(){
        return this.balanceATM;
    }
    
    // Adds the 'amount' parameter to the ATM balance
    public void addBalanceATM(int amount) {
        this.balanceATM = this.balanceATM + amount;
    }

    public void updateCardsCSV() {
        // Set up a 2D array which holds all details of the Cards stored in the ATM
        List<List<String>> cardsDetails = new ArrayList<List<String>>();

        // Create the format for the Card issue date and expiry date to convert to a string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        // Traverse through the cards stored in the atm and store the fields for the .csv in a list
        for (Card c : this.cards) {
            // Create a list with a capacity of 7
            List<String> card = new ArrayList<String>(7);
            for (int i = 0; i < 7; i++) {  // Initialise the elements in the list so values can be set by index
                card.add("");
            }

            String cardID = String.format("%05d", c.getAccNo()); // Convert int to a string and add the leading zeros if necessary
            String name = c.getFullname();
            String pin = String.format("%04d", c.getPIN()); // Convert int to a string and add the leading zeros if necessary
            String bal = String.valueOf(c.getCurrBalance());

            String iDate = dateFormat.format(c.getIssueDate()); // Convert the Date to a string using the SimpleDateFormat
            String eDate = dateFormat.format(c.getExpiryDate()); // Convert the Date to a string using the SimpleDateFormat

            String lost = String.valueOf(c.getIsLost());  // Convert int to string

            card.set(0, cardID);
            card.set(1, name);
            card.set(2, pin);
            card.set(3, bal);
            card.set(4, iDate);
            card.set(5, eDate);
            card.set(6, lost);
            cardsDetails.add(card);
        }

        // Create the .csv writer
        try {
            FileWriter csvWriter = new FileWriter(this.csvFile);
            // Write all the details of the cards into the .csv file, having each card as a new entry on a new line
            for (List<String> c : cardsDetails) {
                csvWriter.append(String.join(",", c));  // Combine all elements in the list and separate by a comma
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error: Couldn't update the database");
        }
    }

    public Card getCurrCard() {
        return this.currCard;
    }


    
    public static void main(String[] args) throws ParseException {
        // Create the ATM and start it up
        ATM atm = new ATM("01/01/2021", "cards.csv", 199934690);
        atm.readCSV();
        Admin admin = new Admin(atm, 99999, 9999);
        Scanner cardInput = new Scanner(System.in); // Allows for the user to interact with the atm
        while (atm.running) {
            System.out.println();

            // Ask for card number - ensure that only an int is given
            System.out.print("Enter your card number: ");
            int cardID = 0;
            // Check if the input given is an integer
            if (cardInput.hasNextInt()) {
                cardID = cardInput.nextInt();
            }
            else {
                System.out.println("Integers only!");
                cardInput.nextLine(); // Clear the input reader
                continue; // Continue the loop from the beginning
            }

            if (cardID == admin.getID()) {
                boolean passCheck = atm.adminPinController(cardInput, admin);
                if (!passCheck) {
                    continue;
                }
                boolean isFinished = false;
                while (!(isFinished)) {
                    System.out.print("\nWelcome" + "to the XYZ ADMINISTRATOR Page\n" +
                            "Would you like to: \n" +
                            "1. ADD to ATM balance\n" +
                            "2. Check ATM balance\n" +
                            "3. Cancel\n" +
                            "Please enter the number corresponding to the action: ");

                    int selection = 0;
                    if (cardInput.hasNextInt()) {
                        selection = cardInput.nextInt();
                    }

                    if (selection == 1) {
                        atm.adminAdd(cardInput, atm, admin);
                        isFinished = true;

                    }
                    else if (selection == 2) {
                        System.out.println(" --------------- ");
                        System.out.println("The current ATM balance is " + atm.getBalanceATM());
                        System.out.println(" --------------- ");
                    }
                    else if (selection == 3) {
                        isFinished = true;
                    }
                    else {
                        System.out.println("Error: Invalid input please select from the given options.");
                        cardInput.nextLine(); // Clear the input reader
                    }
                }
                continue;
            }

            // Check if the card is valid
            boolean valid = atm.checkValid(cardID);
            if (valid == false) { // If card doesn't exist, continue the loop to ask for another card
                continue;
            }

            // If card exist and is valid, ask for PIN
            boolean pinCheck = atm.pinController(cardInput);
            if (pinCheck == false) {
                continue;
            }

            // If card is valid and PIN is correct, provide the user with the 3 options withdraw, deposit, and check balance
            boolean isComplete = false;
            while (!isComplete) {  // Set up a loop to allow the system to repeatedly ask for a input if an incorrect input is provided
                System.out.print("\nWelcome to XYZ ATM "  + atm.currCard.getFullname() +  ", what would you like to do: \n" +
                        "1. Withdrawal of funds\n" +
                        "2. Deposit of Funds\n" +
                        "3. Balance Check\n" +
                        "Please enter the number corresponding to the action: ");

                // Get the input of the user
                int selection = 0;
                if (cardInput.hasNextInt()) {
                    selection = cardInput.nextInt();
                }

                if (selection == 1) { // If withdraw is selected
                    isComplete = atm.withdrawController(cardInput);
                }
                else if (selection == 2) { // If deposit is selected
                    isComplete = atm.depositController(cardInput);
                }
                else if (selection == 3) { // If check balance is selected
                    System.out.println("Your current account balance is: " + atm.checkBalance());
                    isComplete = true;
                }
                else {  // Incorrect input
                    System.out.println("\nCorrect input only!");
                    cardInput.nextLine(); // Clear the input reader
                    continue;
                }
                atm.updateCardsCSV(); // Update the balance of the .csv file
            }
        }
    }
}
