import java.util.Date;

public class Card {
    private int AccNo; // Account Number
    private int PIN; // Account PIN Number
    private int bal; // Account balance
    private boolean isLost; // Is the card lost or stolen
    private Date issueDate = new Date(); // Date of issue
    private Date expiryDate = new Date(); // Expiry date
    
    public Card(int n, int p, int b, Date iDate, Date eDate) {
        this.AccNo = n;
        this.PIN = p;
        this.isLost = false;
        this.issueDate = iDate;
        this.expiryDate = eDate;
    }
    
    public void setPIN(int p) {
        this.PIN = p;
    }
    
    public int getAccNo() {
        return this.AccNo;
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
        return this.PIN;
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
