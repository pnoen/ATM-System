//Pass the date in the constructor
//Check the isLost for confiscate
//Create the main loop
//Create checkers methods for Card checks
//Create a card instance
//Create a function that reads the csv, for each of the lines create a card and then store cards in an arraylist

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class ATM {
    String currentDate;
    String fileName;
    List<Card> cards = new ArrayList<>();

    public ATM(String currentDate, String fileName){
        this.currentDate = currentDate;
        this.fileName = fileName;
    }

    public void readCSV(){


    }


    public Card createCard(String cardID, String fullname, String pin, String currBalance, String issueDate, String expiryDate) throws ParseException {
        //Converting the String to Date Format
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date iDate = sdf.parse(issueDate);
        Date eDate = sdf.parse(expiryDate);

        int cardIDInt = Integer.parseInt(cardID);
        int pinInt = Integer.parseInt(pin);
        int currBalanceInt = Integer.parseInt(currBalance);
        Card newCard = new Card(cardIDInt, fullname, pinInt, currBalanceInt, iDate, eDate);

        return newCard;
    }

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
    public static void main(String args[]){
        System.out.println("test");
    }


}
