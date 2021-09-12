

public class Admin {
    private ATM atm;
    private int ID;
    private int pin;

    public Admin(ATM atm,int ID, int pin) {
        this.atm = atm;
        this.ID = ID;
        this.pin = pin;
    }

    public void addFunds(int amount){
        this.atm.addBalanceATM(amount);

        System.out.println(" --------------- ");
        System.out.println("The new ATM balance is " + this.atm.getBalanceATM());
        System.out.println(" --------------- ");

    }
    public int getID(){return this.ID;}
    public int getPin(){return this.pin;}



}
