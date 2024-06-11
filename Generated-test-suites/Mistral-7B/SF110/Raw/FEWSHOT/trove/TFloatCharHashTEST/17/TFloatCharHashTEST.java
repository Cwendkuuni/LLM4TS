package org.apache.commons.cli; // Assuming you want to test the TFloatCharHash class in the given package, adjust the package name if necessary.

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatProcedure;
import gnu.trove.impl.hash.TPrimitiveHash;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testInitialization() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());

        TFloatCharHash hash2 = new TFloatCharHash(10);
        assertEquals(10, hash2.capacity());
        assertEquals(0, hash2.size());
        assertNull(hash2.get(_setKey(0.0f)));

        TFloatCharHash hash3 = new TFloatCharHash(10, 0.5f);
        assertEquals(10, hash3.capacity());
        assertEquals(0.5f, hash3.loadFactor(), 0.01f);
        assertEquals(0, hash3.size());
        assertNull(hash3.get(_setKey(0.0f)));
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(_setKey(1.0f), 'a');
        hash.put(_setKey(2.0f), 'b');
        hash.put(_setKey(3.0f), 'c');
        hash.put(_setKey(4.0f), 'd');
        hash.put(_setKey(5.0f), 'e');

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));

        assertFalse(hash.contains(0.0f));
        assertFalse(hash.contains(6.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(_setKey(1.0f), 'a');
        hash.put(_setKey(2.0f), 'b');
        hash.put(_setKey(3.0f), 'c');
        hash.put(_setKey(4.0f), 'd');
        hash.put(_setKey(5.0f), 'e');

        char result = '\0';
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                result = (char) key;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals('e', result);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(_setKey(1.0f), 'a');
        hash.put(_setKey(2.0f), 'b');
        hash.put(_setKey(3.0f), 'c');
        hash.put(_setKey(4.0f), 'd');
        hash.put(_setKey(5.0f), 'e');

        assertEquals('e', hash.get(5.0f));

        hash.removeAt(5);

        assertNull(hash.get(5.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(_setKey(1.0f), 'a');
        hash.put(_setKey(2.0f), 'b');
        hash.put(_setKey(3.0f), 'c');
        hash.put(_setKey(4.0f), 'd');
        hash.put(_setKey(5.0f), 'e');

        assertEquals(4, hash.index(4.0f));
        assertEquals(-1, hash.index(10.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(_setKey(1.0f), 'a');
        hash.put(_setKey(2.0f), 'b');
        hash.put(_setKey(3.0f), 'c');
        hash.put(_setKey(4.0f), 'd');
        hash.put(_setKey(5.0f), 'e');
        hash.put(_setKey(6.0f), 'f');

        assertEquals('f', hash.get(6.0f));
        assertEquals(6, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFull() {
        TFloatCharHash hash = new TFloatCharHash(1);

        hash.put(_setKey(1.0f), 'a');

        hash.put(_setKey(2.0f), 'b');
    }

    @Test
    public void testWriteExternalAndReadExternal() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

        TFloatCharHash hash = new TFloatCharHash(5);
        hash.put(_setKey(1.0f), 'a');
        hash.put(_setKey(2.0f), 'b');
        hash.put(_setKey(3.0f), 'c');

        objectStream.writeObject(hash);
        objectStream.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);

        TFloatCharHash hash2 = (TFloatCharHash) objectInput.readObject();
        objectInput.close();

        assertEquals(hash.size(), hash2.size());
        assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey());
        assertEquals(hash.getNoEntryValue(), hash2.getNoEntryValue());

        assertEquals('c', hash2.get(3.0f));
    }

    private float _setKey(float key) {
        return key;
    }
}