package org.apache.commons.cli;

import gnu.trove.procedure.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(5, hash.size());
        assertArrayEquals(new float[5], hash._set);
        assertNotNull(hash._states);
        assertEquals(5, hash._states.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertFalse(hash.contains(0.5f));
        hash.insertKey(0.5f);
        assertTrue(hash.contains(0.5f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(0.5f);
        hash.insertKey(1.0f);

        int count = 0;
        TFloatProcedure procedure = (float value) -> {
            count++;
            return true;
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(2, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(0.5f);
        hash.insertKey(1.0f);
        hash.removeAt(1);
        assertFalse(hash.contains(1.0f));
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(0.5f);
        hash.insertKey(1.0f);
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertEquals(0, hash.insertKey(0.5f));
        assertTrue(hash.contains(0.5f));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.insertKey(0.5f);
        hash.insertKey(1.0f);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(0.5f);
        hash.insertKey(1.0f);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.writeExternal(oos);

        byte[] byteData = baos.toByteArray();
        TFloatCharHash hash2 = new TFloatCharHash(5);
        hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteData)));

        assertEquals(2, hash2.size());
        assertTrue(hash2.contains(0.5f));
        assertTrue(hash2.contains(1.0f));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.writeExternal(oos);
        byte[] byteData = baos.toByteArray();

        TFloatCharHash hash2 = new TFloatCharHash(5);
        hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteData)));

        assertEquals(0.0f, hash.getNoEntryKey(), hash2.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue(), hash2.getNoEntryValue());
        assertEquals(2, hash.size());
        assertEquals(2, hash2.size());
        assertTrue(hash.containsAll(hash2));
    }
}