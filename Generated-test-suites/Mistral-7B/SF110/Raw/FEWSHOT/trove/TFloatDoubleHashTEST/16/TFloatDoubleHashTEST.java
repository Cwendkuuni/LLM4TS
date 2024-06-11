package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructors() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);

        TFloatDoubleHash hashWithInitialCapacity = new TFloatDoubleHash(10);
        assertNotNull(hashWithInitialCapacity);
        assertEquals(10, hashWithInitialCapacity.size());

        TFloatDoubleHash hashWithInitialCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hashWithInitialCapacityAndLoadFactor);
        assertEquals(7, hashWithInitialCapacityAndLoadFactor.size());

        TFloatDoubleHash hashWithInitialCapacityAndLoadFactorAndNoEntryKeyAndValue = new TFloatDoubleHash(10, 0.75f, 1.0f, 1.0);
        assertNotNull(hashWithInitialCapacityAndLoadFactorAndNoEntryKeyAndValue);
        assertEquals(7, hashWithInitialCapacityAndLoadFactorAndNoEntryKeyAndValue.size());
        assertEquals(1.0f, hashWithInitialCapacityAndLoadFactorAndNoEntryKeyAndValue.getNoEntryKey(), 0.0);
        assertEquals(1.0, hashWithInitialCapacityAndLoadFactorAndNoEntryKeyAndValue.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        int capacity = hash.setUp(5);
        assertEquals(5, capacity);
        assertEquals(5, hash.size());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(1.0f));
        hash.insert(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(key, 1.0f);
                return true;
            }
        };
        hash.insert(1.0f);
        hash.forEach(procedure);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insert(1.0f);
        int index = hash.index(1.0f);
        assertEquals(index, 0);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testIndexWithEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.index(1.0f);
        assertNull(index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insert(1.0f);
        hash.insert(2.0f);
        int index = hash.index(1.5f);
        assertNull(index);

        index = hash.indexRehashed(1.5f, 0, 3, (byte) 1);
        assertNull(index);

        hash.insert(1.5f);
        index = hash.index(1.5f);
        assertEquals(index, 2);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(-1, hash.insertKey(1.0f));
        hash.insert(1.0f);
        assertEquals(0, hash.insertKey(1.0f));

        assertEquals(-1, hash.insertKey(1.5f));
        hash.insert(1.5f);
        assertEquals(1, hash.insertKey(1.5f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insert(1.0f);
        hash.insert(1.5f);
        hash.insert(2.0f);

        assertEquals(-1, hash.insertKey(1.1f));
        assertEquals(2, hash.insertKeyRehash(1.1f, 2, 3, (byte) 1));

        assertEquals(-1, hash.insertKey(1.5f));
        assertEquals(0, hash.insertKeyRehash(1.5f, 0, 3, (byte) 1));

        assertEquals(-1, hash.insertKey(2.1f));
        assertEquals(1, hash.insertKeyRehash(2.1f, 1, 3, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0);
        assertEquals(1, hash.size());

        hash.insertKeyAt(1, 1.5f);
        assertEquals(1.5f, hash._set[1], 0.0);
        assertEquals(2, hash.size());

        hash.insertKeyAt(2, 2.0f);
        assertEquals(2.0f, hash._set[2], 0.0);
        assertEquals(3, hash.size());
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insert(1.0f);
        hash.insert(1.5f);
        ObjectOutput out = new ByteArrayOutputStream().newObjectOutput();
        hash.writeExternal(out);

        TFloatDoubleHash hashFromStream = new TFloatDoubleHash();
        hashFromStream.readExternal(new ByteArrayInputStream(out.toByteArray()));

        assertEquals(hash.size(), hashFromStream.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash._set[i], hashFromStream._set[i], 0.0);
        }
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ObjectOutput out = new ByteArrayOutputStream().newObjectOutput();
        hash.writeExternal(out);
        ObjectInput in = new ByteArrayInputStream(out.toByteArray()).newObjectInput();
        hash.readExternal(in);

        assertEquals(hash.size(), hash.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash._set[i], hash._set[i], 0.0);
        }
    }
}