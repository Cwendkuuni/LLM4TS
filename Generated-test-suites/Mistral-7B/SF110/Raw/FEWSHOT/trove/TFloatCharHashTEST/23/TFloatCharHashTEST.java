package org.apache.commons.cli; // This package is different from the given example, please update it with the correct package name for TFloatCharHash

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import java.io.*;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertNull(hash.contains(Float.NaN));
    }

    @Test
    public void testSetUp() {
        int capacity = 20;
        TFloatCharHash hash = new TFloatCharHash(capacity);
        assertNotNull(hash._set);
        assertEquals(capacity, hash.size());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        final char[] expected = new char[]{'a', 'b'};
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                int index = findIndexOf(expected, getCharacterForValue(hash, value));
                assertEquals(value, hash.get(index));
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertEquals('b', hash.get(0));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        assertEquals(2, hash.size());
        assertEquals(Float.NaN, hash.insertKey(3.0f));
        assertEquals(3, hash.size());
        assertEquals('b', hash.get(1));
        assertEquals('a', hash.get(0));
        assertEquals('a', hash.get(2));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInput = new ObjectInputStream(inputStream);
        TFloatCharHash hashCopy = (TFloatCharHash) objectInput.readObject();
        objectInput.close();
        assertEquals(2, hashCopy.size());
        assertEquals('a', hashCopy.get(0));
        assertEquals('b', hashCopy.get(1));
    }

    private int findIndexOf(char[] array, char value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    private char getCharacterForValue(TFloatCharHash hash, float value) {
        return hash.get(hash.index(value));
    }
}