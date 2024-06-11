package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testHashCreation() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertFalse(hash.isEmpty());

        TFloatCharHash hashLoadFactor = new TFloatCharHash(10, 0.5f);
        assertEquals(20, hashLoadFactor.size());
        assertFalse(hashLoadFactor.isEmpty());

        TFloatCharHash hashNoEntry = new TFloatCharHash(10, 0.5f, 0.1f, 'A');
        assertEquals(20, hashNoEntry.size());
        assertFalse(hashNoEntry.isEmpty());
    }

    @Test
    public void testGetNoEntryKeyAndValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());

        TFloatCharHash hashWithValues = new TFloatCharHash(10, 0.5f, 1.2f, 'B');
        assertEquals(1.2f, hashWithValues.getNoEntryKey());
        assertEquals('B', hashWithValues.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertFalse(hash.isEmpty());

        byte[] states = hash._states;
        float[] set = hash._set;
        assertEquals(0, states[0]);
        assertEquals(0.0f, set[0], 0.001f);

        assertEquals(0, states[1]);
        assertEquals(Float.NaN, set[1], 0.001f);

        assertEquals(1, states[2]);
        assertEquals(1.2f, set[2], 0.001f);

        assertEquals(1, states[3]);
        assertEquals(2.3f, set[3], 0.001f);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.2f);
        assertTrue(hash.contains(1.2f));
        hash.remove(1.2f);
        assertFalse(hash.contains(1.2f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.2f);
        hash.insertKey(2.3f);

        int count = 0;
        TFloatCharHash hashInstance = hash;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        });

        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.2f);
        hash.insertKey(2.3f);
        hash.removeAt(1);

        byte[] states = hash._states;
        float[] set = hash._set;
        assertEquals(1, states[1]);
        assertEquals(2.3f, set[1], 0.001f);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);

        int index = hash.index(Float.NaN);
        assertEquals(-1, index);

        hash.insertKey(1.2f);
        index = hash.index(1.2f);
        assertEquals(1, index);

        hash.removeAt(1);
        index = hash.index(1.2f);
        assertEquals(-1, index);

        hash.insertKey(1.2f);
        hash.insertKey(2.3f);
        index = hash.index(1.2f);
        assertEquals(0, index);

        index = hash.index(2.3f);
        assertEquals(1, index);

        hash.removeAt(1);
        index = hash.index(2.3f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);

        int index = hash.insertKey(1.2f);
        assertEquals(0, index);

        index = hash.insertKey(1.2f);
        assertEquals(-1, index);

        hash.insertKey(2.3f);
        assertEquals(1, hash.size());

        byte[] states = hash._states;
        float[] set = hash._set;
        assertEquals(1, states[1]);
        assertEquals(2.3f, set[1], 0.001f);
    }

    @Test
    public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.2f);
        hash.insertKey(2.3f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
        TFloatCharHash readHash = new TFloatCharHash();
        readHash.readExternal(objectInput);
        objectInput.close();

        assertEquals(2, readHash.size());
        assertEquals(1.2f, readHash._set[0], 0.001f);
        assertEquals(2.3f, readHash._set[1], 0.001f);
    }

}