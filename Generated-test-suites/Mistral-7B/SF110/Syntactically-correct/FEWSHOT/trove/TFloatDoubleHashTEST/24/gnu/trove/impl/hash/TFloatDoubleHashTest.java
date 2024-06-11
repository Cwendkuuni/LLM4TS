package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

    @Test
    public void testInitialization() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        assertEquals(0, hashTable.size());
        assertNull(hashTable.getNoEntryValue());
        assertEquals(0.0f, hashTable.getNoEntryKey(), 0.001f);

        TFloatDoubleHash hashTable1 = new TFloatDoubleHash(10);
        assertEquals(0, hashTable1.size());
        assertNull(hashTable1.getNoEntryValue());
        assertEquals(0.0f, hashTable1.getNoEntryKey(), 0.001f);

        TFloatDoubleHash hashTable2 = new TFloatDoubleHash(10, 0.75f);
        assertEquals(0, hashTable2.size());
        assertNull(hashTable2.getNoEntryValue());
        assertEquals(0.0f, hashTable2.getNoEntryKey(), 0.001f);

        TFloatDoubleHash hashTable3 = new TFloatDoubleHash(10, 0.75f, 1.0f, 0.0d);
        assertEquals(0, hashTable3.size());
        assertEquals(1.0f, hashTable3.getNoEntryKey());
        assertEquals(0.0d, hashTable3.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        assertEquals(0, hashTable.size());
        int capacity = hashTable.setUp(10);
        assertArrayEquals(new float[10], hashTable._set);
        assertEquals(10, capacity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCapacity() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        hashTable.setUp(-10);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        assertFalse(hashTable.contains(1.0f));

        hashTable.insertKey(1.0f);
        assertTrue(hashTable.contains(1.0f));

        hashTable.removeAt(0);
        assertFalse(hashTable.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float element) {
                assertEquals(element, 0.0f, 0.001f);
                return true;
            }
        };
        hashTable.forEach(procedure);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        hashTable.insertKey(1.0f);
        hashTable.insertKey(2.0f);
        hashTable.insertKey(3.0f);

        assertEquals(3, hashTable.size());

        hashTable.removeAt(1);
        assertEquals(2, hashTable.size());
        assertFalse(hashTable.contains(2.0f));
        assertTrue(hashTable.contains(1.0f));
        assertTrue(hashTable.contains(3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();

        int index = hashTable.insertKey(1.0f);
        assertEquals(1, hashTable.size());
        assertEquals(1.0f, hashTable._set[index], 0.001f);
        assertEquals(0, hashTable._states[index]);

        index = hashTable.insertKey(2.0f);
        assertEquals(2, hashTable.size());
        assertEquals(2.0f, hashTable._set[index], 0.001f);
        assertEquals(1, hashTable._states[index]);

        index = hashTable.insertKey(1.0f);
        assertEquals(3, hashTable.size());
        assertEquals(1.0f, hashTable._set[index], 0.001f);
        assertEquals(1, hashTable._states[index]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();

        int index = hashTable.XinsertKey(1.0f);
        assertEquals(1, hashTable.size());
        assertEquals(1.0f, hashTable._set[index], 0.001f);
        assertEquals(1, hashTable._states[index]);

        index = hashTable.XinsertKey(2.0f);
        assertEquals(2, hashTable.size());
        assertEquals(2.0f, hashTable._set[index], 0.001f);
        assertEquals(1, hashTable._states[index]);

        index = hashTable.XinsertKey(1.0f);
        assertEquals(3, hashTable.size());
        assertEquals(1.0f, hashTable._set[index], 0.001f);
        assertEquals(1, hashTable._states[index]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        hashTable.insertKey(1.0f);
        hashTable.insertKey(2.0f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hashTable.writeExternal(out);
        out.close();

        byte[] bytes = baos.toByteArray();
        TFloatDoubleHash hashTable1 = new TFloatDoubleHash();
        hashTable1.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

        assertEquals(2, hashTable1.size());
        assertEquals(1.0f, hashTable1._set[0], 0.001f);
        assertEquals(2.0f, hashTable1._set[1], 0.001f);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hashTable = new TFloatDoubleHash();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hashTable.writeExternal(out);
        out.close();

        byte[] bytes = baos.toByteArray();
        TFloatDoubleHash hashTable1 = new TFloatDoubleHash();
        hashTable1.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

        assertEquals(0, hashTable1.size());
        assertEquals(0.0f, hashTable1.getNoEntryKey(), 0.001f);
        assertEquals(0.0d, hashTable1.getNoEntryValue());

        hashTable.insertKey(1.0f);
        hashTable.insertKey(2.0f);
        hashTable.writeExternal(out);
        out.close();

        bytes = baos.toByteArray();
        hashTable1.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

        assertEquals(2, hashTable1.size());
        assertEquals(1.0f, hashTable1._set[0], 0.001f);
        assertEquals(2.0f, hashTable1._set[1], 0.001f);
    }
}