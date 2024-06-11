import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        short noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }

    @Test
    public void testInitialCapacityConstructor() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testInitialCapacityLoadFactorConstructor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.loadFactor(), 0.01f);
    }

    @Test
    public void testInitialCapacityLoadFactorNoEntryValueConstructor() {
        short noEntryValue = 1;
        TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
        assertEquals(10, hash.capacity());
        assertEquals(0.75f, hash.loadFactor(), 0.01f);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacityConstructor() {
        new TShortHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactorConstructor() {
        new TShortHash(10, -0.5f);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(5);
        hash.insert(1);
        assertTrue(hash.contains(1));
        assertFalse(hash.contains(2));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(3);
        hash.insert(1);
        hash.insert(2);
        hash.insert(3);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short key) {
                return key == 2;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.insert(1);
        hash.insert(2);
        hash.insert(1);
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains(1));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(5);
        hash.insert(1);
        hash.insert(2);
        hash.insert(1);
        assertEquals(1, hash.index(1));
        assertEquals(-1, hash.index(3));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        short value = 1;
        hash.insertKey(value);
        assertEquals(1, hash.size());
        assertEquals(value, hash.get(value));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(5);
        short value = Short.MAX_VALUE;
        hash.insertKey(value);
        assertEquals(value, hash.get(value));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TShortHash hash = new TShortHash(5);
        for (short i = 0; i <= Short.MAX_VALUE; i++) {
            hash.insertKey(i);
        }
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash(5);
        hash.insert(1);
        hash.insert(2);
        hash.insert(1);
        assertEquals(1, hash.indexRehashed(1, 1, 1, 1));
        assertEquals(-1, hash.indexRehashed(3, 1, 1, 1));
    }
}