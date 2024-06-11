package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import gnu.trove.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;

public class TShortHashTest {

    @Test
    public void testTShortHashConstruction() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);
        assertEquals(0, hash.size());

        TShortHash hash10 = new TShortHash(10);
        assertNotNull(hash10);
        assertEquals(0, hash10.size());

        TShortHash hash20_0_5f = new TShortHash(20, 0.5f);
        assertNotNull(hash20_0_5f);
        assertEquals(0, hash20_0_5f.size());

        TShortHash hash30_0_75f = new TShortHash(30, 0.75f, (short) 123);
        assertNotNull(hash30_0_75f);
        assertEquals(0, hash30_0_75f.size());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        assertEquals(Short.MIN_VALUE, hash.getNoEntryValue());

        short customNoEntryValue = 123;
        TShortHash hashCustom = new TShortHash(10, 0.75f, customNoEntryValue);
        assertEquals(customNoEntryValue, hashCustom.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        assertFalse(hash.contains((short) 1));

        hash.put((short) 1, (short) 1);
        assertTrue(hash.contains((short) 1));

        hash.remove((short) 1);
        assertFalse(hash.contains((short) 1));
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();

        short[] expected = new short[11];
        int index = 0;
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short shortValue) {
                expected[index++] = shortValue;
                return true;
            }
        };

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        hash.forEach(procedure);

        assertArrayEquals(expected, new short[]{1, 2, 3});
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveAtNullIndex() {
        TShortHash hash = new TShortHash();
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(0, hash.index((short) 1));
        assertEquals(1, hash.index((short) 2));
        assertEquals(2, hash.index((short) 3));
        assertEquals(-1, hash.index((short) 4));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();

        hash.put((short) 1, (short) 1);
        hash.put((short) 2, (short) 2);
        hash.put((short) 3, (short) 3);

        assertEquals(3, hash.size());

        hash.put((short) 4, (short) 4);
        assertEquals(4, hash.size());

        short keyToInsert = (short) 10;
        int index = hash.insertKey(keyToInsert);
        assertEquals(5, hash.size());
        assertEquals(keyToInsert, hash.get((short) index));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();

        for (short i = 0; i < 100; i++) {
            hash.put((short) i, (short) i);
        }

        short keyToInsert = (short) 100;
        int index = hash.insertKeyRehash(keyToInsert, 0, 0, (byte) 1);
        assertEquals(101, hash.size());
        assertEquals(keyToInsert, hash.get((short) index));
    }
}