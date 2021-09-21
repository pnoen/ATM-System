import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Date;


public class TestAdmin {

    @Test
    void testAdminCreation(){
        ATM test_atm = new ATM("01/01/2021", "cards.csv", 10);
        Admin admin = new Admin(test_atm, 99999, 9999);
        assertEquals(99999, admin.getID());
        assertEquals(9999, admin.getPin());
        admin.addFunds(10);
        assertEquals(20, test_atm.getBalanceATM());

    }
}
