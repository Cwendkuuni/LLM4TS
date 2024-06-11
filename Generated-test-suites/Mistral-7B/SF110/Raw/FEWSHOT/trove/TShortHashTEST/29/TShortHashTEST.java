package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testDefaultConstructor() {
        TShortHash shortHash = new TShortHash();
        short[] expected = new short[0];
        assertArrayEquals(expected, shortHash._set);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TShortHash shortHash = new TShortHash(10);
        short[] expected = new short[10];
        Arrays.fill(expected, TShortHash.Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        assertArrayEquals(expected, shortHash._set);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash shortHash = new TShortHash(10, 0.75f);
        short[] expected = new short[13]; // 10 initial capacity, 1.75 load factor
        Arrays.fill(expected, TShortHash.Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
        assertArrayEquals(expected, shortHash._set);
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash shortHash = new TShortHash(10, 0.75f, (short) 1);
        short[] expected = new short[13]; // 10 initial capacity, 1.75 load factor, initial value = 1
        Arrays.fill(expected, 1);
        assertArrayEquals(expected, shortHash._set);
    }

    @Test
    public void testContains() {
        TShortHash shortHash = new TShortHash(10);
        short[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (short value : values) {
            shortHash.put(value);
            assertTrue(shortHash.contains(value));
        }
        assertFalse(shortHash.contains((short) 11));
    }

    @Test
    public void testForEach() {
        TShortHash shortHash = new TShortHash(10);
        short[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (short value : values) {
            shortHash.put(value);
        }
        TShortProcedure procedure = new TShortProcedure() {
            private int count = 0;

            @Override
            public boolean execute(short value) {
                assertEquals(value, count++);
                return true;
            }
        };
        assertTrue(shortHash.forEach(procedure));
        assertEquals(values.length, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash shortHash = new TShortHash(10);
        short[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (short value : values) {
            shortHash.put(value);
        }
        for (int i = 0; i < values.length; i++) {
            shortHash.removeAt(i);
            assertEquals(values.length - i - 1, shortHash.size());
            short expected = values[i + 1];
            assertNull(shortHash.get(values[i]));
            assertEquals(expected, shortHash.get(values[i + 1]));
        }
        assertEquals(0, shortHash.size());
    }

    @Test
    public void testIndex() {
        TShortHash shortHash = new TShortHash(10);
        short[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (short value : values) {
            int index = shortHash.index(value);
            assertEquals(value, shortHash.get(index));
        }
        assertNull(shortHash.get(shortHash.index(11)));
    }
}