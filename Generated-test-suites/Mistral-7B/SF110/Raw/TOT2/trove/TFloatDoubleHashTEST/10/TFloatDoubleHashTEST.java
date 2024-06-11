package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    // Test cases for all public methods of TFloatDoubleHash here

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.0f, 1.0);
        assertTrue(hashMap.contains(1.0f));
    }

    // ...

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.put(1.0f, 1.0);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(hashMap);
        byte[] serializedData = byteStream.toByteArray();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedData);
        ObjectInputStream objInput = new ObjectInputStream(byteInput);
        TFloatDoubleHash deserializedHashMap = (TFloatDoubleHash) objInput.readObject();
        assertEquals(hashMap.getNoEntryKey(), deserializedHashMap.getNoEntryKey());
        assertEquals(hashMap.getNoEntryValue(), deserializedHashMap.getNoEntryValue());
        assertEquals(hashMap.size(), deserializedHashMap.size());
        assertTrue(deserializedHashMap.contains(1.0f));
    }

    @Test(expected = IOException.class)
    public void testSerializationInvalidInput() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeInt(1); // Writing an integer instead of an object
        objStream.close();
    }
}