import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCard {

    @Test
    void testCardCreation() throws ParseException {
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 199934690);
        Card test_card = valid_atm.createCard("123", "alex", "123", "0", "01/12/2020", "10/12/2020", "false");
        assertEquals("alex", test_card.getFullname());
        assertEquals(0, test_card.getCurrBalance());
        assertEquals(123, test_card.getAccNo());
        assertEquals(123, test_card.getPIN());
        assertEquals(false, test_card.getIsLost());


    }

    @Test
    void testCardBlock() throws ParseException{
        ATM valid_atm = new ATM("01/01/2021", "cards.csv", 199934690);
        Card test_card = valid_atm.createCard("123", "alex", "123", "0", "01/12/2020", "10/12/2020", "false");
        assertEquals(false, test_card.getBlockState());
        test_card.setBlockState(true);
        assertEquals(true, test_card.getBlockState());
    }
}
