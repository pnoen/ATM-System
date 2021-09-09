
public class ATM {
    int blockCount = 0; //To count how many times pin has been entered
    int funds = 0; //amount of money in ATM

    public void confiscate(){
        //confiscate the card, make it invalid
    }

    public String enoughBalance(){
        return "Not enough money";
    }

    public int checkBalance(){
        Transaction money = new Transaction();
        return money.checkBalance();
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


    public static void main(String args[]){
        System.out.println("test");
    }


}
