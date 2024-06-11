
package test;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import org.junit.*;

public class TCharHashTest {

    @BeforeClass
    public static void setUpBeforeClass() {
    }

    @AfterClass
    public static void tearDownAfterClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // ... test cases here ...

    @Test
    public void testInitialCapacityLessThanZero() {
        new TCharHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFactorLessThanZero() {
        new TCharHash(5, -0.25f);
    }

    @Test
    public void testAddingExistingKey() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A');
        charHash.put('A');
        assertEquals(1, charHash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddingKeyCausingOverflow() {
        TCharHash charHash = new TCharHash(5);
        for (char key = 'A'; key <= 'Z'; key++) {
            charHash.put(key);
        }
    }

    @Test
    public void testRemovingNonExistingKey() {
        TCharHash charHash = new TCharHash(5);
        charHash.removeAt(10);
        assertEquals(5, charHash.size());
    }
}
