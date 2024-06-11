package org.apache.commons.cli;

import gnu.trove.procedure.TFloatProcedure;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValuesConstructor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'X');
        assertEquals(0, hash.size());
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals('X', hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 1.0f);
                return true;
            }
        };
        hash.forEach(procedure);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash1() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
    }

    @Test
    public void testInsertKeyRehash2() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.removeAt(2);
        float key = 3.5f;
        int index = hash.insertKey(key);
        assertEquals(4, hash.size());
        assertEquals(key, hash._set[index]);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.removeAt(2);
        float key = 3.5f;
        int index = hash.index(key);
        assertEquals(-1, index);
        index = hash.indexRehashed(key, 0, 1, (byte) 0);
        assertEquals(-1, index);
        hash.removeAt(0);
        index = hash.index(1.0f);
        assertEquals(0, index);
        index = hash.indexRehashed(1.0f, 0, 1, (byte) 0);
        assertEquals(0, index);
        index = hash.indexRehashed(1.0f, 0, 1, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.removeAt(2);
        float key = 3.5f;
        int index = hash.insertKey(key);
        assertEquals(5, hash.size());
        assertEquals(key, hash._set[index]);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);
        hash.XinsertKey(3.0f);
        hash.XinsertKey(4.0f);
        hash.removeAt(2);
        float key = 3.5f;
        int index = hash.insertKey(key);
        assertEquals(5, hash.size());
        assertEquals(key, hash._set[index]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(out);
        hash.writeExternal(objectOutput);
        byte[] bytes = out.toByteArray();
        TFloatCharHash hashFromBytes = new TFloatCharHash();
        hashFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(3, hashFromBytes.size());
        assertEquals(1.0f, hashFromBytes._set[0]);
        assertEquals(2.0f, hashFromBytes._set[1]);
        assertEquals(3.0f, hashFromBytes._set[2]);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(out);
        hash.writeExternal(objectOutput);
        byte[] bytes = out.toByteArray();
        TFloatCharHash hashFromBytes = new TFloatCharHash();
        hashFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(3, hashFromBytes.size());
        assertEquals(1.0f, hashFromBytes._set[0]);
        assertEquals(2.0f, hashFromBytes._set[1]);
        assertEquals(3.0f, hashFromBytes._set[2]);
    }
}