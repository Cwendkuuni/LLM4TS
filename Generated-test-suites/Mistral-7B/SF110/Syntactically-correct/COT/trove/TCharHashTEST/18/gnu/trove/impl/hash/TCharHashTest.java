import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testConstructorWithDefaults() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        assertTrue(defaultValue != '\0');
        assertEquals(defaultValue, charHash.get(defaultValue));
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        assertTrue(defaultValue != '\0');
        assertEquals(defaultValue, charHash.get(defaultValue));
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        assertTrue(defaultValue != '\0');
        assertEquals(defaultValue, charHash.get(defaultValue));
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(0, charHash.size());
        assertEquals(noEntryValue, charHash.getNoEntryValue());
        assertEquals(noEntryValue, charHash.get(noEntryValue));
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertFalse(charHash.contains('D'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsWithNull() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        charHash.contains((char) 0);
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char value) {
                count++;
                return true;
            }
        };
        assertTrue(charHash.forEach(procedure));
        assertEquals(3, procedure.getExecuteCount());
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        charHash.removeAt(1);
        assertEquals(2, charHash.size());
        assertFalse(charHash.contains('B'));
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('C'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtWithInvalidIndex() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        charHash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        assertEquals(1, charHash.index('B'));
        assertEquals(-1, charHash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        char insertValue = 'X';
        int index = charHash.insertKey(insertValue);
        assertEquals(4, charHash.size());
        assertEquals(insertValue, charHash.get(insertValue));
        assertEquals(index, charHash.index(insertValue));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyWithNull() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.put('C', 'C');
        charHash.insertKey((char) 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashTable() {
        TCharHash charHash = new TCharHash(2);
        charHash.put('A', 'A');
        charHash.put('B', 'B');
        charHash.insertKey('C');
    }
}