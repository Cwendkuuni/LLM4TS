package test;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash(10);
    }

    @Test
    public void testEmptyHash() {
        assertNull(hash.getNoEntryValue());
        assertEquals(0, hash.size());
        assertFalse(hash.contains(Float.NaN));
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testBasicOperations() {
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertFalse(hash.contains(Float.NaN));
        assertFalse(hash.contains(4.0f));

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                System.out.println("Value: " + value);
                return true;
            }
        };

        hash.forEach(procedure);

        hash.remove(1.0f);
        assertEquals(2, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHash() {
        hash = new TFloatDoubleHash(3, 0.75f);
        for (int i = 0; i < hash.capacity() * 2; i++) {
            hash.put(i, (float) i);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        hash.put(null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacity() {
        hash = new TFloatDoubleHash(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactor() {
        hash = new TFloatDoubleHash(10, -1.0f);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(hash);

        ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objInput.readObject();

        assertEquals(hash.size(), deserializedHash.size());
        assertEquals(hash.getNoEntryKey(), deserializedHash.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), deserializedHash.getNoEntryValue());
    }
}