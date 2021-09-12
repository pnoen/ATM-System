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

    public String getFullname(){return this.fullname; }
    
    public int getAccNo() {
        return this.cardID;
    }

    public void setBlockState(boolean state){
        this.isBlocked = state;
    }

    public boolean getBlockState(){
        return this.isBlocked;
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

    public void setCurrBalance(int newBalance){
        this.currBalance = newBalance;
    }
    public int getCurrBalance() {
        return this.currBalance;
    }
    
    public boolean isValid(){
        //whether the card entered is a valid card number
        return true;
    }





}
