package org.apache.commons.cli; // Assuming the test class is in the same package as TFloatCharHash

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

    @Test
    public void testInitialization() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());

        hash = new TFloatCharHash(10);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());

        hash = new TFloatCharHash(10, 0.5f);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey());
        assertEquals('\0', hash.getNoEntryValue());

        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'A');
        assertEquals(10, hash.size());
        assertEquals(1.0f, hash.getNoEntryKey());
        assertEquals('A', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash(10);

        assertFalse(hash.contains(1.0f));

        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);

        int count = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);

        hash.removeAt(2);
        assertEquals(4, hash.size());
        assertFalse(hash.contains(3.0f));
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);

        assertEquals(2, hash.index(2.0f));
        assertEquals(-1, hash.index(6.0f));
    }

    @Test
    public void testWriteReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);

        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);

        out.writeObject(hash);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInputStream(inputStream);

        TFloatCharHash readHash = (TFloatCharHash) in.readObject();

        assertEquals(5, readHash.size());
        assertTrue(readHash.contains(1.0f));
        assertTrue(readHash.contains(2.0f));
        assertTrue(readHash.contains(3.0f));
        assertTrue(readHash.contains(4.0f));
        assertTrue(readHash.contains(5.0f));
    }
}