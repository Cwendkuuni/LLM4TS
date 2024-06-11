package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testTShortHashConstructorWithDefaultValues() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.getNoEntryValue());
        assertNotNull(hash._set);
        assertTrue(hash.consumeFreeSlot);
    }

    @Test
    public void testTShortHashConstructorWithInitialCapacity() {
        TShortHash hash = new TShortHash(10);
        assertEquals(0, hash.getNoEntryValue());
        assertArrayEquals(new short[10], hash._set);
        assertTrue(hash.consumeFreeSlot);
    }

    @Test
    public void testTShortHashConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        assertEquals(0, hash.getNoEntryValue());
        assertArrayEquals(new short[14], hash._set); // 10 * 1.75
        assertTrue(hash.consumeFreeSlot);
    }

    @Test
    public void testTShortHashConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        short noEntryValue = 1;
        TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertArrayEquals(new short[14], hash._set); // 10 * 1.75
        assertFalse(hash.consumeFreeSlot);
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.getNoEntryValue());
        hash.setNoEntryValue(1);
        assertEquals(1, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(10);
        assertEquals(10, hash.size());
        assertArrayEquals(new short[10], hash._set);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 1);
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertFalse(hash.contains((short) 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsWithNullValue() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.contains((short) null);
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        short count = 0;
        boolean result = hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });
        assertTrue(result);
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains((short) 2));
        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtWithInvalidIndex() {
        TShortHash hash = new TShortHash(5);
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        assertEquals(1, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexWithNullValue() {
        TShortHash hash = new TShortHash(3);
        hash.index((short) null);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        int index = hash.insertKey((short) 1);
        assertEquals(4, hash.size());
        assertFalse(hash.contains((short) 1));
        assertEquals(1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyWithExistingValue() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 1);
        hash.insertKey((short) 1);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.insertKey((short) 4);
        assertEquals(5, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashTable() {
        TShortHash hash = new TShortHash(3);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);
        hash.insertKey((short) 6);
    }
}