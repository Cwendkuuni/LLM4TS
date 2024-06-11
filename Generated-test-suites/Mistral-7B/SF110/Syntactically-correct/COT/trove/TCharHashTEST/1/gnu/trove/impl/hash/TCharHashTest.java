import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testTCharHashConstructor() {
        TCharHash charHash = new TCharHash();
        assertNotNull(charHash);
        assertEquals(0, charHash.size());
    }

    @Test
    public void testTCharHashInitialCapacityConstructor() {
        TCharHash charHash = new TCharHash(10);
        assertNotNull(charHash);
        assertEquals(0, charHash.size());
        assertEquals(10, charHash.capacity());
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorConstructor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertNotNull(charHash);
        assertEquals(0, charHash.size());
        assertEquals(10, charHash.capacity());
        assertEquals(0.75f, charHash.loadFactor(), 0.01f);
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertNotNull(charHash);
        assertEquals(0, charHash.size());
        assertEquals(10, charHash.capacity());
        assertEquals(0.75f, charHash.loadFactor(), 0.01f);
        assertArrayEquals(new char[]{noEntryValue}, charHash._set);
    }

    @Test(expected = NullPointerException.class)
    public void testTCharHashNullNoEntryValue() {
        new TCharHash(10, 0.75f, null);
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
        assertEquals(10, charHash.capacity());
        assertArrayEquals(new char[10], charHash._set);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        assertFalse(charHash.contains(value));
        charHash.put(value);
        assertTrue(charHash.contains(value));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        charHash.put(value);
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                assertEquals(value, character);
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForEachNullProcedure() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        charHash.put(value);
        TCharProcedure procedure = null;
        assertFalse(charHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        charHash.put(value);
        assertTrue(charHash.contains(value));
        charHash.removeAt(0);
        assertFalse(charHash.contains(value));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtNegativeIndex() {
        TCharHash charHash = new TCharHash();
        charHash.removeAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexBiggerThanSize() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        charHash.put(value);
        int size = charHash.size();
        charHash.removeAt(size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexNegativeIndex() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        charHash.put(value);
        int index = -1;
        int result = charHash.index(value);
        assertEquals(-1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexNullValue() {
        TCharHash charHash = new TCharHash();
        int index = charHash.index((char) 0);
        assertEquals(-1, index);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexIndexBiggerThanSize() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        charHash.put(value);
        int index = charHash.size();
        int result = charHash.index(value);
        assertEquals(-1, result);
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash(5);
        char value1 = 'A';
        charHash.put(value1);
        char value2 = 'B';
        charHash.put(value2);
        int hash = HashFunctions.hash(value1) & Integer.MAX_VALUE;
        int index = hash % charHash._states.length;
        assertFalse(charHash.contains(value1));
        int rehashedIndex = charHash.indexRehashed(value1, index, hash, (byte) 0);
        assertTrue(charHash.contains(value1));
        assertEquals(1, rehashedIndex);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char value = 'A';
        int index = charHash.insertKey(value);
        assertTrue(charHash.contains(value));
        assertEquals(0, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyNullValue() {
        TCharHash charHash = new TCharHash();
        int index = charHash.insertKey((char) 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFullHashTable() {
        TCharHash charHash = new TCharHash(5);
        for (char i = 'A'; i <= 'Z'; i++) {
            charHash.put(i);
        }
        char value = 'AA';
        int index = charHash.insertKey(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashed() {
        TCharHash charHash = new TCharHash(5);
        for (char i = 'A'; i <= 'Z'; i++) {
            charHash.put(i);
        }
        char value = 'AA';
        int index = charHash.insertKey(value);
    }
}