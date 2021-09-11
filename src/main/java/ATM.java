//Pass the date in the constructor
//Check the isLost for confiscate
//Create the main loop
//Create checkers methods for Card checks
//Create a card instance
//Create a function that reads the csv, for each of the lines create a card and then store cards in an arraylist
public class ATM {
    int PINattempts = 0; //To count how many times pin has been entered
    int funds = 0; //amount of money in ATM

    public void confiscate(){
        //confiscate the card, make it invalid
    }

    public String enoughBalance(){
        return "Not enough money";
    }

    public int checkBalance(){
        return 0;
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



//While true loop, ends when we need it to
//Entry message before the loop
//Scanner for Card number, if number is valid
//Scan for pin, check if pins counter that has a max of 3
//Present options
//Scan for input
    public static void main(String args[]){
        System.out.println("test");
    }


}
