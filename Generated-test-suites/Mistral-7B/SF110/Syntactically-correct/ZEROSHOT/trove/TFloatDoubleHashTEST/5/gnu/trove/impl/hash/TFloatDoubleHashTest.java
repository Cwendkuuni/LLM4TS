package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
    }

    @Test
    public void testInitializeWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
    }

    @Test
    public void testInitializeWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
    }

    @Test
    public void testInitializeWithInitialCapacityLoadFactorAndNoEntryKeyValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
        assertNotNull(hash);
        assertEquals(10, hash.size());
        assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.size());
        hash.setUp(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));

        assertFalse(hash.contains(0.0f));
        assertFalse(hash.contains(6.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        assertFalse(hash.remove(6.0f));
        assertFalse(hash.remove(1.0f));

        hash.remove(2.0f);
        assertEquals(4, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertFalse(hash.contains(5.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        assertEquals(1, hash.index(1.0f));
        assertEquals(2, hash.index(2.0f));
        assertEquals(3, hash.index(3.0f));
        assertEquals(4, hash.index(4.0f));
        assertEquals(5, hash.index(5.0f));

        assertEquals(-1, hash.index(6.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        assertFalse(hash.put(6.0f, 6.0));

        hash.put(6.0f, 6.0);
        assertEquals(6, hash.size());
        assertTrue(hash.contains(6.0f));
        assertFalse(hash.contains(7.0f));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(os);
        hash.writeExternal(out);
        out.close();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ObjectInput in = new ObjectInputStream(is);
        TFloatDoubleHash hash2 = (TFloatDoubleHash) in.readObject();
        in.close();

        assertEquals(5, hash2.size());
        assertEquals(1.0f, hash2.get(1.0f), 0.01f);
        assertEquals(2.0f, hash2.get(2.0f), 0.01f);
        assertEquals(3.0f, hash2.get(3.0f), 0.01f);
        assertEquals(4.0f, hash2.get(4.0f), 0.01f);
        assertEquals(5.0f, hash2.get(5.0f), 0.01f);
    }
}