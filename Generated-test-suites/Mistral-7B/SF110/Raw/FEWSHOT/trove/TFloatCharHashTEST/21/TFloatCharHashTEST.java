package org.apache.commons.cli;

import gnu.trove.procedure.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashCreation() {
        TFloatCharHash hash = new TFloatCharHash();
        TFloatCharHash hashWithInitialCapacity = new TFloatCharHash(10);
        TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(10, 0.75f);
        TFloatCharHash hashWithInitialCapacityLoadFactorAndKeys = new TFloatCharHash(10, 0.75f, 0.0f, '\0');

        assertEquals(0, hash.size());
        assertEquals(0, hashWithInitialCapacity.size());
        assertEquals(0, hashWithInitialCapacityAndLoadFactor.size());
        assertEquals(0, hashWithInitialCapacityLoadFactorAndKeys.size());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        TFloatCharHash hashWithNoEntryKey = new TFloatCharHash(0.0f);

        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0f, hashWithNoEntryKey.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        TFloatCharHash hashWithNoEntryValue = new TFloatCharHash('\0');

        assertEquals('\0', hash.getNoEntryValue());
        assertEquals('\0', hashWithNoEntryValue.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new TFloatCharHash(-1);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);

        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                count++;
                return true;
            }
        });

        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);

        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals(1.0f, hash._set[0]);
        assertEquals(2.0f, hash._set[1]);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index = hash.index(1.0f);
        assertEquals(0, index);

        index = hash.index(2.0f);
        assertEquals(1, index);

        index = hash.index(3.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(2);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index = hash.indexRehashed(1.0f, 0, 1, 0);
        assertEquals(-1, index);

        index = hash.indexRehashed(2.0f, 0, 1, 0);
        assertEquals(1, index);

        index = hash.indexRehashed(3.0f, 0, 1, 0);
        assertEquals(-1, index);

        hash.insertKey(3.0f);
        index = hash.indexRehashed(3.0f, 0, 1, 0);
        assertEquals(2, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();

        hash.insertKey(1.0f);
        assertEquals(1.0f, hash._set[0]);
        assertEquals(1, hash.size());
        assertTrue(hash.contains(1.0f));

        hash.insertKey(2.0f);
        assertEquals(2.0f, hash._set[1]);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(2);

        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.indexRehashed(3.0f, 0, 1, 0);
        assertEquals(1, index);

        assertEquals(3.0f, hash._set[1]);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        hash.writeExternal(objectOutput);
        objectOutput.close();

        TFloatCharHash deserializedHash = new TFloatCharHash();
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        deserializedHash.readExternal(objectInput);
        objectInput.close();

        assertEquals(hash.getNoEntryKey(), deserializedHash.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), deserializedHash.getNoEntryValue());
        assertEquals(hash.size(), deserializedHash.size());
        assertEquals(hash.containsAll(deserializedHash), true);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        hash.writeExternal(objectOutput);
        objectOutput.close();

        TFloatCharHash deserializedHash = new TFloatCharHash();
        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        deserializedHash.readExternal(objectInput);
        objectInput.close();

        assertEquals(hash.getNoEntryKey(), deserializedHash.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), deserializedHash.getNoEntryValue());
        assertEquals(hash.size(), deserializedHash.size());
        assertEquals(hash.containsAll(deserializedHash), true);
    }
}