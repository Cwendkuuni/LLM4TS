import org.junit.Test;
import org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

    @Test
    public void testTCharHashConstructor() {
        TCharHash charHash = new TCharHash();
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(defaultValue, charHash.contains('\0'));
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        assertEquals(2, charHash.size());
        Assert.assertTrue(charHash.contains('a'));
        Assert.assertTrue(charHash.contains('b'));
    }

    @Test
    public void testTCharHashInitialCapacityConstructor() {
        TCharHash charHash = new TCharHash(10);
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(defaultValue, charHash.contains('\0'));
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        assertEquals(2, charHash.size());
        Assert.assertTrue(charHash.contains('a'));
        Assert.assertTrue(charHash.contains('b'));
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorConstructor() {
        TCharHash charHash = new TCharHash(10, 0.5f);
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(defaultValue, charHash.contains('\0'));
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        assertEquals(2, charHash.size());
        Assert.assertTrue(charHash.contains('a'));
        Assert.assertTrue(charHash.contains('b'));
    }

    @Test
    public void testTCharHashInitialCapacityAndLoadFactorAndValueConstructor() {
        TCharHash charHash = new TCharHash(10, 0.5f, 'e');
        assertEquals(0, charHash.size());
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(defaultValue, charHash.contains('\0'));
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        assertEquals(2, charHash.size());
        Assert.assertEquals('e', charHash.getNoEntryValue());
        Assert.assertTrue(charHash.contains('a'));
        Assert.assertTrue(charHash.contains('b'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyWhenHashTableIsFull() {
        TCharHash charHash = new TCharHash(2);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
    }

    @Test
    public void testContainsMethod() {
        TCharHash charHash = new TCharHash();
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        assertTrue(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
        assertFalse(charHash.contains('c'));
    }

    @Test
    public void testForEachMethod() {
        TCharHash charHash = new TCharHash();
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');

        int count = 0;
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char charValue) {
                count++;
                return true;
            }
        };
        charHash.forEach(procedure);
        assertEquals(3, count);
    }

    @Test
    public void testRemoveAtMethod() {
        TCharHash charHash = new TCharHash();
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');

        charHash.removeAt(1);
        assertEquals(2, charHash.size());
        assertFalse(charHash.contains('b'));
    }

    @Test
    public void testIndexMethod() {
        TCharHash charHash = new TCharHash();
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');

        int indexA = charHash.index('a');
        int indexB = charHash.index('b');
        int indexC = charHash.index('c');
        int indexD = charHash.index('d');

        assertEquals(0, indexA);
        assertEquals(1, indexB);
        assertEquals(2, indexC);
        assertEquals(-1, indexD);
    }

    @Test
    public void testIndexRehashedMethod() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');

        int indexA = charHash.index('a');
        int indexB = charHash.index('b');
        int indexC = charHash.index('c');
        int indexD = charHash.index('d');
        int indexE = charHash.index('e');

        assertEquals(0, indexA);
        assertEquals(1, indexB);
        assertEquals(2, indexC);
        assertEquals(-1, indexD);
        assertEquals(-1, indexE);
    }

    @Test
    public void testInsertKeyMethod() {
        TCharHash charHash = new TCharHash();
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');

        char newValue = 'd';
        int index = charHash.insertKey(newValue);
        assertEquals(4, charHash.size());
        assertTrue(charHash.contains(newValue));
        assertEquals(index, charHash.indexOf(newValue));
    }

    @Test
    public void testInsertKeyRehashMethod() {
        TCharHash charHash = new TCharHash(3);
        charHash.put('a', 'a');
        charHash.put('b', 'b');
        charHash.put('c', 'c');
        charHash.put('d', 'd');

        char newValue = 'e';
        int index = charHash.insertKey(newValue);
        assertEquals(5, charHash.size());
        assertTrue(charHash.contains(newValue));
        assertEquals(index, charHash.indexOf(newValue));
    }

}