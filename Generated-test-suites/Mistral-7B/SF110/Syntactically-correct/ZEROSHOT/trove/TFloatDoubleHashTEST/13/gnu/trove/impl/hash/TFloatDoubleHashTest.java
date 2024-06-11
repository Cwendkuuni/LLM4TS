package test;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        Assert.assertNotNull(hash);
    }

    @Test
    public void testConstructorsWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        Assert.assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @Test
    public void testConstructorsWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        Assert.assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
    }

    @Test
    public void testConstructorsWithInitialCapacityLoadFactorAndNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);;
        Assert.assertNotNull(hash);
        assertEquals(10, hash.capacity());
        assertEquals(7, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpWithNegativeInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int capacity = hash.setUp(-5);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertFalse(hash.contains(4.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f, 0.0);
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
        hash.put(3.0f, 3.0);
        hash.removeAt(1);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        assertEquals(4, hash.size());
        assertEquals(4, hash.capacity());
        assertEquals(4, hash.index(4.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);
        assertEquals(5, hash.size());
        assertEquals(8, hash.capacity());
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.writeExternal(objOut);
        objOut.close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInput(in);
        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(objIn);
        objIn.close();
        assertEquals(3, hash2.size());
        assertEquals(1.0f, hash2.get(1.0f), 0.0);
        assertEquals(2.0f, hash2.get(2.0f), 0.0);
        assertEquals(3.0f, hash2.get(3.0f), 0.0);
    }

    @Test
    public void testSerialization() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(hash);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TFloatDoubleHash hash2 = (TFloatDoubleHash) ois.readObject();
        ois.close();

        assertEquals(3, hash2.size());
        assertEquals(1.0f, hash2.get(1.0f), 0.0);
        assertEquals(2.0f, hash2.get(2.0f), 0.0);
        assertEquals(3.0f, hash2.get(3.0f), 0.0);
    }
}