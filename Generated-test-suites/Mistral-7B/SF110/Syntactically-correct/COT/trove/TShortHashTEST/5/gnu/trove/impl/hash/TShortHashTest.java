import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testTShortHashConstructor() {
        TShortHash shortHash = new TShortHash();
        assertEquals(0, shortHash.getNoEntryValue());
        shortHash = new TShortHash(10);
        assertEquals(10, shortHash.size());
        shortHash = new TShortHash(10, 0.75f);
        assertEquals(10, shortHash.size());
        shortHash = new TShortHash(10, 0.75f, (short) 1);
        assertEquals(10, shortHash.size());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash shortHash = new TShortHash();
        assertEquals(0, shortHash.getNoEntryValue());
        shortHash = new TShortHash(1, 0.5f, (short) 1);
        assertEquals(1, shortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash shortHash = new TShortHash(10);
        assertEquals(10, shortHash.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsNegativeValue() {
        TShortHash shortHash = new TShortHash(10);
        shortHash.contains((short) -1);
    }

    @Test
    public void testContainsValuePresent() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        assertTrue(shortHash.contains((short) 1));
    }

    @Test
    public void testContainsValueAbsent() {
        TShortHash shortHash = new TShortHash(3);
        assertFalse(shortHash.contains((short) 2));
    }

    @Test
    public void testForEach() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        shortHash.insertKey((short) 2);
        TShortProcedure procedure = new TShortProcedure() {
            private int count = 0;

            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        };
        shortHash.forEach(procedure);
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAtIndex() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        shortHash.insertKey((short) 2);
        shortHash.removeAt(0);
        assertFalse(shortHash.contains((short) 1));
        assertTrue(shortHash.contains((short) 2));
    }

    @Test
    public void testIndex() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        shortHash.insertKey((short) 2);
        assertEquals(0, shortHash.index((short) 1));
        assertEquals(-1, shortHash.index((short) 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexNegativeValue() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.index((short) -1);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        shortHash.insertKey((short) 2);
        shortHash.insertKey((short) 3);
        assertEquals(-1, shortHash.indexRehashed((short) 4, 0, 1, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        assertTrue(shortHash.contains((short) 1));
        shortHash.insertKey((short) 2);
        assertTrue(shortHash.contains((short) 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyNegativeValue() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) -1);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        shortHash.insertKey((short) 2);
        shortHash.insertKey((short) 3);
        shortHash.insertKey((short) 4);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash shortHash = new TShortHash(3);
        shortHash.insertKey((short) 1);
        shortHash.insertKey((short) 2);
        shortHash.insertKey((short) 3);
        shortHash.insertKey((short) 4);
        shortHash.insertKey((short) 5);
        assertEquals(4, shortHash.size());
        assertEquals(2, shortHash.index((short) 3));
    }
}