import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testConstructor() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);
        TShortHash hash2 = new TShortHash(10);
        assertNotNull(hash2);
        TShortHash hash3 = new TShortHash(10, 0.75f);
        assertNotNull(hash3);
        TShortHash hash4 = new TShortHash(10, 0.75f, (short) 1);
        assertNotNull(hash4);
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
        assertEquals(5, hash.setUp(5));
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        assertTrue(hash.contains((short) 1));
        hash.put((short) 2);
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        short count = 0;
        TShortProcedure procedure = (short value) -> {
            count++;
        };
        hash.forEach(procedure);
        assertEquals(2, count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        assertEquals(0, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 3));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        int index1 = hash.insertKey((short) 3);
        assertEquals(-1, index1);
        hash.put((short) 4);
        int index2 = hash.insertKey((short) 3);
        assertEquals(3, index2);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        int index = hash.index((short) 4);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        int index = hash.insertKey((short) 4);
        assertEquals(-1, index);
        hash.put((short) 5);
        int index2 = hash.insertKey((short) 4);
        assertEquals(3, index2);
    }
}