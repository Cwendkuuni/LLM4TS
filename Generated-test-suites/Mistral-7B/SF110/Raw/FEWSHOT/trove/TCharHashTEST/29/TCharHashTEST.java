package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testTCharHashEmptyConstruction() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.size());
        assertNull(hash.getNoEntryValue());
        assertEquals(0, hash.capacity());
        assertEquals(serialVersionUID, hash.getSerialVersionUID());
        assertArrayEquals(new char[0], hash._set);
        byte[] states = hash._states;
        assertEquals(0, states.length);
        for (byte state : states) {
            assertEquals(0, state);
        }
    }

    @Test
    public void testTCharHashInitialCapacityConstruction() {
        TCharHash hash = new TCharHash(10);
        assertEquals(0, hash.size());
        assertEquals(' ', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
        assertEquals(serialVersionUID, hash.getSerialVersionUID());
        assertEquals(10, hash._set.length);
        Arrays.fill(hash._set, ' ');
        byte[] states = hash._states;
        assertEquals(10, states.length);
        for (int i = 0; i < states.length; i++) {
            assertEquals(0, states[i]);
        }
    }

    @Test
    public void testTCharHashInitialCapacityLoadFactorConstruction() {
        TCharHash hash = new TCharHash(10, 0.5f);
        assertEquals(0, hash.size());
        assertEquals(' ', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
        assertEquals(0.5f, hash.loadFactor(), 0.01f);
        assertEquals(serialVersionUID, hash.getSerialVersionUID());
        assertEquals(10, hash._set.length);
        Arrays.fill(hash._set, ' ');
        byte[] states = hash._states;
        assertEquals(10, states.length);
        for (int i = 0; i < states.length; i++) {
            assertEquals(0, states[i]);
        }
    }

    @Test
    public void testTCharHashInitialCapacityLoadFactorNoEntryValueConstruction() {
        char noEntryValue = 'X';
        TCharHash hash = new TCharHash(10, 0.5f, noEntryValue);
        assertEquals(0, hash.size());
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
        assertEquals(0.5f, hash.loadFactor(), 0.01f);
        assertEquals(serialVersionUID, hash.getSerialVersionUID());
        assertEquals(10, hash._set.length);
        Arrays.fill(hash._set, noEntryValue);
        byte[] states = hash._states;
        assertEquals(10, states.length);
        for (int i = 0; i < states.length; i++) {
            assertEquals(0, states[i]);
        }
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 15;
        TCharHash hash = new TCharHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return super.setUp(initialCapacity) + 5;
            }
        };
        assertEquals(initialCapacity + 5, hash.capacity());
        assertEquals(initialCapacity + 5, hash._set.length);
        byte[] states = hash._states;
        assertEquals(initialCapacity + 5, states.length);
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('A'));
        hash.put('A', 'A');
        assertTrue(hash.contains('A'));
        hash.put('B', 'B');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.put('B', 'B');
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char character) {
                count++;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, procedure.getCount());
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.removeAt(0);
        assertFalse(hash.contains('A'));
        assertTrue(hash.contains('B'));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAtInvalidIndex() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.removeAt(-1);
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.put('B', 'B');
        int index = hash.index('A');
        assertEquals(0, index);
        index = hash.index('B');
        assertEquals(1, index);
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');
        hash.put('D', 'D');
        int index = hash.index('A');
        assertEquals(0, index);
        index = hash.index('E');
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        char key = 'A';
        hash.put(key, key);
        int index = hash.index(key);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        char key = 'A';
        hash.put('A', 'A');
        hash.put('B', 'B');
        hash.put('C', 'C');
        hash.put('D', 'D');
        hash.put(key, key);
        int index = hash.index(key);
        int expectedIndex = hash._states.length / 2;
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testConsumeFreeSlot() {
        TCharHash hash = new TCharHash();
        char key = 'A';
        hash.put(key, key);
        hash.put(key, key);
        hash.put(key, key);
        hash.put(key, key);
        hash.put(key, key);
        int index = hash.insertKey(key);
        assertEquals(-5, index);
        assertTrue(hash.consumeFreeSlot);
        index = hash.index(key);
        assertEquals(0, index);
    }
}