import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TFloatDoubleHashTest {

    @Test
    public void testDefaultConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
        assertEquals(0, hash.size());
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put((float) 1.0, 1.0);
        assertTrue(hash.contains((float) 1.0));
        assertFalse(hash.contains((float) 2.0));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsWithNullProcedure() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.forEach(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetInvalidIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put((float) 1.0, 1.0);
        hash.removeAt(Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertDuplicateKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put((float) 1.0, 1.0);
        hash.put((float) 1.0, 2.0);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0d, noEntryValue);
    }

    @Test
    public void testSetUpWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpWithNegativeInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(-1);
    }

    @Test
    public void testSetUpWithLoadFactorOne() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.setUp(0.5f);
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put((float) 1.0, 1.0);
        hash.put((float) 2.0, 2.0);
        hash.put((float) 3.0, 3.0);
        int count = 0;
        hash.forEach(value -> count++);
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put((float) 1.0, 1.0);
        hash.put((float) 2.0, 2.0);
        hash.put((float) 3.0, 3.0);
        hash.removeAt(1);
        assertFalse(hash.contains((float) 2.0));
        assertTrue(hash.contains((float) 1.0));
        assertTrue(hash.contains((float) 3.0));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put((float) 1.0, 1.0);
        hash.put((float) 2.0, 2.0);
        hash.put((float) 3.0, 3.0);
        int index = hash.index((float) 1.0);
        assertEquals(-1, index);
        index = hash.index((float) 2.0);
        assertEquals(1, index);
        index = hash.index((float) 3.0);
        assertEquals(2, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put((float) 1.0, 1.0);
        hash.put((float) 2.0, 2.0);
        hash.put((float) 3.0, 3.0);
        hash.put((float) 4.0, 4.0);
        int index = hash.index((float) 4.0);
        assertEquals(3, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.insertKey((float) 1.0);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        int index = hash.insertKeyRehash((float) 5.0, 0, 5, (byte) 0);
        assertEquals(4, index);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put((float) 1.0, 1.0);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        byte[] byteData = byteStream.toByteArray();
        TFloatDoubleHash deserializedHash = new TFloatDoubleHash();
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteData));
        deserializedHash.readExternal(in);
        assertEquals(1, deserializedHash.size());
        assertEquals((float) 1.0, deserializedHash.get(0));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        byte[] byteData = byteStream.toByteArray();
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteData));
        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) in.readObject();
        assertEquals(0, deserializedHash.size());
        in.close();
        hash.put((float) 1.0, 1.0);
        deserializedHash.put((float) 2.0, 2.0);
        assertEquals(2, deserializedHash.size());
        assertEquals((float) 1.0, hash.get(0));
        assertEquals((float) 2.0, deserializedHash.get(0));
    }
}