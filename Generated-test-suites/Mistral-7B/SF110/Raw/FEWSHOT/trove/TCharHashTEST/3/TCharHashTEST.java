package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import static gnu.trove.hash.HashFunctions.*;

import java.util.Random;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashCreation() {
        TCharHash tCharHash = new TCharHash();
        TCharHash tCharHashWithInitialCapacity = new TCharHash(10);
        TCharHash tCharHashWithInitialCapacityAndLoadFactor = new TCharHash(10, 0.75f);
        TCharHash tCharHashWithInitialCapacityLoadFactorAndNoEntryValue = new TCharHash(10, 0.75f, 'N');

        assertEquals(10, tCharHashWithInitialCapacity.capacity());
        assertEquals(0.75f, tCharHashWithInitialCapacityAndLoadFactor.loadFactor(), 0.01f);
        assertEquals('N', tCharHashWithInitialCapacityLoadFactorAndNoEntryValue.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        char key = 'A';

        assertFalse(tCharHash.contains(key));

        tCharHash.put(key);
        assertTrue(tCharHash.contains(key));

        tCharHash.remove(key);
        assertFalse(tCharHash.contains(key));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();

        Character[] expectedValues = new Character[]{'A', 'B', 'C', 'D', 'E'};
        Random random = new Random();

        for (char key : expectedValues) {
            tCharHash.put(key);
        }

        TCharProcedure procedure = new TCharProcedure() {
            private int count = 0;

            @Override
            public boolean execute(char character) {
                assertEquals(expectedValues[count++], character);
                return true;
            }
        };

        assertTrue(tCharHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        char key = 'A';

        tCharHash.put(key);
        int initialSize = tCharHash.size();
        tCharHash.removeAt(tCharHash.index(key));
        assertEquals(initialSize - 1, tCharHash.size());
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        char key = 'A';

        tCharHash.put(key);
        int index = tCharHash.index(key);
        assertEquals(index, tCharHash.index(key));
        assertEquals(-1, tCharHash.index('Z'));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        char key = 'A';

        int initialSize = tCharHash.size();
        tCharHash.put(key);
        assertEquals(initialSize + 1, tCharHash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWhenNoFreeOrRemovedSlotsAvailable() {
        TCharHash tCharHash = new TCharHash();
        char key = 'A';

        for (int i = 0; i < tCharHash.capacity(); i++) {
            tCharHash.put(key);
        }
        tCharHash.put(key);
    }
}