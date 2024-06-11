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
    public void testTFloatDoubleHashConstruction() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        TFloatDoubleHash hashWithInitialCapacity = new TFloatDoubleHash(10);
        TFloatDoubleHash hashWithInitialCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.75f);
        TFloatDoubleHash hashWithInitialCapacityLoadFactorAndKeys = new TFloatDoubleHash(10, 0.75f, 0.1f, 0.0);

        assertEquals(0, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());

        assertEquals(10, hashWithInitialCapacity.getNoEntryKey());
        assertEquals(0.0, hashWithInitialCapacity.getNoEntryValue());

        assertEquals(10, hashWithInitialCapacityAndLoadFactor.getNoEntryKey());
        assertEquals(0.0, hashWithInitialCapacityAndLoadFactor.getNoEntryValue());

        assertEquals(10, hashWithInitialCapacityLoadFactorAndKeys.getNoEntryKey());
        assertEquals(0.0, hashWithInitialCapacityLoadFactorAndKeys.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.setUp(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUpNegativeCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(-5);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
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
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtOutOfBounds() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.removeAt(10);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);

        assertEquals(3, hash.indexRehashed(1.0f, 3, 1, (byte) 1));
        assertEquals(1, hash.indexRehashed(2.0f, 1, 1, (byte) 1));
        assertEquals(0, hash.indexRehashed(3.0f, 0, 1, (byte) 1));
        assertEquals(-1, hash.indexRehashed(5.0f, 1, 1, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
        hash.insertKey(2.0f);
        assertEquals(1, hash.index(2.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatDoubleHash hash = new TFloatDoubleHash(2);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.XinsertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
        hash.XinsertKey(2.0f);
        assertEquals(1, hash.index(2.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testXinsertKeyFull() {
        TFloatDoubleHash hash = new TFloatDoubleHash(2);
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);
        hash.XinsertKey(3.0f);
    }

    @Test
    public void testWriteExternalReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

        hash.writeExternal(objectOutput);
        objectOutput.close();
        TFloatDoubleHash hash2 = (TFloatDoubleHash) objectInput.readObject();
        objectInput.close();

        assertEquals(2, hash2.size());
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
        assertEquals(1.0f, hash2.get(0), 0.0f);
        assertEquals(2.0f, hash2.get(1), 0.0f);
    }

    @Test
    public void testConsumeFreeSlot() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.consumeFreeSlot = true;
        hash.insertKey(1.0f);
        assertEquals(1, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals(0.0, hash.getNoEntryValue());
    }
}