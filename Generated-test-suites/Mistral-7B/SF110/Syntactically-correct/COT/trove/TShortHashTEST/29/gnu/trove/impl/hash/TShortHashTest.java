import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testTShortHashConstruction() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);
    }

    @Test
    public void testTShortHashInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(8, hash.size());
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorAndNoEntryValue() {
        short noEntryValue = 10;
        TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(8, hash.size());
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        short noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(5);
        assertEquals(5, hash.capacity());
        assertNull(hash._set);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        assertTrue(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                assertEquals((short) 1, value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        int index1 = hash.index((short) 1);
        int index2 = hash.index((short) 2);
        int index3 = hash.index((short) 3);
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, index3);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);
        int index1 = hash.indexRehashed((short) 1, 0, 1, (byte) 1);
        int index2 = hash.indexRehashed((short) 2, 0, 1, (byte) 1);
        int index3 = hash.indexRehashed((short) 1, 0, 1, (byte) 0);
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, index3);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        int index = hash.insertKey((short) 1);
        assertEquals(1, index);
        assertEquals(1, hash.size());
        assertEquals((short) 1, hash.get(0));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        int index = hash.insertKey((short) 4);
        assertEquals(-2, index);
        assertEquals(3, hash.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyNegativeCapacity() {
        new TShortHash(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyNegativeLoadFactor() {
        new TShortHash(5, -0.1f);
    }

    @Test(expected = NullPointerException.class)
    public void testInsertKeyNullNoEntryValue() {
        new TShortHash(5, null);
    }
}