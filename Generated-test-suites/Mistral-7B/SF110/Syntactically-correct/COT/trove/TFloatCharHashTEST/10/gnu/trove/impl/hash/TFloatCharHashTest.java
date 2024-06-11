package com.example.junitTests; // Replace with your package name if necessary

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatCharHashTest {

    @org.junit.Test
    public void testTFloatCharHashConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertNotNull(hash);
    }

    @org.junit.Test
    public void testTFloatCharHashWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @org.junit.Test
    public void testTFloatCharHashWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
    }

    @org.junit.Test
    public void testTFloatCharHashWithInitialCapacityLoadFactorAndNoEntryKeyChar() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'X'); ;
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals('X', hash.getNoEntryValue());
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.capacity());
        hash.setUp(5);
        assertEquals(5, hash.capacity());
    }

    @org.junit.Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(1.0f, value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @org.junit.Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertEquals(1, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsException() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(2);
    }

    @org.junit.Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(1.0f);
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @org.junit.Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(1.0f);
        hash.insertKey(3.0f);
        hash.insertKey(3.0f);
        hash.insertKey(3.0f);
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @org.junit.Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @org.junit.Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(4.0f);
        hash.insertKey(4.0f);
        assertEquals(4, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
    }

    @org.junit.Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        out.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInput(inputStream);
        TFloatCharHash hash2 = new TFloatCharHash();
        hash2.readExternal(in);
        in.close();
        assertEquals(2, hash2.size());
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
    }

    @org.junit.Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        out.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInput(inputStream);
        TFloatCharHash hash2 = new TFloatCharHash();
        hash2.readExternal(in);
        in.close();
        assertEquals(0, hash2.size());
        assertNull(hash2.getNoEntryKey());
        assertEquals('\u0000', hash2.getNoEntryValue());
    }
}