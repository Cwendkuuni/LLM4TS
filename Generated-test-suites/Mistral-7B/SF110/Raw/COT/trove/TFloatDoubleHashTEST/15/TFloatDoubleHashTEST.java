package com.example.junit.test; // replace with your test package name

import static org.junit.Assert.*;
import static gnu.trove.HashFunctions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.junit.Test;

import gnu.trove.TFloatProcedure;
import gnu.trove.impl.hash.TFloatDoubleHash;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertEquals(0, hash.size());

        TFloatDoubleHash hash1 = new TFloatDoubleHash(10);
        assertEquals(10, hash1.size());
        assertNull(hash1.getNoEntryKey());
        assertEquals(0.0, hash1.getNoEntryValue(), 0.0);

        TFloatDoubleHash hash2 = new TFloatDoubleHash(10, 0.5f);
        assertEquals(10, hash2.size());
        assertNull(hash2.getNoEntryKey());
        assertEquals(0.0, hash2.getNoEntryValue(), 0.0);

        TFloatDoubleHash hash3 = new TFloatDoubleHash(10, 0.5f, 0.1f, 0.0);
        assertEquals(10, hash3.size());
        assertEquals(0.1f, hash3.getNoEntryKey());
        assertEquals(0.0, hash3.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);

        int index = hash.index(1.0f);
        hash.removeAt(index);
        assertFalse(hash.contains(1.0f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsException() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(1.0f);

        int index1 = hash.index(1.0f);
        assertEquals(0, index1);

        int index2 = hash.index(2.0f);
        assertEquals(1, index2);

        int index3 = hash.index(3.0f);
        assertEquals(-1, index3);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);

        int index1 = hash.index(1.0f);
        assertEquals(0, index1);

        int index2 = hash.index(3.0f);
        int expectedIndex = HashFunctions.hash(3.0f) % 10;
        assertEquals(expectedIndex, index2);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index1 = hash.index(1.0f);
        assertEquals(0, index1);

        int index2 = hash.index(3.0f);
        int expectedIndex = HashFunctions.hash(3.0f) % 10;
        assertEquals(expectedIndex, index2);

        assertFalse(hash.contains(4.0f));
        hash.insertKey(4.0f);
        assertTrue(hash.contains(4.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
    }

    @Test
    public void testWriteRead() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(outputStream);
        hash.writeExternal(output);
        output.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput input = new ObjectInput(inputStream);

        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(input);

        assertEquals(2, hash2.size());
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));

        input.close();
    }
}