package org.apache.commons.cli;

import gnu.trove.procedure.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testTFloatCharHash_empty() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.isEmpty());
        assertEquals(5, hash.size());
    }

    @Test
    public void testTFloatCharHash_basic() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f, 'a');
        hash.insertKey(2.0f, 'b');
        hash.insertKey(1.0f, 'c'); // should fail as key already exists

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
        assertEquals('c', hash.get(1.0f));
        assertEquals('b', hash.get(2.0f));
        assertNull(hash.get(3.0f));
        assertEquals(2, hash.size());

        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
        assertEquals(1, hash.size());
    }

    @Test
    public void testTFloatCharHash_loadFactor() {
        TFloatCharHash hash = new TFloatCharHash(3, 0.5f);

        for (int i = 0; i < 6; i++) {
            hash.insertKey(i, (char) i);
        }

        assertTrue(hash.contains(0.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertEquals(6, hash.size());

        // Test that hash resizes when necessary
        for (int i = 6; i <= 12; i++) {
            hash.insertKey(i, (char) i);
        }

        assertTrue(hash.contains(6.0f));
        assertTrue(hash.contains(7.0f));
        assertTrue(hash.contains(8.0f));
        assertTrue(hash.contains(9.0f));
        assertTrue(hash.contains(10.0f));
        assertTrue(hash.contains(11.0f));
        assertTrue(hash.contains(12.0f));
        assertEquals(12, hash.size());
    }

    @Test
    public void testTFloatCharHash_forEach() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.insertKey(1.0f, 'a');
        hash.insertKey(2.0f, 'b');
        hash.insertKey(1.0f, 'c');

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                assertEquals(key, 1.0f, 0.0f);
                count++;
                return true;
            }
        });

        assertEquals(2, count);
    }

    @Test
    public void testTFloatCharHash_writeAndReadExternal() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f, 'a');
        hash.insertKey(2.0f, 'b');

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        out.close();

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatCharHash hashCopy = (TFloatCharHash) in.readObject();
        in.close();

        assertEquals(hash.size(), hashCopy.size());
        assertEquals(hash.get(1.0f), hashCopy.get(1.0f));
        assertEquals(hash.get(2.0f), hashCopy.get(2.0f));
    }
}