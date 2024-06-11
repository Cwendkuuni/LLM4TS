import org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testDefaultConstructor() {
        TShortHash hash = new TShortHash();
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        Assert.assertArrayEquals(new short[0], hash._set);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TShortHash(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TShortHash(10, -0.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNoEntryValue() {
        new TShortHash(10, 0.5f, (short) -1);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsWithNullValue() {
        TShortHash hash = new TShortHash(5);
        hash.contains((short) null);
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;

            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, count);
    }

    @Test(expected = NullPointerException.class)
    public void testForEachWithNullProcedure() {
        TShortHash hash = new TShortHash(5);
        assertFalse(hash.forEach(null));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.removeAt(0);
        assertFalse(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtWithInvalidIndex() {
        TShortHash hash = new TShortHash(5);
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        assertEquals(0, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 3));
    }

    @Test(expected = NullPointerException.class)
    public void testIndexWithNullValue() {
        TShortHash hash = new TShortHash(5);
        hash.index((short) null);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        assertEquals(2, hash._set.length);
        assertEquals((short) 1, hash._set[0]);
        assertEquals((short) 2, hash._set[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyWithInvalidValue() {
        TShortHash hash = new TShortHash(5);
        hash.insertKey((short) 1);
        hash.insertKey((short) 255);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashTable() {
        TShortHash hash = new TShortHash(5);
        for (short i = 0; i < 5; i++) {
            hash.insertKey((short) i);
        }
        try {
            hash.insertKey((short) 5);
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!", e.getMessage());
        }
    }
}