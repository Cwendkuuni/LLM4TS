package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testEmptyHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertTrue(hash.isEmpty());
        assertNull(hash.getNoEntryValue());
        assertNull(hash.getNoEntryKey());
    }

    @Test
    public void testBasicHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float key = 0.1f;
        hash.put(key, 1.0);
        assertTrue(hash.containsKey(key));
        assertTrue(hash.containsValue(1.0));
        assertEquals(1.0, hash.get(key), 0.0);
        assertTrue(hash.size() == 1);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            key = random.nextFloat();
            hash.put(key, key * 2.0);
        }
        assertTrue(hash.size() == 10);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                System.out.println("Key: " + value);
                return true;
            }
        };
        hash.forEach(procedure);
    }

    @Test
    public void testPutAndGet() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        hash.put(1.1f, 1.0);
        hash.put(2.2f, 2.0);
        hash.put(3.3f, 3.0);

        assertTrue(hash.containsKey(1.1f));
        assertTrue(hash.containsKey(2.2f));
        assertTrue(hash.containsKey(3.3f));

        assertEquals(1.0, hash.get(1.1f), 0.0);
        assertEquals(2.0, hash.get(2.2f), 0.0);
        assertEquals(3.0, hash.get(3.3f), 0.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(3);
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            hash.put(random.nextFloat(), random.nextFloat());
        }
    }

    @Test
    public void testSerialization() throws Exception {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put(1.1f, 1.0);
        hash.put(2.2f, 2.0);
        hash.put(3.3f, 3.0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInput(bais);
        TFloatDoubleHash deserializedHash = new TFloatDoubleHash();
        deserializedHash.readExternal(in);
        in.close();

        assertEquals(3, deserializedHash.size());
        assertTrue(deserializedHash.containsKey(1.1f));
        assertTrue(deserializedHash.containsKey(2.2f));
        assertTrue(deserializedHash.containsKey(3.3f));
    }
}