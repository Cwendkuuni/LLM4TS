import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    TCharHash charHash;

    @Before
    public void setUp() {
        charHash = new TCharHash(11, 0.5f);
    }

    @Test
    public void testDefaultConstructor() {
        TCharHash defaultCharHash = new TCharHash();
        assertEquals(0, defaultCharHash.size());
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultCharHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityConstructor() {
        assertEquals(11, charHash.capacity());
        assertEquals(0.5f, charHash.loadFactor(), 0.01);
        assertEquals(0, charHash.size());
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TCharHash charHash = new TCharHash(11, 0.5f, 'N');
        assertEquals(11, charHash.capacity());
        assertEquals(0.5f, charHash.loadFactor(), 0.01);
        assertEquals('N', charHash.getNoEntryValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityConstructor() {
        new TCharHash(-1, 0.5f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLoadFactorConstructor() {
        new TCharHash(11, -0.5f);
    }

    @Test
    public void testContains() {
        charHash.put('A', 'V');
        assertTrue(charHash.contains('A'));
        assertFalse(charHash.contains('B'));
    }

    @Test
    public void testForEachProcedure() {
        charHash.put('A', 'V');
        charHash.put('B', 'W');
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;

            @Override
            public boolean execute(char character) {
                count++;
                return true;
            }
        };
        charHash.forEach(procedure);
        assertEquals(2, charHash.size());
        assertEquals(2, procedure.getInvocationCount());
    }

    @Test
    public void testRemoveAt() {
        charHash.put('A', 'V');
        charHash.put('B', 'W');
        charHash.put('C', 'X');
        charHash.removeAt(1);
        assertEquals('V', charHash.get('A'));
        assertEquals('X', charHash.get('C'));
        assertNull(charHash.get('B'));
    }

    @Test
    public void testIndex() {
        charHash.put('A', 'V');
        charHash.put('B', 'W');
        charHash.put('C', 'X');
        assertEquals(0, charHash.index('A'));
        assertEquals(-1, charHash.index('D'));
    }

    @Test
    public void testInsertKey() {
        charHash.put('A', 'V');
        charHash.put('B', 'W');
        charHash.put('C', 'X');
        assertEquals(3, charHash.size());
        assertEquals('V', charHash.get('A'));
        assertEquals('W', charHash.get('B'));
        assertEquals('X', charHash.get('C'));
        assertEquals(-1, charHash.index('D'));
        charHash.put('D', 'Y');
        assertEquals(4, charHash.size());
        assertEquals('Y', charHash.get('D'));
        assertEquals(-1, charHash.index('E'));
    }

    @Test
    public void testInsertKeyRehash() {
        charHash.put('A', 'V');
        charHash.put('B', 'W');
        charHash.put('C', 'X');
        charHash.put('D', 'Y');
        charHash.put('E', 'Z');
        charHash.put('F', 'W');
        assertEquals(6, charHash.size());
        assertEquals('V', charHash.get('A'));
        assertEquals('W', charHash.get('F'));
        assertEquals('Z', charHash.get('E'));
        assertEquals('Y', charHash.get('D'));
        assertEquals('X', charHash.get('C'));
        assertEquals('W', charHash.get('B'));
        assertEquals(-1, charHash.index('G'));
    }
}