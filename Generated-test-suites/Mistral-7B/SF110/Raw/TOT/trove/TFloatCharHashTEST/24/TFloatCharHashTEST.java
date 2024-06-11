package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testEmptyHash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        assertFalse(hash.contains(Float.NaN));
        assertFalse(hash.contains(0.0f));
        assertFalse(hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                return false;
            }
        }));
        assertEquals(0, hash.size());
    }

    @Test
    public void testBasicHash() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(1.0f, 'A');
        hash.insert(2.0f, 'B');
        hash.insert(3.0f, 'C');

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertFalse(hash.contains(Float.NaN));
        assertFalse(hash.contains(0.0f));

        assertEquals('A', hash.get(1.0f));
        assertEquals('B', hash.get(2.0f));
        assertEquals('C', hash.get(3.0f));
        assertNull(hash.get(Float.NaN));

        int size = 0;
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float floatValue) {
                size++;
                return true;
            }
        });
        assertEquals(3, size);

        assertEquals(3, hash.size());
    }

    @Test
    public void testLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(5, 0.75f);

        for (int i = 0; i < 7; i++) {
            hash.insert(i / 2.0f, (char) (i % 26 + 'A'));
        }

        for (int i = 0; i < 7; i++) {
            assertTrue(hash.contains(i / 2.0f));
        }

        assertEquals(7, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testFullHash() {
        TFloatCharHash hash = new TFloatCharHash(5, 0.75f);

        for (int i = 0; i < 11; i++) {
            hash.insert(i / 2.0f, (char) (i % 26 + 'A'));
        }
    }

    @Test
    public void testRemove() {
        TFloatCharHash hash = new TFloatCharHash(5);
        hash.insert(1.0f, 'A');
        hash.insert(2.0f, 'B');
        hash.insert(3.0f, 'C');

        assertEquals('A', hash.get(1.0f));
        assertEquals('B', hash.get(2.0f));
        assertEquals('C', hash.get(3.0f));

        hash.remove(1.0f);

        assertFalse(hash.contains(1.0f));
        assertEquals('B', hash.get(2.0f));
        assertEquals('C', hash.get(3.0f));

        hash.remove(2.0f);

        assertFalse(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        assertEquals('C', hash.get(3.0f));

        hash.remove(3.0f);

        assertFalse(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));

        assertEquals(0, hash.size());
    }
}