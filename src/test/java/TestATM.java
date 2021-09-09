
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestATM {

    @Test
    void testRandom() {
        ATM test = new ATM();
        String printer = test.noFunds();
        //test.add where add is the method created in the JUnitDemo class
        assertEquals("Not enough money inside ATM", printer);
    }


}
