package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmpty() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.5f);
        assertEquals(10, charHash.size());
        assertNull(charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.5f, noEntryValue);
        assertEquals(10, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key, key);
        assertTrue(charHash.contains(key));
        charHash.clear();
        assertFalse(charHash.contains(key));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';
        charHash.put(key1, key1);
        charHash.put(key2, key2);
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char element) {
                if (element == key1) {
                    assertEquals(key1, element);
                    return false;
                }
                if (element == key2) {
                    assertEquals(key2, element);
                    return false;
                }
                fail("Unexpected element: " + element);
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';
        charHash.put(key1, key1);
        charHash.put(key2, key2);
        int index1 = charHash.indexOf(key1);
        int index2 = charHash.indexOf(key2);
        charHash.removeAt(index1);
        charHash.removeAt(index2);
        assertEquals(0, charHash.size());
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';
        charHash.put(key1, key1);
        charHash.put(key2, key2);
        int index1 = charHash.indexOf(key1);
        int index2 = charHash.indexOf(key2);
        assertEquals(index1, charHash.index(key1));
        assertEquals(index2, charHash.index(key2));
        assertEquals(-1, charHash.index('C'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';
        charHash.put(key1, key1);
        charHash.put(key2, key2);
        int index1 = charHash.indexOf(key1);
        int index2 = charHash.indexOf(key2);
        charHash.put('C', 'C');
        assertEquals(index1, charHash.indexRehashed(key1, index1, HashFunctions.hash(key1), (byte) 1));
        assertEquals(index2, charHash.indexRehashed(key2, index2, HashFunctions.hash(key2), (byte) 1));
        assertEquals(-1, charHash.indexRehashed('D', index1, HashFunctions.hash('D'), (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';
        charHash.put(key1, key1);
        charHash.put(key2, key2);
        int index1 = charHash.indexOf(key1);
        int index2 = charHash.indexOf(key2);
        charHash.put('C', 'C');
        assertEquals(index1, charHash.insertKey(key1));
        assertEquals(index2 + 1, charHash.insertKey(key2));
        assertEquals(-1, charHash.insertKey('D'));
        assertEquals('C', charHash._set[index2]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char key2 = 'B';
        char key3 = 'C';
        charHash.put(key1, key1);
        charHash.put(key2, key2);
        int index1 = charHash.indexOf(key1);
        int index2 = charHash.indexOf(key2);
        charHash.put('D', 'D');
        charHash.put('E', 'E');
        charHash.put('F', 'F');
        charHash.put('G', 'G');
        charHash.put('H', 'H');
        assertEquals(index1, charHash.insertKeyRehash(key1, index1, HashFunctions.hash(key1), (byte) 1));
        assertEquals(index2 + 1, charHash.insertKeyRehash(key2, index2, HashFunctions.hash(key2), (byte) 1));
        assertEquals(-1, charHash.insertKeyRehash('I', index1, HashFunctions.hash('I'), (byte) 1));
    }
}