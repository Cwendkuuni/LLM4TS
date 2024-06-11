package org.apache.commons.cli;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertFalse(hash.contains(1.0f));
        assertEquals(0, hash.size());
    }

    @Test
    public void testHashWithOneEntry() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertEquals(1, hash.size());
    }

    @Test
    public void testHashWithMultipleEntries() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertEquals(3, hash.size());
    }

    @Test
    public void testHashRehash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.consumeFreeSlot = true;
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.insertKey(4.0f);
        hash.insertKey(5.0f);
        hash.insertKey(6.0f);
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertTrue(hash.contains(4.0f));
        assertTrue(hash.contains(5.0f));
        assertTrue(hash.contains(6.0f));
        assertEquals(6, hash.size());
    }

    @Test
    public void testHashWriteRead() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInput(byteInput);
        TFloatCharHash hashFromDisk = (TFloatCharHash) in.readObject();
        in.close();

        assertTrue(hashFromDisk.contains(1.0f));
        assertTrue(hashFromDisk.contains(2.0f));
        assertTrue(hashFromDisk.contains(3.0f));
        assertEquals(3, hashFromDisk.size());
    }

    @Test
    public void testProcedure() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(3, count);
    }
}