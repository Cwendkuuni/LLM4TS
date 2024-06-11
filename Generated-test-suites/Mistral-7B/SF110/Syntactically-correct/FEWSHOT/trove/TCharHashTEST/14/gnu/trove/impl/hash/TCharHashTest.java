package gnu.trove.impl.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash charHash;

    @Before
    public void setUp() {
        charHash = new TCharHash(11);
    }

    @Test
    public void testInitialization() {
        assertEquals(11, charHash.size());
        assertEquals(11, charHash._states.length);
        assertEquals(11, charHash._set.length);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\u0000', charHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        charHash.put('a');
        assertTrue(charHash.contains('a'));
        assertFalse(charHash.contains('b'));
    }

    @Test
    public void testContainsNullValue() {
        charHash.put('a');
        assertFalse(charHash.contains(null));
    }

    @Test
    public void testForEach() {
        charHash.put('a');
        charHash.put('b');

        int count = 0;
        charHash.forEach(val -> {
            assertEquals('a', val);
            count++;
        });

        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        charHash.put('a');
        charHash.put('b');
        charHash.removeAt(0);

        assertFalse(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
    }

    @Test
    public void testIndex() {
        charHash.put('a');
        charHash.put('b');

        assertEquals(0, charHash.index('a'));
        assertEquals(1, charHash.index('b'));
        assertNull(charHash.index('c'));
    }

    @Test
    public void testInsertKey() {
        charHash.put('a');
        charHash.put('b');

        assertEquals(-2, charHash.insertKey('a'));
        assertEquals(2, charHash.size());
        assertEquals('b', charHash._set[1]);

        assertEquals(1, charHash.insertKey('a'));
        assertEquals(3, charHash.size());
        assertEquals('a', charHash._set[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        charHash.put('a');
        // Adding 10 more elements to make the hash table full
        for (int i = 0; i < 10; i++) {
            charHash.put((char) ('a' + i));
        }

        charHash.put('x');
    }

    @Test
    public void testHashFunctions() {
        assertEquals(1, HashFunctions.hash('a'));
        assertEquals(31, HashFunctions.hash('z'));
        assertEquals(1601, HashFunctions.hash('\u0304'));
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        charHash.put('a');
        charHash.put('b');
        charHash.put('c');

        int indexA = charHash.index('a');
        int indexB = charHash.index('b');
        int indexC = charHash.index('c');

        assertTrue(indexA >= 0 && indexA < charHash._set.length);
        assertTrue(indexB >= 0 && indexB < charHash._set.length);
        assertTrue(indexC >= 0 && indexC < charHash._set.length);

        assertEquals('a', charHash._set[indexA]);
        assertEquals('b', charHash._set[indexB]);
        assertEquals('c', charHash._set[indexC]);
    }

    @Test
    public void testTCharHashWithInitialCapacityAndCustomNoEntryValue() {
        char customNoEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, customNoEntryValue);

        charHash.put('a');
        charHash.put('b');

        assertEquals(customNoEntryValue, charHash.getNoEntryValue());
        assertEquals('a', charHash._set[0]);
        assertEquals('b', charHash._set[1]);
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndCustomNoEntryValue() {
        char customNoEntryValue = 'X';
        TCharHash charHash = new TCharHash(10, 0.75f, customNoEntryValue);

        charHash.put('a');
        charHash.put('b');

        assertEquals(customNoEntryValue, charHash.getNoEntryValue());
        assertEquals('a', charHash._set[0]);
        assertEquals('b', charHash._set[1]);
    }
}