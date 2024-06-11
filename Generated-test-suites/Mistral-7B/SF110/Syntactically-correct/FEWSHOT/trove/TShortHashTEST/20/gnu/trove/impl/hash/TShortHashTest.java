package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testTShortHash() {
        TShortHash hashMap = new TShortHash(10);
        assertEquals(10, hashMap.size());
        assertNull(hashMap.getNoEntryValue());

        TShortHash hashMapWithInitialValue = new TShortHash(10, 0.75f);
        assertEquals(10, hashMapWithInitialValue.size());
        assertEquals(0, hashMapWithInitialValue.getNoEntryValue());

        TShortHash hashMapWithCustomValue = new TShortHash(10, 0.75f, (short) 123);
        assertEquals(10, hashMapWithCustomValue.size());
        assertEquals(123, hashMapWithCustomValue.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hashMap = new TShortHash(10);
        assertEquals(10, hashMap.capacity());

        short[] set = hashMap._set;
        byte[] states = hashMap._states;
        assertNull(set);
        assertNull(states);

        hashMap = new TShortHash(10, 0.75f);
        set = hashMap._set;
        states = hashMap._states;
        assertNotNull(set);
        assertNotNull(states);
        assertEquals(set.length, states.length);
    }

    @Test
    public void testContains() {
        TShortHash hashMap = new TShortHash(10);
        assertFalse(hashMap.contains((short) 1));
        assertFalse(hashMap.contains((short) 2));

        hashMap.put((short) 1, (short) 1);
        assertTrue(hashMap.contains((short) 1));
        assertFalse(hashMap.contains((short) 2));
    }

    @Test
    public void testForEach() {
        TShortHash hashMap = new TShortHash(10);

        int count = 0;
        hashMap.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });
        assertEquals(0, count);

        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);
        hashMap.put((short) 3, (short) 3);

        count = 0;
        hashMap.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hashMap = new TShortHash(10);
        hashMap.put((short) 1, (short) 1);
        hashMap.put((short) 2, (short) 2);

        assertEquals(2, hashMap.size());
        hashMap.removeAt(1);
        assertEquals(1, hashMap.size());
        assertNull(hashMap.get((short) 2));
    }

    @Test
    public void testIndex() {
        TShortHash hashMap = new TShortHash(10);

        int index = hashMap.index((short) 1);
        assertEquals(-1, index);

        hashMap.put((short) 1, (short) 1);
        index = hashMap.index((short) 1);
        assertEquals(0, index);

        hashMap.put((short) 2, (short) 2);
        index = hashMap.index((short) 1);
        assertEquals(-1, index);

        index = hashMap.index((short) 2);
        assertEquals(1, index);

        hashMap.put((short) 3, (short) 3);
        index = hashMap.index((short) 1);
        assertEquals(-1, index);

        index = hashMap.index((short) 3);
        assertEquals(2, index);
    }
}