package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(0.0f));
        assertFalse(hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return false;
            }
        }));
        assertEquals(0, hash.size());
    }

    @Test
    public void testBasicHash() {
        TFloatCharHash hash = new TFloatCharHash(10);
        hash.insertKey(2.0f);
        hash.insertKey(1.5f);
        hash.insertKey(3.0f);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.5f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertFalse(hash.contains(4.0f));
        int index = hash.index(1.5f);
        assertEquals(index, 1);
        index = hash.index(3.0f);
        assertEquals(index, 2);
        index = hash.index(2.0f);
        assertEquals(index, 0);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 3.0f);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRehashing() {
        TFloatCharHash hash = new TFloatCharHash(2);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(3, hash.size());
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                assertEquals(value, 2.0f);
                return true;
            }
        };
        assertFalse(hash.forEach(procedure));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objectOut = new ObjectOutputStream(out);
        hash.writeExternal(objectOut);
        ObjectInput objectIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        TFloatCharHash hash2 = (TFloatCharHash) objectIn.readObject();
        assertEquals(3, hash2.size());
        assertTrue(hash2.contains(1.0f));
        assertTrue(hash2.contains(2.0f));
        assertTrue(hash2.contains(3.0f));
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHash() {
        TFloatCharHash hash = new TFloatCharHash(2);
        for (float i = 0.0f; i <= 2.0f; i += 0.1f) {
            hash.insertKey(i);
        }
    }
}