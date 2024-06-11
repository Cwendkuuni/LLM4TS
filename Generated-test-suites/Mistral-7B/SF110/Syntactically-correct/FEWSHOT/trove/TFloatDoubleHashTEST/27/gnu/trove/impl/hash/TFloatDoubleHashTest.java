package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertFalse(hash.contains(new Float(0.1f)));
        assertEquals(0, hash.size());
    }

    @Test
    public void testBasicHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(new Float(0.1f), 1.0);
        assertTrue(hash.contains(new Float(0.1f)));
        assertEquals(1, hash.size());

        hash.put(new Float(0.2f), 2.0);
        assertTrue(hash.contains(new Float(0.1f)));
        assertTrue(hash.contains(new Float(0.2f)));
        assertEquals(2, hash.size());

        hash.put(new Float(0.1f), 3.0);
        assertFalse(hash.contains(new Float(0.1f), 1.0));
        assertTrue(hash.contains(new Float(0.1f), 3.0));
        assertEquals(2, hash.size());
    }

    @Test
    public void testRemoveKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(new Float(0.1f), 1.0);
        hash.put(new Float(0.2f), 2.0);
        hash.put(new Float(0.1f), 3.0);

        assertEquals(2, hash.size());
        hash.remove(new Float(0.1f));
        assertEquals(1, hash.size());

        assertFalse(hash.contains(new Float(0.1f)));
        assertTrue(hash.contains(new Float(0.2f)));
    }

    @Test
    public void testRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        Random random = new Random();

        for (int i = 0; i < 15; i++) {
            hash.put(new Float(random.nextFloat()), random.nextDouble());
        }

        assertEquals(15, hash.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.put(new Float(0.1f), 1.0);
        hash.put(new Float(0.2f), 2.0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        hash.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(inputStream);
        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInput.readObject();
        objectInput.close();

        assertEquals(hash.size(), deserializedHash.size());
        assertTrue(hash.contains(new Float(0.1f)));
        assertTrue(hash.contains(new Float(0.2f)));
        assertTrue(deserializedHash.contains(new Float(0.1f)));
        assertTrue(deserializedHash.contains(new Float(0.2f)));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.put(new Float(1.0f), 1.0);
        hash.put(new Float(2.0f), 2.0);
        hash.put(new Float(3.0f), 3.0);

        int count = 0;
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(3, count);
    }
}