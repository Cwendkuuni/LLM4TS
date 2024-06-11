import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testContainsEmpty() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(1.0f));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsNull() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.contains(null);
    }

    @Test
    public void testContainsNegativeIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        assertFalse(hash.contains(-1.0f));
    }

    @Test
    public void testContainsNonExistentKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        assertFalse(hash.contains(2.0f));
    }

    // Test other methods here...

    @Test
    public void testWriteRead() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        hash.writeExternal(objOut);
        ObjectInput objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        TFloatDoubleHash hash2 = (TFloatDoubleHash) objIn.readObject();
        assertTrue(hash2.contains(1.0f));
    }

}