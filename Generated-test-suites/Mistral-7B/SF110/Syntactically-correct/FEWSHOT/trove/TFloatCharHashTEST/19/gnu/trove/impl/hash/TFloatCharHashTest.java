package org.apache.commons.cli; //package name should be correct, assuming TFloatCharHash is part of the org.apache.commons.cli package

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndKeysAndValuesConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 0.0f, '\0');
        assertEquals(0, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        float key = 1.0f;
        assertFalse(hash.contains(key));

        hash.insertKey(key);
        assertTrue(hash.contains(key));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);

        TFloatProcedure procedure = new TFloatProcedure() {
            private int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertTrue(hash.forEach(procedure));
        assertEquals(3, procedure.getInvocations());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        float key = 1.0f;

        hash.insertKey(key);

        int index = hash.index(key);
        hash.removeAt(index);

        assertFalse(hash.contains(key));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.close();

        TFloatCharHash deserializedHash = new TFloatCharHash(10);
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        deserializedHash.readExternal(in);
        in.close();

        assertEquals(3, deserializedHash.size());
        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
        assertTrue(deserializedHash.contains(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        float key = 1.0f;

        int index = hash.insertKey(key);
        assertTrue(hash.contains(key));
        assertEquals(1, hash.size());
        assertEquals(index, hash.index(key));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWithFullHashSet() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.insertKey(1.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWithFullHashSet() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.index(1.0f);
        assertEquals(0, index);

        index = hash.index(2.0f);
        assertEquals(1, index);

        index = hash.index(3.0f);
        assertEquals(2, index);

        index = hash.index(-1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(10);

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.indexRehashed(1.0f, 0, 1, (byte) 0);
        assertEquals(0, index);

        index = hash.indexRehashed(2.0f, 0, 1, (byte) 0);
        assertEquals(1, index);

        index = hash.indexRehashed(3.0f, 0, 1, (byte) 0);
        assertEquals(2, index);

        index = hash.indexRehashed(-1.0f, 0, 1, (byte) 0);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(10);

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.insertKey(4.0f);

        int index = hash.insertKeyRehash(4.0f, 0, 1, (byte) 0);
        assertEquals(3, index);
    }

    @Test
    public void testInsertKeyRehashWithRemovedSlot() {
        TFloatCharHash hash = new TFloatCharHash(10);

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.removeAt(1);

        hash.insertKey(4.0f);

        int index = hash.insertKeyRehash(4.0f, 0, 1, (byte) 0);
        assertEquals(2, index);
    }
}