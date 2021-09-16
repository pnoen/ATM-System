
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Date;

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
    void testCardCreation() throws ParseException{
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 199934690);
        Card test_card = valid_atm.createCard("123", "alex", "123", "0", "01/12/2020", "10/12/2020", "false");
        assertEquals("alex", test_card.getFullname());
        assertEquals(0, test_card.getCurrBalance());
        assertEquals(123, test_card.getAccNo());
        assertEquals(123, test_card.getPIN());
        assertEquals(false, test_card.getIsLost());
    }

    @Test
    void testAdminCreation(){
        ATM test_atm = new ATM("01/01/2021", "cards.csv", 10);
        Admin admin = new Admin(test_atm, 99999, 9999);
        assertEquals(99999, admin.getID());
        assertEquals(9999, admin.getPin());
        admin.addFunds(10);
        assertEquals(20, test_atm.getBalanceATM());

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
}


