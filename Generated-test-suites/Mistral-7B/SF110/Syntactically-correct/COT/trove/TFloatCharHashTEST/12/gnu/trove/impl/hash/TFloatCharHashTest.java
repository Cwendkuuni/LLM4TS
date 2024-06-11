package com.example.junit4.test; // replace with your test package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testTFloatCharHashWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.get(0.0f));
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(7, hash.size());
        assertNull(hash.get(0.0f));
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityLoadFactorAndKeys() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
        assertEquals(7, hash.size());
        assertNull(hash.get(1.0f));
        assertEquals('A', hash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTFloatCharHashWithNegativeInitialCapacity() {
        new TFloatCharHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTFloatCharHashWithNegativeLoadFactor() {
        new TFloatCharHash(5, -0.5f);
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.size());
        assertArrayEquals(new float[5], hash._set, 0.01f);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'A');
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        char[] result = new char[3];
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                int index = hash.indexOf(key);
                result[hash.size() - index - 1] = hash.get(key);
                return true;
            }
        };

        hash.forEach(procedure);

        assertArrayEquals(new char[]{'C', 'B', 'A'}, result);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        assertEquals('C', hash.remove(3.0f));
        assertEquals(2, hash.size());
        assertNull(hash.get(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        int index = hash.index(1.0f);
        assertEquals(1, index);
        index = hash.index(4.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        float key = 4.0f;
        int index = hash.insertKey(key);
        assertEquals(4, hash.size());
        assertEquals(key, hash.get(key), 0.01f);
        assertEquals(index, hash.indexOf(key));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyWithDuplicateKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        hash.insertKey(1.0f);
    }

    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        ByteArrayOutputStream bytes = new ByteArrayOutputStream(100);
        ObjectOutputStream output = new ObjectOutputStream(bytes);
        hash.writeExternal(output);

        TFloatCharHash hash2 = new TFloatCharHash(3);
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        hash2.readExternal(input);

        assertEquals(hash.size(), hash2.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.get(i), hash2.get(i), 0.01f);
        }
    }

}