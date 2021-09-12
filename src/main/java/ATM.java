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
    private boolean running = true;
    private String currentDate;
    private String fileName;
    private List<Card> cards = new ArrayList<>();
    private Card currCard = null;
    private int transactionCount = 0;
    private int balanceATM;

    public ATM(String currentDate, String fileName, int balanceATM){
        this.currentDate = currentDate;
        this.fileName = fileName;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
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

    public int checkBalance() {
        return this.currCard.getCurrBalance();
    }

    public boolean pinController(Scanner cardInput) {
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
    public boolean adminPinController(Scanner cardInput, Admin admin){
        System.out.println();
        System.out.print("Enter your card pin: ");
        if (cardInput.hasNextInt()) {
            int cardP = cardInput.nextInt();
            return cardP == admin.getPin();
        }

        return false;
    }

    public boolean withdrawController(Scanner cardInput) {
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
        this.transactionCount++;
        int finalAmount = this.currCard.getCurrBalance() - amount;
        this.balanceATM = this.balanceATM - amount;
        this.currCard.setCurrBalance(finalAmount);
        System.out.println(" --------------- ");
        System.out.println("Receipt No: " +  this.transactionCount + "\n" + "Transaction type : Withdraw\n" + "Amount withdrawed: " + amount + "\n" + "Current Balance: " + this.currCard.getCurrBalance());
        System.out.println(" --------------- ");
    }

    public boolean depositController(Scanner cardInput) {
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
        this.transactionCount++;
        int finalAmount = this.currCard.getCurrBalance() + amount;
        this.balanceATM = this.balanceATM + amount;
        this.currCard.setCurrBalance(finalAmount);
        System.out.println(" --------------- ");
        System.out.println("Receipt No: " +  this.transactionCount + "\n" + "Transaction type : Deposit\n" + "Amount deposited: " + amount + "\n" + "Current Balance: " + this.currCard.getCurrBalance());
        System.out.println(" --------------- ");
    }

    public void adminAdd(Scanner cardInput, ATM atm, Admin admin){
        System.out.println(" --------------- ");
        System.out.println("Current ATM balance is: " + atm.getBalanceATM());
        System.out.println(" --------------- ");

        boolean isDone = false;
        while(!(isDone)){
            System.out.print("Welcome to the maintenance section, How much would you like to add to the ATM balance? ");
            int amount = 0;
            if (cardInput.hasNextInt()) {
                amount = cardInput.nextInt();
                if (amount < 0){
                    System.out.println("Error amount is invalid.");
                }
                else{
                    admin.addFunds(amount);
                    isDone = true;
                }
            }
        }
    }

    public int getBalanceATM(){
        return this.balanceATM;
    }
    public void addBalanceATM(int amount) {
        this.balanceATM = this.balanceATM + amount;
    }

    public void updateCardsCSV() {
        List<List<String>> cardsDetails = new ArrayList<List<String>>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        for (Card c : this.cards) {
            List<String> card = new ArrayList<String>(7);
            for (int i = 0; i < 7; i++) {
                card.add("");
            }

            String cardID = String.format("%05d", c.getAccNo());
            String name = c.getFullname();
            String pin = String.format("%04d", c.getPIN());
            String bal = String.valueOf(c.getCurrBalance());

            String iDate = dateFormat.format(c.getIssueDate());
            String eDate = dateFormat.format(c.getExpiryDate());

            String lost = String.valueOf(c.getIsLost());

            card.set(0, cardID);
            card.set(1, name);
            card.set(2, pin);
            card.set(3, bal);
            card.set(4, iDate);
            card.set(5, eDate);
            card.set(6, lost);
            cardsDetails.add(card);
        }

        try {
            FileWriter csvWriter = new FileWriter("src/main/java/cards.csv");
            for (List<String> c : cardsDetails) {
                csvWriter.append(String.join(",", c));
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }




    
    public static void main(String[] args) throws ParseException {
        // Create the atm and start it.
        ATM atm = new ATM("01/01/2021", "cards.csv", 100000);
        atm.readCSV();
        Admin admin = new Admin(atm, 99999, 9999);
        Scanner cardInput = new Scanner(System.in);
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

            // If card exist, ask for PIN
            boolean pinCheck = atm.pinController(cardInput);
            if (pinCheck == false) {
                continue;
            }

            boolean isComplete = false;
            while(!(isComplete)){
                System.out.print("\nWelcome to XYZ ATM "  + atm.currCard.getFullname() +  ", what would you like to do: \n" +
                        "1. Withdrawal of funds\n" +
                        "2. Deposit of Funds\n" +
                        "3. Balance Check\n" +
                        "Please enter the number corresponding to the action: ");

                int selection = 0;
                if (cardInput.hasNextInt()) {
                    selection = cardInput.nextInt();
                }

                if(selection == 1){
                    isComplete = atm.withdrawController(cardInput);
                }
                else if(selection == 2){
                    isComplete = atm.depositController(cardInput);
                }
                else if(selection == 3){
                    System.out.println("Your current account balance is: " + atm.checkBalance());
                    isComplete = true;
                }
                else {
                    System.out.println("\nCorrect input only!");
                    cardInput.nextLine(); // clear the input reader
                    continue;
                }
                atm.updateCardsCSV();
            }

            // If valid and pin is correct, provide the user with the 4 options (Withdraw, deposit, check balance, exit account)
                // if exit account is selected, return the user back to the home screen (where the user enters a card number)

        }


    }


}
