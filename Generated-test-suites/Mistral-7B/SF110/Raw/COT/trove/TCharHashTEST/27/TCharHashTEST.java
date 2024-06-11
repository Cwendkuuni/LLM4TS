import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testTCharHashConstruction() {
        TCharHash charHash = new TCharHash();
        TCharHash charHashWithInitialCapacity = new TCharHash(10);
        TCharHash charHashWithInitialCapacityAndLoadFactor = new TCharHash(10, 0.75f);
        TCharHash charHashWithInitialCapacityLoadFactorAndNoEntryValue = new TCharHash(10, 0.75f, 'N');

        assertEquals(10, charHashWithInitialCapacity.capacity());
        assertEquals(0.75f, charHashWithInitialCapacityAndLoadFactor.loadFactor(), 0.01);
        assertEquals('N', charHashWithInitialCapacityLoadFactorAndNoEntryValue.noEntryValue);
    }

    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        char expected = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actual = charHash.getNoEntryValue();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(10);
        int actualCapacity = charHash.setUp(10);

        assertEquals(10, actualCapacity);
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        charHash.put(key, value);
        boolean actualResult = charHash.contains(key);

        assertTrue(actualResult);
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        charHash.put(key, value);
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                if (character == key) {
                    return false;
                }
                return true;
            }
        };

        charHash.forEach(procedure);
        assertTrue(charHash.contains(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAtWithEmptyHash() {
        TCharHash charHash = new TCharHash();
        int index = 0;
        charHash.removeAt(index);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        charHash.put(key, value);
        int index = charHash.indexOf(key);
        charHash.removeAt(index);
        assertFalse(charHash.contains(key));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexWithInvalidIndex() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        charHash.put(key, 'A');
        int index = -1;
        int actualIndex = charHash.index(key);

        assertEquals(index, actualIndex);
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        charHash.put(key, value);
        int actualIndex = charHash.index(key);

        assertEquals(0, actualIndex);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        char key = 'A';
        char value = 'A';
        int actualIndex = charHash.insertKey(key);

        assertEquals(0, actualIndex);
        assertTrue(charHash.contains(key));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHash() {
        TCharHash charHash = new TCharHash(2);
        char key = 'A';
        char value = 'A';
        charHash.put(key, value);

        try {
            charHash.insertKey(key);
        } catch (IllegalStateException e) {
            assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testInsertKeyWithCollision() {
        TCharHash charHash = new TCharHash();
        char key1 = 'A';
        char value1 = 'A';
        char key2 = 'A';
        charHash.put(key1, value1);
        int index1 = charHash.indexOf(key1);
        charHash.insertKey(key2);
        int index2 = charHash.indexOf(key2);

        assertEquals(index1, index2);
        assertTrue(charHash.contains(key1));
        assertTrue(charHash.contains(key2));
    }

}