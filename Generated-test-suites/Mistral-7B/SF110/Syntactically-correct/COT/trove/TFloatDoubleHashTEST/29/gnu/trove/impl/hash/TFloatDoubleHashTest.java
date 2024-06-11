package tests; // replace with your test package if needed

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactorConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size()); // assuming initial data is added
    }

    @Test
    public void testTFloatDoubleHashInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
        assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.noEntryKey(), 0.001);
        assertEquals(0.0, hash.noEntryValue(), 0.001);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.001);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsNullValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, "not a float"); // replace with your test case for a null value
        assertFalse(hash.contains(null));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(1.0f, value, 0.001);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.removeAt(1);
        assertFalse(hash.containsKey(1.0f));
        assertTrue(hash.containsKey(2.0f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtIndexOutOfBounds() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.removeAt(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void testContainsEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        int index = hash.index(1.0f);
        assertEquals(0, index);
        index = hash.index(2.0f);
        assertEquals(1, index);
        index = hash.index(3.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        int index = hash.indexRehashed(1.0f, 0, 1, (byte) 0);
        assertEquals(-1, index);
        index = hash.indexRehashed(2.0f, 0, 1, (byte) 1);
        assertEquals(1, index);
        index = hash.indexRehashed(3.0f, 0, 1, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1.0f, hash.get(0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyNullValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(null); // replace with your test case for a null value
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        int index = hash.insertKey(5.0f);
        assertEquals(3, index);
        assertEquals(5.0f, hash.get(3), 0.001);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyNoFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        for (int i = 0; i < hash.capacity(); i++) {
            hash.put(i, i);
        }
        hash.insertKey(hash.getNoEntryKey());
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        TFloatDoubleHash loadedHash = new TFloatDoubleHash();
        loadedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())));
        assertEquals(2, loadedHash.size());
        assertEquals(1.0f, loadedHash.get(0), 0.001);
        assertEquals(2.0f, loadedHash.get(1), 0.001);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TFloatDoubleHash loadedHash = new TFloatDoubleHash();
        loadedHash.readExternal(in);
        assertEquals(0, loadedHash.size());
        loadedHash.put(1.0f, 1.0);
        loadedHash.put(2.0f, 2.0);
        assertEquals(2, loadedHash.size());
        assertEquals(1.0f, loadedHash.get(0), 0.001);
        assertEquals(2.0f, loadedHash.get(1), 0.001);
    }
}