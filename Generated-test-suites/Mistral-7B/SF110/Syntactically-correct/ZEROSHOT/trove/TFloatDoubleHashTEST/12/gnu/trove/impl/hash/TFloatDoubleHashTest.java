package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatDoubleHashTest {

    @org.junit.Before
    public void setUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
    }

    @org.junit.Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.1f));
        assertEquals(0, hash.size());
    }

    @org.junit.Test
    public void testInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(0.1f));
        assertEquals(0, hash.size());
    }

    @org.junit.Test
    public void testLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        assertFalse(hash.contains(0.1f));
        assertEquals(0, hash.size());
    }

    @org.junit.Test
    public void testNoEntryValues() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0f, noEntryKey, 0.001f);
        assertEquals(0.0, noEntryValue, 0.001);
    }

    @org.junit.Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertEquals(5, hash.size());
    }

    @org.junit.Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(0.1f, 1.0);
        assertTrue(hash.contains(0.1f));
        hash.put(0.2f, 2.0);
        assertTrue(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
    }

    @org.junit.Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(0.1f, key, 0.001f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @org.junit.Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        hash.removeAt(0);
        assertFalse(hash.contains(0.1f));
        assertTrue(hash.contains(0.2f));
    }

    @org.junit.Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);
        int index = hash.index(0.1f);
        assertEquals(0, index);
        index = hash.index(0.2f);
        assertEquals(1, index);
        index = hash.index(0.3f);
        assertEquals(-1, index);
    }

    @org.junit.Test
    public void testWriteReadExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(0.1f, 1.0);
        hash.put(0.2f, 2.0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        hash.writeExternal(out);
        out.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInput(inputStream);
        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(in);
        in.close();

        assertEquals(hash.size(), hash2.size());
        assertTrue(hash.containsAll(hash2));
        assertTrue(hash2.containsAll(hash));
    }
}