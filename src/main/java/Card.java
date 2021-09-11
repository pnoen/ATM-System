import java.util.Date;

public class Card {
    private int cardID; // Account Number
    private int pin; // Account PIN Number
    private int currBalance;
    private boolean isLost; // Is the card lost or stolen
    private String fullname;
    private Date issueDate; // Date of issue
    private Date expiryDate; // Expiry date
    
    public Card(int cardID, String fullname, int pin, int currBalance, Date issueDate, Date expiryDate) {
        this.cardID = cardID;
        this.pin = pin;
        this.fullname = fullname;
        this.currBalance = currBalance;
        this.isLost = false;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    
    public int getAccNo() {
        return this.cardID;
    }
    
    public Date getIssueDate() {
        return this.issueDate;
    }
    
    public Date getExpiryDate() {
        return this.expiryDate;
    }
    
    public boolean getIsLost() {
        return this.isLost;
    }
    
    public int getPIN() {
        return this.pin;
    }
    
    public boolean isValid(){
        //whether the card entered is a valid card number
        return true;
    }

    public boolean isLost(){
        //whether the card was reported stolen or lost
        return false;
    }



}
