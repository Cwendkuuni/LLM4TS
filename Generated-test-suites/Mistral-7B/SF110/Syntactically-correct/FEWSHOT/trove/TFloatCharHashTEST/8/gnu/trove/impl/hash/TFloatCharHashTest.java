package org.apache.commons.cli; // Assuming you want to test TFloatCharHash in the context of the Apache Commons CLI package

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testCustomInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testCustomLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testCustomInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertArrayEquals(new float[10], hash._set);
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(0.0f, 'a');
        assertTrue(hash.contains(0.0f));
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(0.0f, 'a');
        hash.insert(1.0f, 'b');
        hash.insert(0.0f, 'c');
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(0.0f, key);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(0.0f, 'a');
        hash.insert(1.0f, 'b');
        hash.insert(0.0f, 'c');
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(0.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(0.0f, 'a');
        hash.insert(1.0f, 'b');
        hash.insert(0.0f, 'c');
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.insertKey(0.0f));
        assertEquals(5, hash.size());
        assertTrue(hash.contains(0.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatCharHash hash = new TFloatCharHash(5);
        for (float i = 0; i < 6; i++) {
            hash.insertKey(i);
        }
    }

    @Test
    public void testWriteReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(0.0f, 'a');
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectOutput objectOutput = Channels.newOutputChannel(out).asObjectOutput();
        ObjectInput objectInput = Channels.newInputStream(in);
        hash.writeExternal(objectOutput);
        TFloatCharHash hashRead = new TFloatCharHash();
        hashRead.readExternal(objectInput);
        assertEquals(1, hashRead.size());
        assertTrue(hashRead.contains(0.0f));
    }
}