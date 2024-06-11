package com.example.junit.test; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    private TShortHash hash;
    private short[] testData = new short[10];
    private Random random = new Random();

    @Before
    public void setUp() {
        hash = new TShortHash(10);
        for (int i = 0; i < 10; i++) {
            testData[i] = (short) (random.nextInt(Short.MAX_VALUE) & Short.MAX_VALUE);
        }
    }

    @Test
    public void testConstructors() {
        TShortHash hash1 = new TShortHash();
        TShortHash hash2 = new TShortHash(5);
        TShortHash hash3 = new TShortHash(5, 0.75f);
        TShortHash hash4 = new TShortHash(5, 0.75f, (short) 1);

        assertEquals(1, hash1.size());
        assertEquals(5, hash2.size());
        assertEquals(5, hash3.size());
        assertEquals(5, hash4.size());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());

        hash = new TShortHash(10, (short) 5);
        assertEquals(5, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testContains() {
        for (short data : testData) {
            assertTrue(hash.contains(data));
            hash.put(data);
        }

        for (short data : testData) {
            assertTrue(hash.contains(data));
        }

        assertFalse(hash.contains((short) (testData[0] ^ 0x1)));
    }

    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            private int count = 0;

            @Override
            public boolean execute(short shortValue) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);
        assertEquals(10, procedure.getExecutedCount());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        for (short data : testData) {
            int index = hash.indexOf(data);
            assertTrue(index >= 0 || index == -1);
            if (index >= 0) {
                assertEquals(data, hash.get(index));
            }
        }

        assertNull(hash.get((short) (testData[0] ^ 0x1)));
    }

    @Test
    public void testInsertKey() {
        for (short data : testData) {
            int index = hash.put(data);
            assertEquals(data, hash.get(index));
        }

        assertNull(hash.put((short) (testData[0] ^ 0x1)));
    }

    @Test
    public void testInsertKeyAt() {
        for (short data : testData) {
            hash.put(data);
        }

        short newData = (short) (testData[0] ^ 0x1);
        hash.insertKeyAt(0, newData);
        assertEquals(newData, hash.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCapacityExceeded() {
        TShortHash hash = new TShortHash(2);
        for (short data : testData) {
            hash.put(data);
        }
    }

    @Test
    public void testLoadFactorExceeded() {
        TShortHash hash = new TShortHash(5, 0.8f);
        for (short data : testData) {
            hash.put(data);
        }
        assertEquals(10, hash.size());
    }
}