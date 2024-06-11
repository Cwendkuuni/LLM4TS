package org.apache.commons.cli;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(1.0f));
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNull(hash.getNoEntryValue());
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @Test
    public void testHashWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        int capacity = hash.setUp(10);
        assertEquals(13, capacity); // 10 * 1.3 = 13
        assertNull(hash.getNoEntryValue());
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @Test
    public void testHashWithInitialCapacityAndNoEntryKeyAndValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        int capacity = hash.setUp(10);
        assertEquals(13, capacity); // 10 * 1.3 = 13
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals(2.0, hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoFreeOrRemovedSlotsAvailable() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        for (int i = 0; i < 5; i++) {
            hash.insertKey(i);
        }
        hash.insertKey(5.0f);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        TFloatDoubleHash hashFromStream = new TFloatDoubleHash(5);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        hashFromStream.readExternal(in);
        assertTrue(hashFromStream.contains(1.0f));
        assertTrue(hashFromStream.contains(2.0f));
    }
}