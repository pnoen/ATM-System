
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TestATM {

    @Test
    void testConstructor() {
        ATM test = new ATM("Hello", "test_cards", 100000);
    }

    @Test
    void testFileReader() throws ParseException {

        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 199934690);
        ATM invalid_atm = new ATM("01/01/2021", "fakecards.csv", 199934690);

        //Assertions.assertThrows(FileNotFoundException.class, ()-> invalid_atm.readCSV());
        //assertEquals("Could not load the database", valid_atm.readCSV());
    }

    @Test
    void testBalance() throws ParseException{
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        valid_atm.readCSV();
        Admin admin = new Admin(valid_atm, 99999, 9999);
        assertEquals(10, valid_atm.getBalanceATM());

    }

    @Test
    void testCheckValid() throws ParseException{
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        valid_atm.readCSV();
        assertTrue(valid_atm.checkValid(00006));
        assertFalse(valid_atm.checkValid(0055));
    }

    @Test
    void testWithdraw() throws ParseException{
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10000);
        valid_atm.readCSV();
        valid_atm.checkValidID(9);
        Card card = valid_atm.getCurrCard();
        valid_atm.withdraw(1000);
        assertTrue(valid_atm.getBalanceATM() == 9000);
        assertTrue(card.getCurrBalance() == 9100);


    }

    @Test
    void testDeposit() throws ParseException{
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10000);
        valid_atm.readCSV();
        valid_atm.checkValidID(9);
        Card card = valid_atm.getCurrCard();
        valid_atm.deposit(1000);
        assertTrue(valid_atm.getBalanceATM() == 11000);
        assertTrue(card.getCurrBalance() == 11100);
    }

    @Test
    void testWithdrawController() throws ParseException, NoSuchElementException {
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        //valid_atm.readCSV();
        Card test_card = valid_atm.createCard("123", "alex", "123", "5", "01/12/2020", "10/12/2020", "false");

        //testing improper input
        //Scanner card_inp = new Scanner("1-1");
        //assertEquals(false, valid_atm.withdrawController(card_inp));

        //withdrawing too much amount (cause of my card)
        valid_atm.setCurrCard(test_card);
        Scanner card_inp1 = new Scanner("1 6");
        assertEquals(false, valid_atm.withdrawController(card_inp1));

        //withdrawing impossible amount
        valid_atm.setCurrCard(test_card);
        Scanner card_inp4 = new Scanner("1 -1");
        assertEquals(false, valid_atm.withdrawController(card_inp4));

        //withdrawing right  amount
        valid_atm.setCurrCard(test_card);
        Scanner card_inp2 = new Scanner("1 4");
        assertEquals(true, valid_atm.withdrawController(card_inp2));

    }

    @Test
    void testWithdrawControllerMore() throws ParseException, NoSuchElementException {
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        //valid_atm.readCSV();
        Card test_card = valid_atm.createCard("123", "alex", "123", "20", "01/12/2020", "10/12/2020", "false");

        //withdrawing too much amount (cause of the atm balance)
        valid_atm.setCurrCard(test_card);
        Scanner card_inp3 = new Scanner("1 11");
        assertEquals(false, valid_atm.withdrawController(card_inp3));

        //selecting a number that's not an option
        //valid_atm.setCurrCard(test_card);
        //Scanner card_inp5 = new Scanner("4");
        //assertEquals(false, valid_atm.withdrawController(card_inp5));

        //pressing cancel
        valid_atm.setCurrCard(test_card);
        Scanner card_inp6 = new Scanner("2");
        assertEquals(false, valid_atm.withdrawController(card_inp6));
    }

    @Test
    void testDepositController() throws ParseException, NoSuchElementException {
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        //valid_atm.readCSV();
        Card test_card = valid_atm.createCard("123", "alex", "123", "5", "01/12/2020", "10/12/2020", "false");

        //testing improper input
        //Scanner card_inp = new Scanner("1-1");
        //assertEquals(false, valid_atm.withdrawController(card_inp));

        // depositing not an australian note
        valid_atm.setCurrCard(test_card);
        Scanner card_inp1 = new Scanner("1 6");
        assertEquals(false, valid_atm.depositController(card_inp1));

        //depositing impossible amount
        valid_atm.setCurrCard(test_card);
        Scanner card_inp4 = new Scanner("1 -1");
        assertEquals(false, valid_atm.depositController(card_inp4));

        //depositing right  amount
        valid_atm.setCurrCard(test_card);
        Scanner card_inp2 = new Scanner("1 5");
        assertEquals(true, valid_atm.depositController(card_inp2));

    }
    @Test
    void testPinController() throws ParseException, NoSuchElementException {

        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        //valid_atm.readCSV()
        Card test_card = valid_atm.createCard("123", "alex", "123", "5", "01/12/2020", "10/12/2020", "false");
        valid_atm.setCurrCard(test_card);

        //testing it working
        Scanner pin_inp = new Scanner("123");
        assertEquals(true, valid_atm.pinController(pin_inp));

        //testing it with wrong pin
        Scanner pin_inp1 = new Scanner("122 122 122 122");
        assertEquals(false, valid_atm.pinController(pin_inp1));

        //testing wrong and eventually right
        //testing it with wrong pin
        Scanner pin_inp2 = new Scanner("122 122 123 123");
        assertEquals(true, valid_atm.pinController(pin_inp2));
    }

    @Test
    void testAdminPinController() throws ParseException, NoSuchElementException {

        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        //valid_atm.readCSV()
        Card test_card = valid_atm.createCard("123", "alex", "123", "5", "01/12/2020", "10/12/2020", "false");
        valid_atm.setCurrCard(test_card);
        Admin admin = new Admin(valid_atm, 99999, 9999);

        //testing it working
        Scanner pin_inp3 = new Scanner("9999");
        assertEquals(true, valid_atm.adminPinController(pin_inp3, admin));

        //testing it not working
        Scanner pin_inp4 = new Scanner("999");
        assertEquals(false, valid_atm.adminPinController(pin_inp4, admin));

        //testing it not working
        Scanner pin_inp5 = new Scanner("");
        assertEquals(false, valid_atm.adminPinController(pin_inp5, admin));


    }

    @Test
    void testAdminAdd() throws ParseException, NoSuchElementException {

        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 10);
        //valid_atm.readCSV()
        Card test_card = valid_atm.createCard("123", "alex", "123", "5", "01/12/2020", "10/12/2020", "false");
        valid_atm.setCurrCard(test_card);
        Admin admin = new Admin(valid_atm, 99999, 9999);

        //normal adding
        Scanner add_inp = new Scanner("10");
        valid_atm.adminAdd(add_inp, valid_atm, admin);
        assertEquals(20, valid_atm.getBalanceATM());


    }

    @Test
    void testMain() throws ParseException, NoSuchElementException {
        String userInput = "00006 0110 3";
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        //String expected = "";
        //ByteArrayOutputStream boas = new ByteArrayOutputStream();
        //PrintStream printStream = new PrintStream(boas);
        //System.setOut(printStream);
        ATM.main(null);

        //String[] lines = boas.toString().split(" ");
        //String actual = lines[lines.length-1];
        //System.out.print(actual);



    }
}


