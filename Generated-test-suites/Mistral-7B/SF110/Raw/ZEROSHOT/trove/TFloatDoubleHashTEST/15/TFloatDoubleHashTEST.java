package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.size());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testInitialCapacityHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.size());
        assertArrayEquals(new float[10], hash._set);
    }

    @Test
    public void testLoadFactorHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        assertEquals(5, hash.size());
        assertArrayEquals(new float[5], hash._set);
    }

    @Test
    public void testNoEntryValuesHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f, 1.0f, 1.0);
        assertEquals(5, hash.size());
        assertArrayEquals(new float[5], hash._set);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(1.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContainsHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);
        hash.insert(4.0f, 4.0);
        hash.insert(5.0f, 5.0);
        hash.insert(6.0f, 6.0);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
    }

    @Test
    public void testForEachHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);
        hash.insert(4.0f, 4.0);
        hash.insert(5.0f, 5.0);
        hash.insert(6.0f, 6.0);

        byte[] expectedStates = new byte[] { 1, 1, 1, 1, 1, 1 };
        float[] expectedSet = new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float element) {
                int index = hash.indexOf(element);
                assertEquals(index, hash.indexOf(element));
                assertEquals(element, hash._set[index]);
                return true;
            }
        };

        hash.forEachProcedure(procedure);
        assertArrayEquals(expectedStates, hash._states);
        assertArrayEquals(expectedSet, hash._set);
    }

    @Test
    public void testRemoveAtHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);
        hash.insert(4.0f, 4.0);
        hash.insert(5.0f, 5.0);
        hash.insert(6.0f, 6.0);

        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
        assertEquals(5, hash.size());

        byte[] expectedStates = new byte[] { 1, 1, 1, 1, 0, 1 };
        float[] expectedSet = new float[] { 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float element) {
                int index = hash.indexOf(element);
                assertEquals(index, hash.indexOf(element));
                assertEquals(element, hash._set[index]);
                return true;
            }
        };

        hash.forEachProcedure(procedure);
        assertArrayEquals(expectedStates, hash._states);
        assertArrayEquals(expectedSet, hash._set);
    }

    @Test
    public void testIndexHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);
        hash.insert(4.0f, 4.0);
        hash.insert(5.0f, 5.0);
        hash.insert(6.0f, 6.0);

        assertEquals(3, hash.index(3.0f));
        assertEquals(-1, hash.index(7.0f));
    }

    @Test
    public void testIndexRehashedHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);
        hash.insert(4.0f, 4.0);
        hash.insert(5.0f, 5.0);
        hash.insert(6.0f, 6.0);
        hash.insert(7.0f, 7.0);

        assertEquals(-1, hash.index(6.5f));
    }

    @Test
    public void testInsertKeyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.insert(1.0f);
        hash.insert(2.0f);
        hash.insert(3.0f);
        hash.insert(4.0f);
        hash.insert(5.0f);
        hash.insert(6.0f);
        hash.insert(7.0f);

        assertEquals(7, hash.size());

        byte[] expectedStates = new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 };
        float[] expectedSet = new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f };

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float element) {
                int index = hash.indexOf(element);
                assertEquals(index, hash.indexOf(element));
                assertEquals(element, hash._set[index]);
                return true;
            }
        };

        hash.forEachProcedure(procedure);
        assertArrayEquals(expectedStates, hash._states);
        assertArrayEquals(expectedSet, hash._set);
    }

    @Test
    public void testWriteReadExternalHash() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);

        TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
        hash.insert(1.0f, 1.0);
        hash.insert(2.0f, 2.0);
        hash.insert(3.0f, 3.0);
        hash.insert(4.0f, 4.0);
        hash.insert(5.0f, 5.0);
        hash.insert(6.0f, 6.0);

        hash.writeExternal(out);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ObjectInput in = new ObjectInput(input);

        TFloatDoubleHash hash2 = new TFloatDoubleHash();
        hash2.readExternal(in);

        byte[] expectedStates = new byte[] { 1, 1, 1, 1, 1, 1 };
        float[] expectedSet = new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float element) {
                int index = hash2.indexOf(element);
                assertEquals(index, hash2.indexOf(element));
                assertEquals(element, hash2._set[index]);
                return true;
            }
        };

        hash2.forEachProcedure(procedure);
        assertArrayEquals(expectedStates, hash2._states);
        assertArrayEquals(expectedSet, hash2._set);
    }
}