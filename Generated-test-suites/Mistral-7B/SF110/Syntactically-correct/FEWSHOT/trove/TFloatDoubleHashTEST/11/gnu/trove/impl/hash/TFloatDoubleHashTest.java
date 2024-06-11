package org.apache.commons.cli;

import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testInitialization() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(0, hashMap.size());
        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());
        assertNull(hashMap._set);

        TFloatDoubleHash hashMapWithCapacity = new TFloatDoubleHash(10);
        assertEquals(0, hashMapWithCapacity.size());
        assertNotNull(hashMapWithCapacity._set);
        assertEquals(10, hashMapWithCapacity._set.length);

        TFloatDoubleHash hashMapWithCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.75f);
        assertEquals(0, hashMapWithCapacityAndLoadFactor.size());
        assertNotNull(hashMapWithCapacityAndLoadFactor._set);
        assertEquals(10, hashMapWithCapacityAndLoadFactor._set.length);

        TFloatDoubleHash hashMapWithCapacityLoadFactorAndInitialValue = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
        assertEquals(0, hashMapWithCapacityLoadFactorAndInitialValue.size());
        assertNotNull(hashMapWithCapacityLoadFactorAndInitialValue._set);
        assertEquals(10, hashMapWithCapacityLoadFactorAndInitialValue._set.length);
        assertEquals(0.0f, hashMapWithCapacityLoadFactorAndInitialValue.getNoEntryKey());
        assertEquals(0.0, hashMapWithCapacityLoadFactorAndInitialValue.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        assertEquals(5, hashMap.size());
        assertNotNull(hashMap._set);
        assertEquals(5, hashMap._set.length);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);
        hashMap.insertKey(4.0f);
        hashMap.insertKey(5.0f);

        assertTrue(hashMap.contains(1.0f));
        assertTrue(hashMap.contains(2.0f));
        assertTrue(hashMap.contains(3.0f));
        assertTrue(hashMap.contains(4.0f));
        assertTrue(hashMap.contains(5.0f));

        assertFalse(hashMap.contains(6.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);
        hashMap.insertKey(4.0f);
        hashMap.insertKey(5.0f);

        int count = 0;
        hashMap.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);
        hashMap.insertKey(4.0f);
        hashMap.insertKey(5.0f);

        hashMap.removeAt(2);

        assertEquals(4, hashMap.size());
        assertFalse(hashMap.contains(3.0f));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtInvalidIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);
        hashMap.insertKey(4.0f);
        hashMap.insertKey(5.0f);

        hashMap.removeAt(5);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);
        hashMap.insertKey(4.0f);
        hashMap.insertKey(5.0f);

        assertEquals(1, hashMap.index(1.0f));
        assertEquals(2, hashMap.index(2.0f));
        assertEquals(3, hashMap.index(3.0f));
        assertEquals(4, hashMap.index(4.0f));
        assertEquals(5, hashMap.index(5.0f));

        assertEquals(-1, hashMap.index(6.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);

        assertEquals(0, hashMap.size());
        assertEquals(0.0f, hashMap.getNoEntryKey());
        assertEquals(0.0, hashMap.getNoEntryValue());

        hashMap.insertKey(1.0f);
        assertEquals(1, hashMap.size());
        assertTrue(hashMap.contains(1.0f));

        hashMap.insertKey(2.0f);
        assertEquals(2, hashMap.size());
        assertTrue(hashMap.contains(2.0f));

        hashMap.insertKey(1.0f); // Attempting to insert again, should not change the size
        assertEquals(2, hashMap.size());

        hashMap.insertKey(3.0f);
        assertEquals(3, hashMap.size());
        assertTrue(hashMap.contains(3.0f));

        hashMap.insertKey(1.0f); // Attempting to insert again, should not change the size
        assertEquals(3, hashMap.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);

        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);
        hashMap.insertKey(4.0f);
        hashMap.insertKey(5.0f);

        hashMap.insertKey(6.0f); // Should rehash
        assertEquals(6, hashMap.size());
        assertTrue(hashMap.contains(6.0f));

        hashMap.insertKey(7.0f); // Should rehash
        assertEquals(7, hashMap.size());
        assertTrue(hashMap.contains(7.0f));
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);
        hashMap.insertKey(4.0f);
        hashMap.insertKey(5.0f);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        hashMap.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TFloatDoubleHash deserializedHashMap = (TFloatDoubleHash) objectInput.readObject();
        objectInput.close();

        assertEquals(5, deserializedHashMap.size());
        assertTrue(deserializedHashMap.contains(1.0f));
        assertTrue(deserializedHashMap.contains(2.0f));
        assertTrue(deserializedHashMap.contains(3.0f));
        assertTrue(deserializedHashMap.contains(4.0f));
        assertTrue(deserializedHashMap.contains(5.0f));
    }
}