
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
        Card test_card = valid_atm.createCard("123", "alex", "123", "0", "1/12/2020", "1/12/2020", "false");
        assertEquals("alex", test_card.getFullname());
    }
}


