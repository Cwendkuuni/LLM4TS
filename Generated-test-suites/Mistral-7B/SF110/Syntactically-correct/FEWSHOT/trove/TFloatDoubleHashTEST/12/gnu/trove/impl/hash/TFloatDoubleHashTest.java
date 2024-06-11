package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatDoubleHashTest {

    @Test
    public void testTFloatDoubleHashBasics() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        assertTrue(hash.isEmpty());
        assertEquals(5, hash.size());

        hash.put((float) 1.0, 1.0);
        hash.put((float) 2.0, 2.0);
        hash.put((float) 3.0, 3.0);
        hash.put((float) 4.0, 4.0);
        hash.put((float) 5.0, 5.0);
        assertTrue(hash.containsKey((float) 1.0));
        assertTrue(hash.containsKey((float) 2.0));
        assertTrue(hash.containsKey((float) 3.0));
        assertTrue(hash.containsKey((float) 4.0));
        assertTrue(hash.containsKey((float) 5.0));
        assertEquals(5, hash.size());

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                return key > 2.5;
            }
        };
        assertFalse(hash.forEach(procedure));

        hash.remove((float) 1.0);
        hash.remove((float) 2.0);
        assertTrue(hash.isEmpty());
    }

    @Test
    public void testTFloatDoubleHashSerialization() {
        TFloatDoubleHash hash = new TFloatDoubleHash(5);
        hash.put((float) 1.0, 1.0);
        hash.put((float) 2.0, 2.0);
        hash.put((float) 3.0, 3.0);
        hash.put((float) 4.0, 4.0);
        hash.put((float) 5.0, 5.0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TFloatDoubleHash deserializedHash = (TFloatDoubleHash) in.readObject();

        assertEquals(5, deserializedHash.size());
        assertTrue(deserializedHash.containsKey((float) 1.0));
        assertTrue(deserializedHash.containsKey((float) 2.0));
        assertTrue(deserializedHash.containsKey((float) 3.0));
        assertTrue(deserializedHash.containsKey((float) 4.0));
        assertTrue(deserializedHash.containsKey((float) 5.0));
    }
}