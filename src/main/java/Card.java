import java.util.Date;

public class Card {
    private int AccNo;
    private int PIN;
    private boolean isLost;
    private Date issueDate = new Date();
    private Date expiryDate = new Date();
    private int attempts;
    
    public Card(int n, int p, Date iDate, Date eDate) {
        this.AccNo = n;
        this.PIN = p;
        this.isLost = False;
        this.issueDate = iDate;
        this.expiryDate = eDate;
        this.attempts = 0;
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
    
    public int getAttempts() {
        return this.attempts;
    }
    
    public void setAttempts(int attempt) {
        this.attempts += attempt;
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
