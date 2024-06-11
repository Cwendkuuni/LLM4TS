package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertTrue(hash.isEmpty());
        assertEquals(5, hash.size());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testBasicOperations() {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        assertEquals(3, hash.size());
        assertTrue(hash.containsKey(1.0f));
        assertTrue(hash.containsKey(2.0f));
        assertTrue(hash.containsKey(3.0f));
        assertFalse(hash.containsKey(4.0f));

        assertEquals('a', hash.get(1.0f));
        assertEquals('b', hash.get(2.0f));
        assertEquals('c', hash.get(3.0f));

        hash.remove(1.0f);
        assertFalse(hash.containsKey(1.0f));
        assertEquals(2, hash.size());

        hash.clear();
        assertTrue(hash.isEmpty());
        assertEquals(5, hash.size());
    }

    @Test
    public void testLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(3, 0.5f);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        hash.put(4.0f, 'd');

        assertEquals(4, hash.size());
        assertTrue(hash.containsKey(1.0f));
        assertTrue(hash.containsKey(2.0f));
        assertTrue(hash.containsKey(3.0f));
        assertTrue(hash.containsKey(4.0f));

        hash.put(5.0f, 'e'); // should trigger rehash

        assertEquals(7, hash.size());
        assertTrue(hash.containsKey(1.0f));
        assertTrue(hash.containsKey(2.0f));
        assertTrue(hash.containsKey(3.0f));
        assertTrue(hash.containsKey(4.0f));
        assertTrue(hash.containsKey(5.0f));
    }

    @Test
    public void testRehashing() {
        TFloatCharHash hash = new TFloatCharHash(3, 0.5f);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        hash.put(4.0f, 'd');
        hash.put(5.0f, 'e'); // should trigger rehash

        assertEquals(7, hash.size());
        assertTrue(hash.containsKey(1.0f));
        assertTrue(hash.containsKey(2.0f));
        assertTrue(hash.containsKey(3.0f));
        assertTrue(hash.containsKey(4.0f));
        assertTrue(hash.containsKey(5.0f));

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float key) {
                System.out.println("Key: " + key);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TFloatCharHash hash = new TFloatCharHash(5);

        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        hash.writeExternal(out);
        out.close();

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TFloatCharHash deserializedHash = (TFloatCharHash) in.readObject();
        in.close();

        byteStream.close();

        assertEquals(3, deserializedHash.size());
        assertTrue(deserializedHash.containsKey(1.0f));
        assertTrue(deserializedHash.containsKey(2.0f));
        assertTrue(deserializedHash.containsKey(3.0f));
        assertFalse(deserializedHash.containsKey(4.0f));

        assertEquals('a', deserializedHash.get(1.0f));
        assertEquals('b', deserializedHash.get(2.0f));
        assertEquals('c', deserializedHash.get(3.0f));
    }
}