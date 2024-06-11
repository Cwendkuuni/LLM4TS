package gnu.trove.impl.hash.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

import org.junit.Test;

import gnu.trove.TFloatProcedure;
import gnu.trove.TFloatProcedureAdapter;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.impl.hash.TPrimitiveHash;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructors() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        TFloatDoubleHash hash1 = new TFloatDoubleHash(10);
        assertEquals(0, hash1.size());
        TFloatDoubleHash hash2 = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0, hash2.size());
        TFloatDoubleHash hash3 = new TFloatDoubleHash(10, 0.5f, Float.NaN, Double.NaN);
        assertEquals(0, hash3.size());
    }

    @Test
    public void testGetters() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNull(hash.getNoEntryKey());
        assertNull(hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(Float.NaN));
        hash.insertKey(Float.NaN);
        assertTrue(hash.contains(Float.NaN));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        TFloatProcedure procedure = new TFloatProcedureAdapter() {
            private int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };
        hash.forEach(procedure);
        assertEquals(0, hash.size());
        assertEquals(0, count);

        hash.insertKey(Float.NaN);
        hash.forEach(procedure);
        assertEquals(1, hash.size());
        assertEquals(1, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(Float.NaN);
        hash.removeAt(0);
        assertFalse(hash.contains(Float.NaN));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtOutOfBounds() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.removeAt(10);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNull(hash.index(Float.NaN));

        hash.insertKey(Float.NaN);
        int index = hash.index(Float.NaN);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(Float.NaN);
        hash.insertKey(Float.NaN);

        int index = hash.indexRehashed(Float.NaN, 0, 0, (byte) 1);
        assertEquals(-1, index);

        index = hash.indexRehashed(Float.NaN, 0, 0, (byte) 2);
        assertEquals(1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(Float.NaN);
        assertTrue(hash.contains(Float.NaN));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatDoubleHash hash = new TFloatDoubleHash(1);
        hash.insertKey(Float.NaN);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(Float.NaN);
        hash.insertKey(Float.NaN);

        hash.insertKey(Float.NaN);
        int index = hash.indexRehashed(Float.NaN, 0, 0, (byte) 1);
        assertEquals(2, index);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKeyAt(0, Float.NaN);
        assertTrue(hash.contains(Float.NaN));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInsertKeyAtOutOfBounds() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKeyAt(10, Float.NaN);
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(Float.NaN);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new java.io.ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new java.io.ObjectInputStream(byteInput);
        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(in);
        in.close();

        assertTrue(hash2.contains(Float.NaN));
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new java.io.ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new java.io.ObjectInputStream(byteInput);
        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(in);
        in.close();

        assertTrue(hash2.contains(Float.NaN));
    }
}