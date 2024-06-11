package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashConstruction() {
        TCharHash charHash = new TCharHash();
        TCharHash charHashWithInitialCapacity = new TCharHash(10);
        TCharHash charHashWithInitialCapacityAndLoadFactor = new TCharHash(10, 0.75f);
        TCharHash charHashWithInitialCapacityLoadFactorAndNoEntryValue = new TCharHash(10, 0.75f, 'X');

        assertEquals(10, charHashWithInitialCapacity.size());
        assertEquals(10, charHashWithInitialCapacityAndLoadFactor.size());
        assertEquals(10, charHashWithInitialCapacityLoadFactorAndNoEntryValue.size());
        assertEquals('X', charHashWithInitialCapacityLoadFactorAndNoEntryValue.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash(5);
        assertEquals(5, charHash.size());
        charHash.setUp(10);
        assertEquals(10, charHash.size());
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);

        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertTrue(charHash.contains('D'));
        assertTrue(charHash.contains('E'));
        assertFalse(charHash.contains('F'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);

        int count = 0;
        charHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char key) {
                assertEquals('A', key);
                assertEquals(1, charHash.get(key));
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);

        charHash.removeAt(3);

        assertFalse(charHash.contains('D'));
        assertTrue(charHash.contains('A'));
        assertTrue(charHash.contains('B'));
        assertTrue(charHash.contains('C'));
        assertTrue(charHash.contains('E'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);

        int indexOfA = charHash.index('A');
        int indexOfB = charHash.index('B');
        int indexOfC = charHash.index('C');
        int indexOfD = charHash.index('D');
        int indexOfE = charHash.index('E');
        int indexOfF = charHash.index('F');

        assertEquals(0, indexOfA);
        assertEquals(1, indexOfB);
        assertEquals(2, indexOfC);
        assertEquals(3, indexOfD);
        assertEquals(4, indexOfE);
        assertEquals(-1, indexOfF);
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);

        char newKey = 'F';
        int index = charHash.insertKey(newKey);

        assertTrue(charHash.contains(newKey));
        assertEquals(6, charHash.size());
        assertEquals(5, charHash.index(newKey));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TCharHash charHash = new TCharHash(5);
        charHash.put('A', 1);
        charHash.put('B', 2);
        charHash.put('C', 3);
        charHash.put('D', 4);
        charHash.put('E', 5);
        charHash.put('F', 6); // Should throw an IllegalStateException
    }
}