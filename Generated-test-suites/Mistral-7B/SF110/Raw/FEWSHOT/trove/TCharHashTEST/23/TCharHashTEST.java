package gnu.trove.impl.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmpty() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(10, charHash.size());
        assertTrue(charHash.isEmpty());
    }

    @Test
    public void testTCharHashInitialCapacity() {
        char hashValue = 'a';
        TCharHash charHash = new TCharHash(10, hashValue);
        assertEquals(10, charHash.size());
        assertTrue(charHash.isEmpty());
        assertFalse(charHash.contains(hashValue));

        char newValue = 'b';
        int index = charHash.insertKey(newValue);
        assertEquals(1, charHash.size());
        assertTrue(charHash.contains(newValue));
        assertEquals(newValue, charHash.get(index));
        assertEquals(hashValue, charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashLoadFactor() {
        char hashValue = 'a';
        TCharHash charHash = new TCharHash(5, hashValue, 0.75f);
        assertEquals(5, charHash.size());
        assertTrue(charHash.isEmpty());
        assertFalse(charHash.contains(hashValue));

        char newValue = 'b';
        int index = charHash.insertKey(newValue);
        assertEquals(1, charHash.size());
        assertTrue(charHash.contains(newValue));
        assertEquals(newValue, charHash.get(index));
        assertEquals(hashValue, charHash.getNoEntryValue());

        char newValue2 = 'c';
        int index2 = charHash.insertKey(newValue2);
        assertEquals(2, charHash.size());
        assertTrue(charHash.contains(newValue));
        assertTrue(charHash.contains(newValue2));
        assertEquals(newValue, charHash.get(index));
        assertEquals(newValue2, charHash.get(index2));
        assertEquals(hashValue, charHash.getNoEntryValue());

        char newValue3 = 'd';
        int index3 = charHash.insertKey(newValue3);
        assertEquals(4, charHash.size());
        assertTrue(charHash.contains(newValue));
        assertTrue(charHash.contains(newValue2));
        assertTrue(charHash.contains(newValue3));
        assertEquals(newValue, charHash.get(index));
        assertEquals(newValue2, charHash.get(index2));
        assertEquals(newValue3, charHash.get(index3));
        assertEquals(hashValue, charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashCustomNoEntryValue() {
        char noEntryValue = 'x';
        TCharHash charHash = new TCharHash(5, 10, noEntryValue);
        assertEquals(5, charHash.size());
        assertTrue(charHash.isEmpty());
        assertFalse(charHash.contains('a'));

        char key = 'a';
        int index = charHash.insertKey(key);
        assertEquals(1, charHash.size());
        assertTrue(charHash.contains(key));
        assertEquals(key, charHash.get(index));
        assertEquals(noEntryValue, charHash.getNoEntryValue());

        char key2 = 'b';
        int index2 = charHash.insertKey(key2);
        assertEquals(2, charHash.size());
        assertTrue(charHash.contains(key));
        assertTrue(charHash.contains(key2));
        assertEquals(key, charHash.get(index));
        assertEquals(key2, charHash.get(index2));
        assertEquals(noEntryValue, charHash.getNoEntryValue());
    }

    @Test
    public void testTCharHashContains() {
        char hashValue = 'a';
        TCharHash charHash = new TCharHash(5, hashValue);

        char key1 = 'b';
        charHash.insertKey(key1);
        assertTrue(charHash.contains(key1));

        char key2 = 'c';
        charHash.insertKey(key2);
        assertTrue(charHash.contains(key2));

        char key3 = 'd';
        charHash.insertKey(key3);
        assertTrue(charHash.contains(key3));

        char key4 = 'e';
        charHash.insertKey(key4);
        assertTrue(charHash.contains(key4));

        char key5 = 'f';
        charHash.insertKey(key5);
        assertTrue(charHash.contains(key5));

        char key6 = 'g';
        assertFalse(charHash.contains(key6));

        charHash.remove('b');
        assertFalse(charHash.contains('b'));
        assertTrue(charHash.size() == 5);
    }

    @Test
    public void testTCharHashPutGet()
    {
        char hashValue = 'a';
        TCharHash charHash = new TCharHash(5, hashValue);

        char key1 = 'b';
        int index1 = charHash.insertKey(key1);
        assertEquals(key1, charHash.get(index1));

        char key2 = 'c';
        int index2 = charHash.insertKey(key2);
        assertEquals(key2, charHash.get(index2));

        char key3 = 'd';
        int index3 = charHash.insertKey(key3);
        assertEquals(key3, charHash.get(index3));

        char key4 = 'e';
        int index4 = charHash.insertKey(key4);
        assertEquals(key4, charHash.get(index4));

        char key5 = 'f';
        int index5 = charHash.insertKey(key5);
        assertEquals(key5, charHash.get(index5));

        char getKey1 = charHash.get(index1);
        assertEquals(getKey1, key1);

        char getKey2 = charHash.get(index2);
        assertEquals(getKey2, key2);

        char getKey3 = charHash.get(index3);
        assertEquals(getKey3, key3);

        char getKey4 = charHash.get(index4);
        assertEquals(getKey4, key4);

        char getKey5 = charHash.get(index5);
        assertEquals(getKey5, key5);
    }

    @Test
    public void testTCharHashForEach() {
        char hashValue = 'a';
        TCharHash charHash = new TCharHash(5, hashValue);

        char key1 = 'b';
        int index1 = charHash.insertKey(key1);
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key1);
                return true;
            }
        });

        char key2 = 'c';
        int index2 = charHash.insertKey(key2);
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key1);
                return false;
            }
        });
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key2);
                return true;
            }
        });

        char key3 = 'd';
        int index3 = charHash.insertKey(key3);
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key1);
                return false;
            }
        });
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key2);
                return false;
            }
        });
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key3);
                return true;
            }
        });

        charHash.remove('b');
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key2);
                return true;
            }
        });
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key3);
                return true;
            }
        });

        charHash.remove('c');
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key3);
                return true;
            }
        });

        charHash.remove('d');
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertEquals(value, key3);
                return false;
            }
        });

        charHash.remove('e');
        charHash.remove('f');
        charHash.remove('g');
        charHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                assertNull(value);
                return true;
            }
        });
    }

    @Test(expected = IllegalStateException.class)
    public void testTCharHashInsertKeyRehashFull() {
        char hashValue = 'a';
        TCharHash charHash = new TCharHash(5, hashValue);

        for (char key = 'a'; key < 'z'; key++) {
            charHash.insertKey(key);
        }
    }
}