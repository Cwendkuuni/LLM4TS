package gnu.trove.impl.hash.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertTrue(hash.isEmpty());
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertSame('\0', hash.getNoEntryValue());
    }

    @Test
    public void testHashCreation() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertSame('\0', hash.getNoEntryValue());
    }

    @Test
    public void testHashCreationWithLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
        assertEquals(20, hash.size());
        assertNull(hash.getNoEntryKey());
        assertSame('\0', hash.getNoEntryValue());
    }

    @Test
    public void testHashCreationWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
        assertEquals(20, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertSame('\0', hash.getNoEntryValue());
    }

    @Test
    public void testHashSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.capacity());
        assertArrayEquals(new float[10], hash._set);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.put((float) 3, 'c');
        assertTrue(hash.contains((float) 1));
        assertTrue(hash.contains((float) 2));
        assertTrue(hash.contains((float) 3));
        assertFalse(hash.contains((float) 4));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.put((float) 3, 'c');

        TFloatProcedure procedure = new TFloatProcedure() {
            private int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.put((float) 3, 'c');
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains((float) 2));
        assertTrue(hash.contains((float) 1));
        assertTrue(hash.contains((float) 3));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.put((float) 3, 'c');
        assertEquals(1, hash.index((float) 1));
        assertEquals(2, hash.index((float) 2));
        assertEquals(3, hash.index((float) 3));
        assertEquals(-1, hash.index((float) 4));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFullHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.put((float) 3, 'c');
        hash.put((float) 4, 'd');
        hash.put((float) 5, 'e');
        hash.put((float) 6, 'f');
        hash.put((float) 7, 'g');
        hash.put((float) 8, 'h');
        hash.put((float) 9, 'i');
        hash.put((float) 10, 'j');
        hash.put((float) 11, 'k');
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.put((float) 3, 'c');

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);

        TFloatCharHash deserializedHash = new TFloatCharHash(10);
        deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray())));

        assertEquals(3, deserializedHash.size());
        assertArrayEquals(new float[] { 1, 2, 3 }, deserializedHash._set);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(10);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        byte[] serializedData = outputStream.toByteArray();

        ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(serializedData));
        TFloatCharHash deserializedHash = (TFloatCharHash) input.readObject();

        assertEquals(3, deserializedHash.size());
        assertArrayEquals(new float[] { 0.0f, 0.0f, 0.0f }, deserializedHash._set);

        input.readObject(); // no_entry_key
        input.readObject(); // no_entry_value

        assertEquals(3, deserializedHash.size());
        assertArrayEquals(new float[] { 1, 2, 3 }, deserializedHash._set);
    }
}