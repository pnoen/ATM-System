import java.util.Date;

public class Card {
    private int cardID; // Account Number
    private int pin; // Account PIN Number
    private int currBalance;
    private boolean isLost; // Is the card lost or stolen
    private boolean isBlocked;
    private String fullname;
    private Date issueDate; // Date of issue
    private Date expiryDate; // Expiry date

    //Sets all the variables
    public Card(int cardID, String fullname, int pin, int currBalance, Date issueDate, Date expiryDate, boolean isLost) {
        this.cardID = cardID;
        this.pin = pin;
        this.fullname = fullname;
        this.currBalance = currBalance;
        this.isLost = isLost;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.isBlocked = false;
    }

    ///Returns the name of the Card holder
    public String getName(){return this.fullname; }

    //Returns the card number
    public int getCardID() {
        return this.cardID;
    }

    //If the pin fails 3 times, this fucntion sets the block state when needed
    public void setBlockState(boolean state){
        this.isBlocked = state;
    }

    //Returns the block state
    public boolean getBlockState(){
        return this.isBlocked;
    }

    //Returns the issue date of the card
    public Date getIssueDate() {
        return this.issueDate;
    }

    //Returns the expiry date of the card
    public Date getExpiryDate() {
        return this.expiryDate;
    }

    //Returns the lost/stolen state of the Card
    public boolean getIsLost() {
        return this.isLost;
    }

    //Returns the pin for the card
    public int getPIN() {
        return this.pin;
    }

    //Sets the balance of the card
    public void setCurrBalance(int newBalance){
        this.currBalance = newBalance;
    }

    //Gets the current balance of the card
    public int getCurrBalance() {
        return this.currBalance;
    }
}
