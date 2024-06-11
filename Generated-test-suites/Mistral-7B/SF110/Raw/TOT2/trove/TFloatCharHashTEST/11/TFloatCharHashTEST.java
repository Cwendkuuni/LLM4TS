import org.junit.Test;
import org.junit.Assert.*;
import gnu.trove.TFloatProcedure;
import gnu.trove.impl.hash.TFloatCharHash;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

    @Test
    public void testEmptyConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(Float.NaN));
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(5, 0.75f);
        assertEquals(5, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertTrue(hash.contains(0.5f));
        assertFalse(hash.contains(1.5f));
    }

    // ... (Other basic test cases)

    @Test
    public void testContainsWithFloatNaN() {
        TFloatCharHash hash = new TFloatCharHash(5, 0.75f);
        hash.insert(0.5f);
        assertFalse(hash.contains(Float.NaN));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithIllegalArguments() {
        new TFloatCharHash(-5, 0.75f, Float.NaN, '\u0000');
    }

    // ... (Edge cases and exception handling test cases)

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5, 0.75f);
        hash.insert(0.5f);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(os);
        hash.writeExternal(out);
        byte[] bytes = os.toByteArray();

        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(is);
        TFloatCharHash deserializedHash = (TFloatCharHash) in.readObject();

        assertEquals(hash.size(), deserializedHash.size());
        assertEquals(hash.contains(0.5f), deserializedHash.contains(0.5f));
    }
}