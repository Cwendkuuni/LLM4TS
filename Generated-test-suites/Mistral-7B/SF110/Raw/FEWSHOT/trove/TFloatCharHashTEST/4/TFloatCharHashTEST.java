package org.apache.commons.cli;

import gnu.trove.procedure.TFloatProcedure;
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(key, 1.0f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        int index = hash.insertKey(1.0f);
        assertEquals(index, 0);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        hash.insertKey(2.0f);
        index = hash.insertKey(1.0f);
        assertEquals(index, -1);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(1.0f); // should rehash
        int index = hash.insertKey(4.0f);
        assertEquals(index, 3);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
    }

    @Test
    public void testWriteReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(1.0f);
        hash.writeExternal(objectOutputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TFloatCharHash hashRead = (TFloatCharHash) objectInputStream.readObject();
        assertTrue(hashRead.contains(1.0f));
    }
}