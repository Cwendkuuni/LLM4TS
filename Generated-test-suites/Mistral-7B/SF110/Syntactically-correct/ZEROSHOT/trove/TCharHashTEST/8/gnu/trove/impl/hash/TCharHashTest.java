import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TCharHashTest {

    private TCharHash charHash;

    @Before
    public void setUp() {
        charHash = new TCharHash(5);
    }

    @Test
    public void testConstructor() {
        assertNotNull(charHash);
        assertEquals(5, charHash.size());
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test(expected = NullPointerException.class)
    public void testContainsNullValue() {
        charHash.contains((char) 0);
    }

    @Test
    public void testContainsValidValue() {
        charHash.put('A', 'A');
        assertTrue(charHash.contains('A'));
    }

    @Test
    public void testForEachProcedure() {
        charHash.put('A', 'A');
        charHash.put('B', 'B');

        int count = 0;
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char charValue) {
                count++;
                return true;
            }
        };

        charHash.forEach(procedure);

        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        charHash.put('A', 'A');
        charHash.put('B', 'B');

        int initialSize = charHash.size();
        charHash.removeAt(0);

        assertEquals(initialSize - 1, charHash.size());
    }

    @Test
    public void testIndex() {
        charHash.put('A', 'A');
        charHash.put('B', 'B');

        assertEquals(0, charHash.index('A'));
        assertEquals(1, charHash.index('B'));
        assertNull(charHash.index('C'));
    }

    @Test
    public void testInsertKey() {
        charHash.put('A', 'A');

        int index = charHash.insertKey('B');
        assertEquals(1, index);
        assertEquals('B', charHash.get(0));

        index = charHash.insertKey('C');
        assertEquals(2, index);
        assertEquals('C', charHash.get(1));
    }

    @Test
    public void testInsertKeyRehash() {
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');

        int initialSize = charHash.size();
        charHash.put('D');

        assertEquals(initialSize + 1, charHash.size());
    }
}