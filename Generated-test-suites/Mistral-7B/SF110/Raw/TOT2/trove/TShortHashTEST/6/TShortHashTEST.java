import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

    @Test
    public void testDefaultConstructor() {
        // Test 1: Default constructor
        TShortHash hash = new TShortHash();
        assertNull(hash.getNoEntryValue());
        assertEquals(0, hash.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        // Test 2: Constructor with initialCapacity
        TShortHash hash = new TShortHash(5);
        assertNotNull(hash.getNoEntryValue());
        assertEquals(5, hash.size());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        // Test 3: Constructor with initialCapacity and loadFactor
        TShortHash hash = new TShortHash(10, 0.75f);
        assertNotNull(hash.getNoEntryValue());
        assertEquals(10, hash.size());
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        // Test 4: Constructor with initialCapacity, loadFactor, and no_entry_value
        TShortHash hash = new TShortHash(15, 0.8f, 1);
        assertNotNull(hash.getNoEntryValue());
        assertEquals(15, hash.size());
    }

    @Test
    public void testGetNoEntryValue() {
        // Test 5: getNoEntryValue()
        TShortHash hash = new TShortHash(5);
        assertEquals(hash.getNoEntryValue(), TShortHash.Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
    }

    @Test
    public void testSetUp() {
        // Test 6: setUp()
        TShortHash hash = new TShortHash(7);
        assertEquals(7, hash.setUp(7).length);
    }

    @Test
    public void testContains() {
        // Test 7: contains()
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);

        assertTrue(hash.contains((short) 1));
        assertTrue(hash.contains((short) 2));
        assertTrue(hash.contains((short) 3));
        assertTrue(hash.contains((short) 4));
        assertTrue(hash.contains((short) 5));

        assertFalse(hash.contains((short) 6));
    }

    @Test
    public void testForEach() {
        // Test 8: forEach()
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        hash.put((short) 5);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });

        assertEquals(5, count);
    }

    // Edge cases and exception handling scenarios to be tested

    @Test(expected = IllegalArgumentException.class)
    public void testException1() {
        new TShortHash(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException2() {
        new TShortHash(0.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException3() {
        new TShortHash(5, 1.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException4() {
        new TShortHash(5, 0.5f, -1);
    }

    @Test
    public void testEdgeCase1() {
        // Edge case: Testing if the hash table remains empty when no elements are added
        TShortHash hash = new TShortHash(5);
        assertEquals(0, hash.size());
    }

    @Test
    public void testEdgeCase2() {
        // Edge case: Testing if the hash table can handle the maximum number of elements
        TShortHash hash = new TShortHash(Integer.MAX_VALUE);
        hash.put((short) 0);
        hash.put((short) 1);
        hash.put((short) 2);
        // ... continue adding elements up to Integer.MAX_VALUE
        assertEquals(Integer.MAX_VALUE, hash.size());
    }

    @Test
    public void testEdgeCase3() {
        // Edge case: Testing if the hash table can handle collisions effectively
        TShortHash hash = new TShortHash(5);
        hash.put((short) 1);
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 3);

        int count = 0;
        hash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        });

        assertEquals(3, count);
    }
}