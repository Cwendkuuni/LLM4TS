package org.apache.commons.cli;

import gnu.trove.procedure.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.capacity());
        assertArrayEquals(new float[10], hash._set);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        assertTrue(hash.contains((float) 1));
        assertFalse(hash.contains((float) 2));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals((float) 1, value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, hash.size());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.removeAt(0);
        assertEquals((float) 2, hash._set[0]);
        assertEquals(9, hash.capacity());
        assertEquals(1, hash.size());
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.put((float) 2, 'b');
        hash.put((float) 3, 'c');
        assertEquals(3, hash.index((float) 3));
        assertEquals(-1, hash.index((float) 4));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey((float) 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey((float) 1);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOut = new ObjectOutputStream(out);
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put((float) 1, 'a');
        hash.writeExternal(objectOut);
        ObjectInput objectIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        TFloatCharHash deserializedHash = (TFloatCharHash) objectIn.readObject();
        assertEquals((float) 1, deserializedHash.get(_set[0]));
        assertEquals('a', deserializedHash.getNoEntryValue());
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOut = new ObjectOutputStream(out);
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.writeExternal(objectOut);
        ObjectInput objectIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        TFloatCharHash deserializedHash = (TFloatCharHash) objectIn.readObject();
        assertEquals(10, deserializedHash.capacity());
        assertEquals(0.0f, deserializedHash.getNoEntryKey());
        assertEquals('\0', deserializedHash.getNoEntryValue());
    }
}