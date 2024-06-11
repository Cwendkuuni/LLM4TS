package testing; // add your testing package here

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.0);
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
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        TFloatProcedure procedure = arg -> assertEquals(1.0f, arg, 0.0);
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtWithNoEntry() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.removeAt(0);
    }

    @Test
    public void testIndexAndRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        int index = hash.index(1.0f);
        int originalIndex = index;
        assertEquals(0, index);
        hash.removeAt(index);
        index = hash.index(1.0f);
        assertNull(index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        int index = hash.index(1.0f);
        int originalIndex = index;
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        index = hash.index(1.0f);
        assertNull(index);
        index = hash.indexRehashed(1.0f, originalIndex, HashFunctions.hash(1.0f), 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        float key = hash.getNoEntryKey();
        assertEquals(1.0f, hash.get(key), 0.0);
    }

    @Test
    public void testInsertKeyWithSameKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        int index = hash.insertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        int index = hash.insertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 1.0f);
        float key = hash.getNoEntryKey();
        assertEquals(1.0f, hash.get(key), 0.0);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        byte[] bytes = byteStream.toByteArray();
        TFloatDoubleHash hashFromBytes = new TFloatDoubleHash();
        hashFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(1.0f, hashFromBytes.get(hash.getNoEntryKey()), 0.0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        TFloatDoubleHash hashFromBytes = new TFloatDoubleHash();
        hashFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));
        assertEquals(hash.getNoEntryKey(), hashFromBytes.getNoEntryKey(), 0.0);
    }
}