package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testTShortHashZeroArgsConstructor() {
        TShortHash hashMap = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hashMap.getNoEntryValue());
        assertEquals(0, hashMap.size());
    }

    @Test
    public void testTShortHashInitialCapacityConstructor() {
        TShortHash hashMap = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hashMap.getNoEntryValue());
        assertEquals(0, hashMap.size());
        assertEquals(10, hashMap.capacity());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorConstructor() {
        TShortHash hashMap = new TShortHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hashMap.getNoEntryValue());
        assertEquals(0, hashMap.size());
        assertEquals(10, hashMap.capacity());
        assertEquals(0.75f, hashMap.loadFactor(), 0.001f);
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        short noEntryValue = 128;
        TShortHash hashMap = new TShortHash(10, 0.75f, noEntryValue);
        assertEquals(noEntryValue, hashMap.getNoEntryValue());
        assertEquals(0, hashMap.size());
        assertEquals(10, hashMap.capacity());
        assertEquals(0.75f, hashMap.loadFactor(), 0.001f);
        assertArrayEquals(new short[]{noEntryValue}, hashMap._set);
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hashMap = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hashMap.getNoEntryValue());
        short noEntryValue = 128;
        hashMap = new TShortHash(10, 0.75f, noEntryValue);
        assertEquals(noEntryValue, hashMap.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TShortHash(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TShortHash(10, -1.5f);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorArg() {
        new TShortHash(null);
    }

    @Test
    public void testContains() {
        TShortHash hashMap = new TShortHash();
        hashMap.put((short) 1, (short) 1);
        assertTrue(hashMap.contains((short) 1));
        assertFalse(hashMap.contains((short) 2));
    }

    @Test
    public void testForEach() {
        TShortHash hashMap = new TShortHash();
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        int count = 0;
        hashMap.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals((short) (count + 1), value);
                count++;
                return true;
            }
        });
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hashMap = new TShortHash();
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        short removedValue = hashMap.removeAt(1);
        assertEquals((short) 2, removedValue);
        assertEquals(2, hashMap.size());
        assertFalse(hashMap.contains((short) 2));
        assertTrue(hashMap.contains((short) 1));
    }

    @Test
    public void testIndex() {
        TShortHash hashMap = new TShortHash();
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        int index = hashMap.index((short) 1);
        assertEquals(0, index);
        index = hashMap.index((short) 2);
        assertEquals(1, index);
        index = hashMap.index((short) 3);
        assertEquals(2, index);
        index = hashMap.index((short) 4);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TShortHash hashMap = new TShortHash();
        int index = hashMap.insertKey((short) 1);
        assertEquals(0, index);
        assertEquals(1, hashMap.size());
        assertEquals((short) 1, hashMap.get(0));
    }

    @Test
    public void testInsertKeyCollision() {
        TShortHash hashMap = new TShortHash();
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 1, (short) 2);
        int index = hashMap.insertKey((short) 1);
        assertEquals(-2, index);
        assertEquals(2, hashMap.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hashMap = new TShortHash(5);
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);
        hashMap.put((short) 4, (short) 4);
        hashMap.put((short) 5, (short) 5);
        hashMap.put((short) 6, (short) 6);

        int index = hashMap.insertKey((short) 7);
        assertEquals(1, index);
        assertEquals(7, hashMap.size());
        assertEquals((short) 7, hashMap.get(1));
    }

    @Test
    public void testInsertKeyRehashCollision() {
        TShortHash hashMap = new TShortHash(5);
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);
        hashMap.put((short) 4, (short) 4);
        hashMap.put((short) 5, (short) 5);
        hashMap.put((short) 6, (short) 6);
        hashMap.put((short) 7, (short) 7);
        hashMap.put((short) 7, (short) 8);

        int index = hashMap.insertKey((short) 7);
        assertEquals(-2, index);
        assertEquals(8, hashMap.size());
    }
}