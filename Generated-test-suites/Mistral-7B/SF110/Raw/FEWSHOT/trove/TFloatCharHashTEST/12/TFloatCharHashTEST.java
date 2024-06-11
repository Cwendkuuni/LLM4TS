package org.apache.commons.cli;

import gnu.trove.procedure.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHashDefaultConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
    }

    @Test
    public void testTFloatCharHashWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(10, hash.size());
    }

    @Test
    public void testTFloatCharHashWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 0.0f, '\0');;
        assertEquals(10, hash.size());
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey());
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f, 0.001f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.removeAt(0);
        assertEquals(1, hash.size());
        assertFalse(hash.containsKey(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertEquals(1, hash.size());
        assertTrue(hash.containsKey(1.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWithNoFreeOrRemovedSlots() {
        TFloatCharHash hash = new TFloatCharHash();
        for (int i = 0; i < hash.size() * 2; i++) {
            hash.put(i, (char) i);
        }
        hash.insertKey(hash.size() * 2);
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');

        hash.writeExternal(objectOutputStream);
        objectOutputStream.close();

        TFloatCharHash deserializedHash = new TFloatCharHash();
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        deserializedHash.readExternal(objectInputStream);

        assertEquals(hash.size(), deserializedHash.size());
        for (int i = 0; i < hash.size(); i++) {
            assertEquals(hash.keyAt(i), deserializedHash.keyAt(i));
        }

        objectInputStream.close();
    }
}