package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatCharHashTest {

    @Test
    public void testInitialization() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());

        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(10);
        assertEquals(0, hashWithInitialCapacity.size());
        assertEquals(10, hashWithInitialCapacity.capacity());
        assertNull(hashWithInitialCapacity.getNoEntryKey());
        assertEquals('\0', hashWithInitialCapacity.getNoEntryValue());

        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        assertEquals(0, hashWithInitialCapacityAndLoadFactor.size());
        assertEquals(10, hashWithInitialCapacityAndLoadFactor.capacity());
        assertEquals(0.75f, hashWithInitialCapacityAndLoadFactor.loadFactor(), 0.01f);
        assertNull(hashWithInitialCapacityAndLoadFactor.getNoEntryKey());
        assertEquals('\0', hashWithInitialCapacityAndLoadFactor.getNoEntryValue());

        TFloatCharHash hashWithInitialCapacityAndLoadFactorAndKeys = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
        assertEquals(0, hashWithInitialCapacityAndLoadFactorAndKeys.size());
        assertEquals(10, hashWithInitialCapacityAndLoadFactorAndKeys.capacity());
        assertEquals(0.75f, hashWithInitialCapacityAndLoadFactorAndKeys.loadFactor(), 0.01f);
        assertEquals(1.0f, hashWithInitialCapacityAndLoadFactorAndKeys.getNoEntryKey());
        assertEquals('A', hashWithInitialCapacityAndLoadFactorAndKeys.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(1.0f));

        hash.put(1.0f, 'A');
        assertTrue(hash.contains(1.0f));

        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        hash.forEach(procedure);

        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        hash.removeAt(1);

        assertEquals(2, hash.size());
        assertFalse(hash.contains(2.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();

        // Empty hash
        assertNull(hash.index(1.0f));

        hash.put(1.0f, 'A');

        // Exact match
        assertEquals(0, hash.index(1.0f));

        // Different key
        assertNull(hash.index(2.0f));

        // Key not present, but index is non-negative
        assertEquals(1, hash.index(3.0f));

        // Key not present, but index is negative
        assertNull(hash.index(-1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();

        // Empty hash
        assertEquals(-1, hash.insertKey(1.0f));

        hash.put(1.0f, 'A');

        // Key already present
        assertEquals(-1, hash.insertKey(1.0f));

        // Key not present, but index is non-negative
        assertEquals(0, hash.insertKey(2.0f));

        // Key not present, but index is negative
        assertEquals(-1, hash.insertKey(-1.0f));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'A');
        hash.put(2.0f, 'B');
        hash.put(3.0f, 'C');

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        hash.writeExternal(out);
        out.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInput in = new ObjectInput(bis);
        TFloatCharHash hashFromStream = new TFloatCharHash();
        hashFromStream.readExternal(in);
        in.close();

        assertEquals(hash.size(), hashFromStream.size());
        assertEquals(hash.capacity(), hashFromStream.capacity());
        assertEquals(hash.loadFactor(), hashFromStream.loadFactor(), 0.01f);

        int i = 0;
        for (float key : hash.keySet()) {
            assertEquals(hash.get(key), hashFromStream.get(key));
            i++;
        }

        assertEquals(hash.size(), i);
    }
}