import java.util.Date;

// Declare card attributes
public class Card {
    private int cardID; // Account Number
    private int pin; // Account PIN Number
    private int currBalance;
    private boolean isLost; // Is the card lost or stolen
    private boolean isBlocked;
    private String fullname;
    private Date issueDate; // Date of issue
    private Date expiryDate; // Expiry date

    // Assigns values to all card attributes from respective parameters
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

    // Returns name of Card holder
    public String getFullname() {
        return this.fullname; 
    }

    // Returns card number
    public int getAccNo() {
        return this.cardID;
    }

    // After 3 incorrect PIN attempts, enable 'blocked' state
    public void setBlockState(boolean state) {
        this.isBlocked = state;
    }

    // Returns block state
    public boolean getBlockState() {
        return this.isBlocked;
    }

    // Returns card's issue date
    public Date getIssueDate() {
        return this.issueDate;
    }

    // Returns card's expiry date
    public Date getExpiryDate() {
        return this.expiryDate;
    }

    // Returns card's 'lost' or 'stolen' state
    public boolean getIsLost() {
        return this.isLost;
    }

    // Returns card PIN
    public int getPIN() {
        return this.pin;
    }

    // Sets card balance
    public void setCurrBalance(int newBalance) {
        this.currBalance = newBalance;
    }

    // Returns current card balance
    public int getCurrBalance() {
        return this.currBalance;
    }
}
