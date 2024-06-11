package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertTrue(hash.isEmpty());
        assertNull(hash.getNoEntryValue());
        assertNotNull(hash.getNoEntryKey());
        assertEquals(10, hash.size());
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testBasicFunctionality() {
        TFloatCharHash hash = new TFloatCharHash(10);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        assertTrue(hash.containsKey(1.0f));
        assertTrue(hash.containsValue('a'));
        assertEquals('a', hash.get('a'));
        assertEquals(3, hash.size());

        hash.remove('a');
        assertFalse(hash.containsKey(1.0f));
        assertFalse(hash.containsValue('a'));
        assertEquals(2, hash.size());

        hash.clear();
        assertTrue(hash.isEmpty());
        assertNull(hash.getNoEntryValue());
        assertNotNull(hash.getNoEntryKey());
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testRehashing() {
        TFloatCharHash hash = new TFloatCharHash(10);

        for (int i = 0; i < 11; i++) {
            hash.put(i, (char) i);
        }

        assertFalse(hash.isEmpty());
        assertEquals(11, hash.size());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                hash.remove(value);
                return true;
            }
        };

        hash.forEach(procedure);

        assertTrue(hash.isEmpty());
        assertNull(hash.getNoEntryValue());
        assertNotNull(hash.getNoEntryKey());
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(outputStream);
        hash.writeExternal(output);
        output.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput input = new ObjectInputStream(inputStream);
        TFloatCharHash deserializedHash = (TFloatCharHash) input.readObject();
        input.close();

        assertTrue(deserializedHash.containsKey(1.0f));
        assertTrue(deserializedHash.containsValue('a'));
        assertEquals('a', deserializedHash.get('a'));
        assertEquals(3, deserializedHash.size());
    }
}