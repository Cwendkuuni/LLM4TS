package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        assertFalse(hash.contains(0.0f));
        assertNull(hash.getNoEntryValue());
        assertNull(hash.getNoEntryKey());
    }

    @Test
    public void testBasicFunctionality() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        assertEquals(2, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertEquals(1.0, hash.get(1.0f), 0.0);
        assertEquals(2.0, hash.get(2.0f), 0.0);
        assertNull(hash.get(3.0f));
        assertEquals(1.0, hash.getNoEntryValue(), 0.0);
        assertEquals(0.0, hash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3, 0.5f);
        for (int i = 0; i < 4; i++) {
            hash.put(i / 2.0f, i);
        }
        assertEquals(4, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
        assertNull(hash.get(3.0f));
    }

    @Test
    public void testRehashing() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3, 0.5f);
        for (int i = 0; i < 8; i++) {
            hash.put(i / 2.0f, i);
        }
        assertEquals(8, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertFalse(hash.contains(5.0f));
        assertNull(hash.get(5.0f));
        assertEquals(0.0, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3, 0.5f);
        for (int i = 0; i < 9; i++) {
            hash.put(i / 2.0f, i);
        }
    }

    @Test
    public void testRemove() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1);
        hash.put(2.0f, 2);
        hash.put(3.0f, 3);
        hash.remove(1.0f);
        assertEquals(2, hash.size());
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertEquals(2, hash.get(2.0f), 0.0);
        assertEquals(3, hash.get(3.0f), 0.0);
        assertEquals(0.0, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testProcedure() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1);
        hash.put(2.0f, 2);
        hash.put(3.0f, 3);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                System.out.println(key);
                return true;
            }
        };
        hash.forEach(procedure);
        assertEquals("1.0\n2.0\n3.0\n", procedure.getLog());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1);
        hash.put(2.0f, 2);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(hash);
        out.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) in.readObject();
        in.close();
        assertEquals(2, deserializedHash.size());
        assertTrue(deserializedHash.contains(1.0f));
        assertTrue(deserializedHash.contains(2.0f));
    }
}