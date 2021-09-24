import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TestATM {
    private ATM valid_atm1;
    private ATM valid_atm2;
    private Card test_card1;
    private Card test_card2;

    @BeforeEach
    public void setup() throws ParseException {
        //setting up two different atms, difference is atm balance
        valid_atm1 = new ATM("01/01/2021", "cards.csv", 10);
        valid_atm1.readCSV();

        valid_atm2 = new ATM("01/01/2021", "cards.csv", 10000);
        valid_atm2.readCSV();

        //setting up two different test cards, difference is balance
        test_card1 = valid_atm1.createCard("123", "alex", "123", "5", "01/12/2020", "10/12/2020", "false");
        test_card2 = valid_atm1.createCard("123", "alex", "123", "20", "01/12/2020", "10/12/2020", "false");
    }

    @Test
    void testConstructor() throws ParseException {
        //testing to ensure constructor sets values correctly
        assertEquals(10, valid_atm1.getBalanceATM());
        assertNotEquals(12, valid_atm1.getBalanceATM());
    }

    @Test
    void testCheckValid() throws ParseException{
        //checking if check valid works
        assertTrue(valid_atm1.checkValid(00006));
        assertFalse(valid_atm1.checkValid(0055));
    }

    @Test
    void testWithdraw() throws ParseException{
        valid_atm2.checkValidID(9);
        Card card = valid_atm2.getCurrCard();

        //ensuring withdraw function works
        valid_atm2.withdraw(1000);
        assertTrue(valid_atm2.getBalanceATM() == 9000);
        assertTrue(card.getCurrBalance() == 9100);
    }

    @Test
    void testDeposit() throws ParseException{
        valid_atm2.checkValidID(9);
        Card card = valid_atm2.getCurrCard();

        //ensuring deposit works
        valid_atm2.deposit(1000);
        assertTrue(valid_atm2.getBalanceATM() == 11000);
        assertTrue(card.getCurrBalance() == 11100);
    }

    @Test
    void testWithdrawController() throws ParseException, NoSuchElementException {
        valid_atm1.setCurrCard(test_card1);

        //withdrawing too much amount (cause of my card)
        Scanner card_inp1 = new Scanner("1 6");
        assertFalse(valid_atm1.withdrawController(card_inp1));

        //withdrawing impossible amount
        Scanner card_inp4 = new Scanner("1 -1");
        assertFalse(valid_atm1.withdrawController(card_inp4));

        //withdrawing right  amount
        Scanner card_inp2 = new Scanner("1 4");
        assertTrue(valid_atm1.withdrawController(card_inp2));
    }

    @Test
    void testWithdrawControllerMore() throws ParseException, NoSuchElementException {
        valid_atm1.setCurrCard(test_card2);

        //withdrawing too much amount (cause of the atm balance)
        Scanner card_inp3 = new Scanner("1 11");
        assertFalse(valid_atm1.withdrawController(card_inp3));

        //pressing cancel
        Scanner card_inp6 = new Scanner("2");
        assertFalse(valid_atm1.withdrawController(card_inp6));
    }

    @Test
    void testDepositController() throws ParseException, NoSuchElementException {
        valid_atm1.setCurrCard(test_card1);

        //depositing not an australian note
        Scanner card_inp1 = new Scanner("1 6");
        assertFalse(valid_atm1.depositController(card_inp1));

        //depositing impossible amount
        Scanner card_inp4 = new Scanner("1 -1");
        assertFalse(valid_atm1.depositController(card_inp4));

        //depositing right amount
        Scanner card_inp2 = new Scanner("1 5");
        assertTrue(valid_atm1.depositController(card_inp2));
    }

    @Test
    void testPinController() throws ParseException, NoSuchElementException {
        valid_atm1.setCurrCard(test_card1);

        //testing it working
        Scanner pin_inp = new Scanner("123");
        assertTrue(valid_atm1.pinController(pin_inp));

        //testing it with wrong pin
        Scanner pin_inp1 = new Scanner("122 122 122 122");
        assertFalse(valid_atm1.pinController(pin_inp1));

        //testing wrong and eventually right
        Scanner pin_inp2 = new Scanner("122 122 123 123");
        assertTrue(valid_atm1.pinController(pin_inp2));
    }

    @Test
    void testAdminPinController() throws ParseException, NoSuchElementException {
        valid_atm1.setCurrCard(test_card1);
        Admin admin = new Admin(valid_atm1, 99999, 9999);

        //testing it working
        Scanner pin_inp3 = new Scanner("9999");
        assertTrue(valid_atm1.adminPinController(pin_inp3, admin));

        //testing it not working
        Scanner pin_inp4 = new Scanner("999");
        assertFalse(valid_atm1.adminPinController(pin_inp4, admin));

        //testing it not working
        Scanner pin_inp5 = new Scanner("");
        assertFalse(valid_atm1.adminPinController(pin_inp5, admin));
    }

    @Test
    void testAdminAdd() throws ParseException, NoSuchElementException {
        valid_atm1.setCurrCard(test_card1);
        Admin admin = new Admin(valid_atm1, 99999, 9999);

        //testing normal adding
        Scanner add_inp = new Scanner("10");
        valid_atm1.adminAdd(add_inp, valid_atm1, admin);
        assertEquals(20, valid_atm1.getBalanceATM());
    }

    @Test
    void testMain() throws ParseException, NoSuchElementException {
        //testing check balance feature

        //passing through input
        String userInput = "2 00006 0110 3";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(inputStream);

        //catching output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        ATM.main(null);

        //comparing expected output to what we actually return
        String expected = "47567";
        String[] output = outputStream.toString().split(" ");
        String actual = output[output.length-1];
        assertEquals(expected, actual.trim());

    }

}


