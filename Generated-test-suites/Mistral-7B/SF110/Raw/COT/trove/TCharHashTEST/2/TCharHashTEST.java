import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testTCharHashConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
    }

    @Test
    public void testTCharHashConstructorWithInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(0, charHash.size());
        assertEquals(10, charHash.capacity());
    }

    @Test
    public void testTCharHashConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(0, charHash.size());
        assertEquals(10, charHash.capacity());
        assertEquals(0.75f, charHash.loadFactor(), 0.01);
    }

    @Test
    public void testTCharHashConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'A';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(0, charHash.size());
        assertEquals(10, charHash.capacity());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        char[] set = charHash._set;
        Arrays.fill(set, noEntryValue);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        char noEntryValue = charHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(10);
        int capacity = charHash.setUp(10);
        assertEquals(10, capacity);
        char[] set = charHash._set;
        Arrays.fill(set, Constants.DEFAULT_CHAR_NO_ENTRY_VALUE);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(10);
        char key = 'A';
        assertFalse(charHash.contains(key));
        charHash.put(key);
        assertTrue(charHash.contains(key));
        charHash.remove(key);
        assertFalse(charHash.contains(key));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(3);
        char[] keys = {'A', 'B', 'C'};
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                assertEquals(character, keys[index++]);
                return true;
            }
        };

        for (char key : keys) {
            charHash.put(key);
        }

        charHash.forEach(procedure);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtWithEmptyList() {
        TCharHash charHash = new TCharHash();
        int index = 0;
        charHash.removeAt(index);
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(3);
        char key = 'A';
        charHash.put(key);
        int index = charHash.index(key);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash(3);
        char key = 'B';
        charHash.put('A');
        int index = charHash.indexRehashed(key, 0, 1, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(3);
        char key = 'A';
        int index = charHash.insertKey(key);
        assertEquals(0, index);
        charHash.put(key);
        assertTrue(charHash.contains(key));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash(3);
        char key = 'B';
        int index = charHash.insertKeyRehash(key, 0, 1, (byte) 1);
        assertEquals(-1, index);

        charHash.put('A');
        index = charHash.insertKeyRehash(key, 0, 1, (byte) 1);
        assertTrue(index >= 0);
        assertTrue(charHash.contains(key));
    }
}