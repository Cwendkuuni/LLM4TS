package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

import org.junit.Test;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
        assertTrue(hash.isEmpty());
        assertNull(hash.getNoEntryValue());
        assertNull(hash.getNoEntryKey());
    }

    @Test
    public void testIntInitialCapacityConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertNull(hash.getNoEntryValue());
        assertNull(hash.getNoEntryKey());
        assertEquals(10, hash.size());
    }

    @Test
    public void testIntInitialCapacityAndLoadFactorConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f);
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertNull(hash.getNoEntryValue());
        assertNull(hash.getNoEntryKey());
        assertEquals(10, hash.size());
    }

    @Test
    public void testIntInitialCapacityLoadFactorAndNoEntryValuesConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
        assertEquals(10, hash.size());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.containsKey(1.0f));
        assertTrue(hash.containsValue(1.0));
        assertFalse(hash.contains(3.0f));
        assertFalse(hash.containsKey(3.0f));
        assertFalse(hash.containsValue(3.0));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f, 0.0);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        int index = hash.index(1.0f);
        assertEquals(0, index);
        index = hash.index(3.0f);
        assertEquals(2, index);
        index = hash.index(4.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        hash.put(3.0f, 3.0);
        hash.put(4.0f, 4.0);
        hash.put(5.0f, 5.0);
        assertEquals(5, hash.size());
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(0, hash.insertKey(6.0f));
        assertEquals(6, hash.size());
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.0f, 1.0);
        hash.put(2.0f, 2.0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(out);
        hash.writeExternal(objOut);
        objOut.close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInputStream(in);
        TFloatDoubleHash hash2 = (TFloatDoubleHash) objIn.readObject();
        objIn.close();
        assertEquals(hash.size(), hash2.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.keyAt(i), hash2.keyAt(i), 0.0);
            assertEquals(hash.valueAt(i), hash2.valueAt(i), 0.0);
        }
    }

}