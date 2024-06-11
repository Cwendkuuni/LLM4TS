package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testInitialCapacityConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(10, hash.size(), 10); // initialCapacity + empty slot
        assertNull(hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndKeysConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        assertEquals(10, hash.size(), 10); // initialCapacity + empty slot
        assertEquals(1.0f, hash.getNoEntryKey(), 1.0f);
        assertEquals(2.0, hash.getNoEntryValue(), 2.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertArrayEquals(new float[10], hash._set, 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TFloatDoubleHash(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        new TFloatDoubleHash(1, -0.5f);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insert(1.0f, 1.0);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);

        assertEquals(3, hash.size());
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);

        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(4.0f));
        assertEquals(-1, hash.index(1.5f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        assertEquals(0, hash.size());

        int index = hash.insertKey(1.0f);
        assertEquals(1, hash.size());
        assertEquals(1.0f, hash.get(index), 0.0f);
        assertEquals(1.0f, hash.getNoEntryKey(), 1.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);

        index = hash.insertKey(2.0f);
        assertEquals(2, hash.size());
        assertEquals(2.0f, hash.get(index), 0.0f);
        assertEquals(1.0f, hash.get(0), 0.0f);
        assertEquals(1.0f, hash.getNoEntryKey(), 1.0f);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);

        index = hash.insertKey(1.0f); // should rehash
        assertEquals(-1, index);
        assertEquals(3, hash.size());
        assertEquals(2.0f, hash.get(1), 0.0f);
        assertEquals(1.0f, hash.get(2), 0.0f);
        assertEquals(1.0f, hash.get(0), 0.0f);
    }

    @Test
    public void testWriteAndReadExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(out);
        hash.writeExternal(objOut);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInput(in);
        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(objIn);
        objIn.close();

        assertEquals(3, hash2.size());
        assertEquals(1.0f, hash2.get(0), 0.0f);
        assertEquals(2.0f, hash2.get(1), 0.0f);
        assertEquals(3.0f, hash2.get(2), 0.0f);
    }

    @Test
    public void testConsumeFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);

        assertEquals(3, hash.size());
        hash.consumeFreeSlot = true;
        int index = hash.insertKey(4.0f);
        assertEquals(4, hash.size());
        assertEquals(4.0f, hash.get(3), 0.0f);
    }
}