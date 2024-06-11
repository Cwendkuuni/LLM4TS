package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertFalse(hash.contains(1.0f));
        assertNull(hash.getNoEntryValue());
        assertNull(hash.getNoEntryKey());
    }

    @Test
    public void testBasicFunctionality() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(2.0f, 'a');
        hash.insertKey(3.0f, 'b');
        hash.insertKey(1.0f, 'c');

        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertEquals('c', hash.get(1.0f));
        assertEquals('a', hash.get(2.0f));
        assertEquals('b', hash.get(3.0f));
        assertNull(hash.get(4.0f));

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key > 1.0f;
            }
        };
        assertTrue(hash.forEach(procedure));

        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
        assertEquals(2, hash.size());
    }

    @Test
    public void testRehashing() {
        TFloatCharHash hash = new TFloatCharHash(2);
        hash.insertKey(1.0f, 'a');
        hash.insertKey(1.0f, 'b');
        hash.insertKey(1.0f, 'c');

        assertEquals(3, hash.size());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key > 1.0f;
            }
        };
        assertFalse(hash.forEach(procedure));

        hash = new TFloatCharHash(10);
        hash.insertKey(1.0f, 'a');
        hash.insertKey(1.0f, 'b');
        hash.insertKey(1.0f, 'c');

        assertEquals(3, hash.size());

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f, 'a');
        hash.insertKey(2.0f, 'b');

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(hash);
        objStream.close();

        ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatCharHash deserializedHash = (TFloatCharHash) objInput.readObject();
        objInput.close();

        assertEquals(2, deserializedHash.size());
        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
    }
}