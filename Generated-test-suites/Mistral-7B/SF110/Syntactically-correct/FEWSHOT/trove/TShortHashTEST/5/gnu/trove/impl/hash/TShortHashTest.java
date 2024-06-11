package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hashMap = new TShortHash(5);
        assertEquals(5, hashMap.size());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hashMap = new TShortHash();
        short noEntryValue = hashMap.getNoEntryValue();
        assertEquals(Short.MIN_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TShortHash hashMap = new TShortHash(5);
        assertEquals(5, hashMap.size());
        byte[] states = hashMap._states;
        short[] set = hashMap._set;
        assertEquals(5, states.length);
        assertEquals(5, set.length);
    }

    @Test
    public void testContains() {
        TShortHash hashMap = new TShortHash(5);
        hashMap.put((short) 1, (short) 1);
        assertTrue(hashMap.contains((short) 1));
        assertFalse(hashMap.contains((short) 2));
    }

    @Test
    public void testForEach() {
        TShortHash hashMap = new TShortHash(3);
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short key) {
                assertEquals(key, (short) 2);
                return true;
            }
        };
        assertTrue(hashMap.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hashMap = new TShortHash(3);
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);
        short removed = hashMap.removeAt(1);
        assertEquals((short) 2, removed);
        assertEquals(2, hashMap.size());
        assertFalse(hashMap.contains((short) 2));
    }

    @Test
    public void testIndex() {
        TShortHash hashMap = new TShortHash(3);
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);
        int index = hashMap.index((short) 2);
        assertEquals(1, index);
        index = hashMap.index((short) 4);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TShortHash hashMap = new TShortHash(3);
        short key = (short) 1;
        short value = (short) 1;
        int index = hashMap.insertKey(key);
        assertEquals(-1, index);
        hashMap.put(key, value);
        index = hashMap.insertKey(key);
        assertEquals(-index - 1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TShortHash hashMap = new TShortHash(3);
        for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
            hashMap.put(i, i);
        }
        short key = (short) 1;
        hashMap.insertKey(key);
    }
}