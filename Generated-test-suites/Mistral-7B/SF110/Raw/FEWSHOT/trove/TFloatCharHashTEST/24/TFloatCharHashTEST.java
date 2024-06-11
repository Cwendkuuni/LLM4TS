package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.impl.*;
import gnu.trove.procedure.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertFalse(hash.contains(0.0f));
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testBasicFunctionality() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0, hash.size());

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        hash.put(4.0f, 'd');

        assertEquals(4, hash.size());
        assertTrue(hash.contains(1.0f));
        assertEquals('a', hash.get(1.0f));
        assertTrue(hash.contains(2.0f));
        assertEquals('b', hash.get(2.0f));
        assertTrue(hash.contains(3.0f));
        assertEquals('c', hash.get(3.0f));
        assertTrue(hash.contains(4.0f));
        assertEquals('d', hash.get(4.0f));

        assertFalse(hash.contains(5.0f));
        assertNull(hash.get(5.0f));

        assertFalse(hash.isEmpty());

        hash.remove(1.0f);
        assertEquals(3, hash.size());
        assertFalse(hash.contains(1.0f));
        assertNull(hash.get(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
    }

    @Test
    public void testPutAndRemoveInHashCapacity() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        hash.put(4.0f, 'd');
        hash.put(5.0f, 'e');
        hash.put(6.0f, 'f');

        assertEquals(6, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));

        hash.remove(1.0f);
        assertEquals(5, hash.size());
        assertFalse(hash.contains(1.0f));
        assertNull(hash.get(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));

        hash.put(7.0f, 'g');
        assertEquals(6, hash.size());
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
        assertTrue(hash.contains(7.0f));

        hash.remove(3.0f);
        assertEquals(5, hash.size());
        assertFalse(hash.contains(3.0f));
        assertNull(hash.get(3.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
        assertTrue(hash.contains(7.0f));
    }

    @Test
    public void testPutAndRemoveInRehashedState() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        hash.put(4.0f, 'd');
        hash.put(5.0f, 'e');
        hash.put(6.0f, 'f');
        hash.put(7.0f, 'g');
        hash.put(8.0f, 'h');
        hash.put(9.0f, 'i');
        hash.put(10.0f, 'j');

        assertEquals(10, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
        assertTrue(hash.contains(7.0f));
        assertTrue(hash.contains(8.0f));
        assertTrue(hash.contains(9.0f));
        assertTrue(hash.contains(10.0f));

        hash.remove(3.0f);
        assertEquals(9, hash.size());
        assertFalse(hash.contains(3.0f));
        assertNull(hash.get(3.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
        assertTrue(hash.contains(7.0f));
        assertTrue(hash.contains(8.0f));
        assertTrue(hash.contains(9.0f));
        assertTrue(hash.contains(10.0f));

        hash.put(11.0f, 'k');
        assertEquals(10, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
        assertTrue(hash.contains(7.0f));
        assertTrue(hash.contains(8.0f));
        assertTrue(hash.contains(9.0f));
        assertTrue(hash.contains(10.0f));
        assertTrue(hash.contains(11.0f));

        hash.remove(1.0f);
        assertEquals(9, hash.size());
        assertFalse(hash.contains(1.0f));
        assertNull(hash.get(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
        assertTrue(hash.contains(7.0f));
        assertTrue(hash.contains(8.0f));
        assertTrue(hash.contains(9.0f));
        assertTrue(hash.contains(10.0f));
        assertTrue(hash.contains(11.0f));
    }

    @Test
    public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);

        TFloatCharHash deserializedHash = (TFloatCharHash) objectInput.readObject();
        objectInput.close();

        assertEquals(3, deserializedHash.size());
        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
        assertTrue(deserializedHash.contains(3.0f));
    }
}