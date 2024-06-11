package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash.capacity());
        hash.setUp(5);
        assertEquals(5, hash.capacity());
        assertNotNull(hash._set);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertFalse(hash.contains(1.0f));
        hash.put(1.0f, 'a');
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(10);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key == 1.0f;
            }
        };
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(1.0f, 'c');
        assertTrue(hash.forEach(procedure));
        assertEquals(1, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(1.0f, 'c');
        assertEquals('c', hash.remove(1.0f));
        assertTrue(hash.containsKey(1.0f));
        assertFalse(hash.containsKey(2.0f));
        assertEquals('a', hash.get('a'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatCharHash hash = new TFloatCharHash(1);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyNullKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(null, 'b');
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(1.0f, 'c');
        assertEquals(1, hash.index(1.0f));
        assertEquals(-1, hash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash(3);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        assertEquals(1, hash.indexRehashed(1.0f, 1, 1, (byte) 1));
        assertEquals(-1, hash.indexRehashed(4.0f, 1, 1, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(1.0f, 'c');
        hash.insertKeyAt(2, 3.0f);
        assertEquals('b', hash.remove(2.0f));
        assertEquals('c', hash.get(3.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(1.0f, 'c');
        hash.XinsertKey(3.0f);
        assertEquals('a', hash.remove(1.0f));
        assertEquals('b', hash.get(1.0f));
        assertEquals('c', hash.get(2.0f));
        assertEquals('a', hash.get(3.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.writeExternal(out);
        TFloatCharHash hash2 = new TFloatCharHash(10);
        hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));
        assertEquals(hash.size(), hash2.size());
        assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), hash2.getNoEntryValue());
        for (float key : hash.keySet()) {
            assertEquals(hash.get(key), hash2.get(key));
        }
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.writeExternal(out);
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteInput);
        TFloatCharHash hash2 = (TFloatCharHash) in.readObject();
        assertEquals(hash.size(), hash2.size());
        assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), hash2.getNoEntryValue());
        for (float key : hash.keySet()) {
            assertEquals(hash.get(key), hash2.get(key));
        }
    }
}