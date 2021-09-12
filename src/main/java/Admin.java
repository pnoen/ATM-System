

public class Admin {
    private ATM atm;

    public Admin(ATM atm){
        this.atm = atm;
    }

    public void addFunds(int amount){
        this.atm.addBalanceATM(amount);

        System.out.println(" --------------- ");
        System.out.println("The new ATM balance is " + this.atm.getBalanceATM());
        System.out.println(" --------------- ");

    }



}
