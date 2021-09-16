import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    private boolean running = true; //if the machine is running or not
    private String currentDate; //setting the date of the ATM
    private List<Card> cards = new ArrayList<>(); //list of cards from csv file
    private Card currCard = null; //current card thats selected
    private int transactionCount = 0; //Receipt number/ what transaction it is for the ATm
    private int balanceATM; //ATM balance

    //Setting the variables for the atm
    public ATM(String currentDate, int balanceATM){
        this.currentDate = currentDate;
        this.balanceATM = balanceATM;
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

        //This loop converts the CSV file to Card objects that we can use
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] details = line.split(",");
            // Create the cards
            Card card = createCard(details[0], details[1], details[2], details[3], details[4], details[5], details[6]);
            this.cards.add(card);
        }
    }

    //The function that creates a card with the given variables
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

    //THis function checks if the card is valid
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
    
    public boolean checkDate() throws ParseException { //Checks to see if the date on the card is legitimate
        boolean result = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH); //Setting the format of the dates we are using
        Date currDate = sdf.parse(this.currentDate);

        long iDiff = Math.abs(currDate.getTime() - this.currCard.getIssueDate().getTime()); //Finding difference with the issue date
        if(iDiff < 0){
            result = false;
        }
        long eDiff = Math.abs(this.currCard.getExpiryDate().getTime() - currDate.getTime()); //Finding difference with expiry date
        if(eDiff < 0){
            result = false;
        }

        return result;

    }

    public boolean checkBlockState(){ //Checks to see if the card is blocked
        return this.currCard.getBlockState();
    }

    public boolean checkPIN(int inputPin) { //Checks to see if the pin is correct
        return this.currCard.getPIN() == inputPin;
    }

    public int checkBalance() { //checks balance of the card
        return this.currCard.getCurrBalance();
    }

    public boolean pinController(Scanner cardInput) { //Responsible for the pin input and the logic
        int counter = 1;
        while (counter < 4) {
            System.out.println();
            System.out.print("Enter your card pin: ");
            if (cardInput.hasNextInt()) {
                int cardP = cardInput.nextInt();
                boolean result = this.checkPIN(cardP);

                if(result){ // If PIN is correct
                    System.out.println("Success");
                    return true;
                }
                else{ // If incorrect, (After 3 failed attempts - lock the card, print an apology, reset the loop to ask for another card)
                    System.out.println("Invalid Pin " + (3-counter) + " attempts remaining before the card is blocked");
                    counter++;
                }
            }

            else {
                cardInput.nextLine(); // clear the input reader
                System.out.println("Integers only!");
                counter++;
            }
        }
        if(counter == 4){
            this.currCard.setBlockState(true);
            System.out.println("This card has been blocked");
            return false;
        }
        return true;
    }
    public boolean adminPinController(Scanner cardInput, Admin admin){ //Since the admin pin is different, this function checks if the admin pin is correct
        System.out.println();
        System.out.print("Enter your card pin: ");
        if (cardInput.hasNextInt()) {
            int cardP = cardInput.nextInt();
            return cardP == admin.getPin();
        }

        return false;
    }

    public boolean withdrawController(Scanner cardInput) { //Controls the logic if the withdraw functionality
        System.out.print("\nWould you like to\n" +
                "1. Withdraw funds\n" +
                "2. Cancel\n" +
                "Please enter the number corresponding to the action: ");

        int option = 0;
        if (cardInput.hasNextInt()) {
            option = cardInput.nextInt();
        }

        if (option == 1){
            System.out.print("\nHow much would you like to withdraw: ");
            if (cardInput.hasNextInt()) {
                int amount = cardInput.nextInt();
                if (amount <= 0) {
                    System.out.println("Invalid withdraw amount.");
                    return false;
                }
                if (amount > this.currCard.getCurrBalance()) {
                    System.out.println("Sorry, you dont have enough funds to withdrawn the amount selected.");
                    return false;
                }
                if(amount > this.balanceATM){
                    System.out.println("There isn't enough cash in the ATM to withdraw.");
                    return false;
                }
                this.withdraw(amount);
                System.out.println("Card is now being ejected.");
                return true;
            }
        }
        else if (option == 2){
            return false;
        }
        System.out.println("\nCorrect input only!");
        cardInput.nextLine(); // clear the input reader
        return false;
    }

    public void withdraw(int amount){
        this.transactionCount++; // Increase the number of overall transactions
        int finalAmount = this.currCard.getCurrBalance() - amount; // New balance on the card
        this.balanceATM = this.balanceATM - amount; // Decrease the amount in the atm
        this.currCard.setCurrBalance(finalAmount); // Update the balance of the card
        System.out.println(" --------------- ");
        System.out.println("Receipt No: " +  this.transactionCount + "\n" + "Transaction type : Withdraw\n" +
                "Amount withdrawn: " + amount + "\n" + "Current Balance: " + this.currCard.getCurrBalance());
        System.out.println(" --------------- ");
    }

    public boolean depositController(Scanner cardInput) { //Controls the loop and logic of the deposit functionality
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
                if (amount <= 0) {
                    System.out.println("Invalid deposit amount.");
                    return false;
                }
                if (amount % 5 != 0) {
                    System.out.println("Can only deposit Australian notes.");
                    return false;
                }

                this.deposit(amount);
                System.out.println("Card is now being ejected.");
                return true;
            }
        }
        else if (option == 2){
            return false;
        }
        System.out.println("\nCorrect input only!");
        cardInput.nextLine(); // clear the input reader
        return false;
    }

    public void deposit(int amount) {
        this.transactionCount++; // Increase the number of overall transactions
        int finalAmount = this.currCard.getCurrBalance() + amount; // New balance of the card
        this.balanceATM = this.balanceATM + amount; // Increase the amount of cash in the atm
        this.currCard.setCurrBalance(finalAmount); // Increase the balance of the card
        System.out.println(" --------------- ");
        System.out.println("Receipt No: " +  this.transactionCount + "\n" + "Transaction type : Deposit\n" + "Amount deposited: " + amount + "\n" + "Current Balance: " + this.currCard.getCurrBalance());
        System.out.println(" --------------- ");
    }

    //This function controls the 'add' section of the maintenance for the admin
    //This is the interface asks the admin how much they would like to add to the current balance
    public void adminAdd(Scanner cardInput, ATM atm, Admin admin){
        System.out.println(" --------------- ");
        System.out.println("Current ATM balance is: " + atm.getBalanceATM());
        System.out.println(" --------------- ");

        boolean isDone = false; //checks if the loop is complete or not
        while(!(isDone)){
            System.out.print("Welcome to the maintenance section, How much would you like to add to the ATM balance? ");
            int amount = 0;
            if (cardInput.hasNextInt()) { //checking the input of the user
                amount = cardInput.nextInt();
                if (amount < 0){ //making sure the amount cant be negative
                    System.out.println("Error amount is invalid.");
                }
                else{
                    admin.addFunds(amount); //adds the amount to the ATM balance
                    isDone = true; //stops the loop
                }
            }
        }
    }

    //Gets the current balance of the ATM
    public int getBalanceATM(){
        return this.balanceATM;
    }
    //Adds 'amount' to the ATM balance
    public void addBalanceATM(int amount) {
        this.balanceATM = this.balanceATM + amount;
    }

    public void updateCardsCSV() {
        // Set up a 2D array which holds all details of the Cards stored in the atm.
        List<List<String>> cardsDetails = new ArrayList<List<String>>();

        // Create the format for the Card issue date and expiry date to convert to a string.
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        // Traverse through the cards stored in the atm and store the fields for the csv in a list.
        for (Card c : this.cards) {
            // Create a list with a capacity of 7
            List<String> card = new ArrayList<String>(7);
            for (int i = 0; i < 7; i++) {  // Initialise the elements in the list so values can be set by index
                card.add("");
            }

            String cardID = String.format("%05d", c.getAccNo()); // Convert int to a string and add the leading zeros if necessary.
            String name = c.getFullname();
            String pin = String.format("%04d", c.getPIN()); // Convert int to a string and add the leading zeros if necessary.
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

        // Create the csv writer
        try {
            FileWriter csvWriter = new FileWriter("src/main/java/cards.csv");
            // Write all the details of the cards into the csv file, having each card on a new line
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


    
    public static void main(String[] args) throws ParseException {
        // Create the atm and start it.
        ATM atm = new ATM("01/01/2021",199934690);
        atm.readCSV();
        Admin admin = new Admin(atm, 99999, 9999);
        Scanner cardInput = new Scanner(System.in); // Allows for the user to interact with the atm
        while (atm.running) {
            System.out.println();

            // Ask for card number - ensure that an int is given, not a different data type
            System.out.print("Enter your card number: ");
            int cardID = 0;
            // Check if the input given is an integer
            if (cardInput.hasNextInt()) {
                cardID = cardInput.nextInt();
            }
            else {
                System.out.println("Integers only!");
                cardInput.nextLine(); // clear the input reader
                continue; // this will send it back to the start of the loop, skipping the code after this
            }

            if (cardID == admin.getID()){
                boolean passCheck = atm.adminPinController(cardInput, admin);
                if(!passCheck){
                    continue;
                }
                boolean isFinished = false;
                while(!(isFinished)){
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

                    if (selection == 1){
                        atm.adminAdd(cardInput, atm, admin);
                        isFinished = true;

                    }
                    else if (selection == 2){
                        System.out.println(" --------------- ");
                        System.out.println("The current ATM balance is " + atm.getBalanceATM());
                        System.out.println(" --------------- ");
                    }
                    else if (selection == 3){
                        isFinished = true;
                    }
                    else {
                        System.out.println("Error: Invalid input please select from the given options.");
                        cardInput.nextLine(); // clear the input reader
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

            // If valid and pin is correct, provide the user with the 3 options (Withdraw, deposit, check balance)
            boolean isComplete = false;
            while(!isComplete) {  // Set up a loop to allow the system to repeatedly ask for a input if an incorrect input is provided.
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

                if(selection == 1) { // If withdraw is selected
                    isComplete = atm.withdrawController(cardInput);
                }
                else if(selection == 2) { // If deposit is selected
                    isComplete = atm.depositController(cardInput);
                }
                else if(selection == 3) { // If check balance is selected
                    System.out.println("Your current account balance is: " + atm.checkBalance());
                    isComplete = true;
                }
                else {  // Incorrect input
                    System.out.println("\nCorrect input only!");
                    cardInput.nextLine(); // clear the input reader
                    continue;
                }
                atm.updateCardsCSV(); // Update the balance of the csv file
            }
        }
    }
}
