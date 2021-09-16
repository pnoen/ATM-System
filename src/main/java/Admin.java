//This class is responsible for managing the Admin aspect of the ATM and ensuring the Admin can manage and maintain the ATM

public class Admin {
    private ATM atm;
    private int ID;
    private int pin;

    //Constructor variables setting the login for the admin
    public Admin(ATM atm,int ID, int pin) {
        this.atm = atm;
        this.ID = ID;
        this.pin = pin;
    }

    //This function is responsible for adding money to the ATM
    public void addFunds(int amount){
        this.atm.addBalanceATM(amount);

        System.out.println(" --------------- ");
        System.out.println("The new ATM balance is " + this.atm.getBalanceATM());
        System.out.println(" --------------- ");

    }
    //Returns the specified Card Number/ID for the Admin
    public int getID(){return this.ID;}
    //Returns the specific pin for the Admin
    public int getPin(){return this.pin;}



}
