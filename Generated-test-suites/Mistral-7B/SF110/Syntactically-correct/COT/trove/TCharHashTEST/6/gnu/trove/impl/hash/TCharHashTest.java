import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
    }

    @Test
    public void testInitialCapacityConstructor() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(10, charHash.size());
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        char noEntryValue = 'A';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(10, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityConstructor() {
        new TCharHash(-5);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        char defaultValue = charHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(5);
        int capacity = charHash.setUp(5);
        assertEquals(5, capacity);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        assertTrue(charHash.contains(key));
    }

    @Test
    public void testDoesNotContain() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        assertFalse(charHash.contains(key));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char charToTest) {
                assertEquals(key, charToTest);
                return true;
            }
        };
        charHash.forEach(procedure);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtWhenEmpty() {
        TCharHash charHash = new TCharHash();
        int index = 0;
        charHash.removeAt(index);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        int index = charHash.indexOf(key);
        charHash.removeAt(index);
        assertFalse(charHash.contains(key));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsException() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        int index = charHash.size();
        charHash.removeAt(index);
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        int index = charHash.indexOf(key);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key);
        int index = charHash.indexOf('B');
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        int index = charHash.insertKey(key);
        assertTrue(charHash.contains(key));
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyWhenFull() {
        TCharHash charHash = new TCharHash(2);
        char key = 'A';
        charHash.put(key);
        charHash.put('B');
        try {
            charHash.insertKey(key);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
        }
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.insertKey(key);
        int index = charHash.indexOf(key);
        charHash.insertKeyRehash(key, index, HashFunctions.hash(key), (byte) 1);
        assertEquals(0, index);
        assertTrue(charHash.contains(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TCharHash(10, -1.25f);
    }

    @Test
    public void testConstructorWithNegativeInitialCapacityAndLoadFactor() {
        try {
            new TCharHash(-5, 0.75f);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Initial capacity cannot be negative", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithNegativeInitialCapacity() {
        try {
            new TCharHash(-5);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Initial capacity cannot be negative", e.getMessage());
        }
    }
}