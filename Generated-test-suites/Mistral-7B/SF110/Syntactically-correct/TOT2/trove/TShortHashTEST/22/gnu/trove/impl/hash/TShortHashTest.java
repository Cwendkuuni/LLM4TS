package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.impl.*;
import gnu.trove.procedure.*;
import gnu.trove.hash.*;

public class TShortHashTest {
    @Test
    public void testTShortHashDefaultConstructor() {
        TShortHash hash = new TShortHash();
        short expectedDefaultValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        short[] actualSet = hash._set;

        //Checking if the no_entry_value is set correctly
        assertEquals(expectedDefaultValue, actualSet[0]);

        //Checking if the size of the hash is 0
        assertEquals(0, hash.size());
    }

    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash hash = new TShortHash(5);
        short[] expectedSet = new short[5];
        Arrays.fill(expectedSet, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        short expectedNoEntryValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;

        //Checking if the no_entry_value is set correctly
        assertEquals(expectedNoEntryValue, hash.getNoEntryValue());

        //Checking if the _set has the correct size
        assertArrayEquals(expectedSet, hash._set);

        //Checking if the size of the hash is 0
        assertEquals(0, hash.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTShortHashWithMaximumCapacity() {
        TShortHash hash = new TShortHash(Integer.MAX_VALUE);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(2);
        hash.put((short) 1);
        hash.put((short) 2);

        boolean expectedResult1 = hash.contains((short) 1);
        boolean expectedResult2 = hash.contains((short) 2);
        boolean expectedResult3 = hash.contains((short) 3);

        assertTrue(expectedResult1);
        assertTrue(expectedResult2);
        assertFalse(expectedResult3);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(2);

        int expectedIndex1 = hash.insertKey((short) 1);
        int expectedIndex2 = hash.insertKey((short) 2);

        assertEquals(0, expectedIndex1);
        assertEquals(1, expectedIndex2);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(2);
        hash.put((short) 1);
        hash.put((short) 1); //duplicate key

        int expectedIndex = hash.insertKey((short) 3); //should find empty slot

        assertEquals(-1, expectedIndex);
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(2);
        hash.put((short) 1);
        hash.put((short) 2);

        short expectedResult1 = Short.MIN_VALUE;
        short expectedResult2 = Short.MAX_VALUE;

        boolean result = hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short key) {
                if (key == (short) 1) {
                    expectedResult1 = key;
                    return false;
                }
                if (key == (short) 2) {
                    expectedResult2 = key;
                    return false;
                }
                return true;
            }
        });

        assertTrue(result);
        assertEquals((short) 1, expectedResult1);
        assertEquals((short) 2, expectedResult2);
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash(2);
        short expectedNoEntryValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;

        short actualNoEntryValue = hash.getNoEntryValue();

        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        TShortHash hash = new TShortHash(-1);
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(5);

        int expectedCapacity = hash.capacity();
        short[] expectedSet = new short[expectedCapacity];
        Arrays.fill(expectedSet, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);

        short[] actualSet = hash._set;

        assertArrayEquals(expectedSet, actualSet);
    }
}