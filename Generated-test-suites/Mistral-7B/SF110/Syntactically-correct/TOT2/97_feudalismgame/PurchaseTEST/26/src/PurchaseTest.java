import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import src.Fiefdoms;
import src.Knight;
import src.CurrentPlayers;
import src.Actions.Purchase;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseTest {

    // Test cases for perform method here (similar to Expert 1's test)

    private static final String TEST_FIEFDOMS_NAME = "testFiefdom";
    private static final String TEST_KNIGHT_NAME = "testKnight";
    private static final String TEST_BUYER_NAME = "testBuyer";
    private static final int TEST_QTY = 5;
    private static final int TEST_CASTLE_PRICE = 100;
    private static final String TEST_OWNER_NAME = "testOwner";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVassal_TypicalUseCase() {
        // Test for typical use case of vassal method here (similar to Expert 1's test)
    }

    @Test
    public void testVassal_InsufficientFunds() {
        // Test for insufficient funds scenario of vassal method here
    }

    // Test cases for other methods (castleFiefdom, castlePersonal, mercenary) with various edge cases and error scenarios
}