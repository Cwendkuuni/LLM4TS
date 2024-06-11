package test.gnu.trove.impl.hash;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class TCharHashTest {

    private TCharHash charHash;

    @Before
    public void setUp() {
        charHash = new TCharHash(5);
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertEquals(5, charHash.capacity());
        Assert.assertEquals(0.75f, charHash.loadFactor(), 0.01);
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
        Assert.assertArrayEquals(new char[5], charHash._set);
    }

    @Test
    public void testInitialCapacityConstructor() {
        charHash = new TCharHash(10);
        Assert.assertEquals(10, charHash.capacity());
        Assert.assertEquals(0.75f, charHash.loadFactor(), 0.01);
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
        Assert.assertArrayEquals(new char[10], charHash._set);
    }

    @Test
    public void testInitialCapacityLoadFactorConstructor() {
        charHash = new TCharHash(15, 0.85f);
        Assert.assertEquals(15, charHash.capacity());
        Assert.assertEquals(0.85f, charHash.loadFactor(), 0.01);
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
        Assert.assertArrayEquals(new char[15], charHash._set);
    }

    @Test
    public void testInitialCapacityLoadFactorAndNoEntryValueConstructor() {
        charHash = new TCharHash(17, 0.85f, 'X');
        Assert.assertEquals(17, charHash.capacity());
        Assert.assertEquals(0.85f, charHash.loadFactor(), 0.01);
        char noEntryValue = charHash.getNoEntryValue();
        Assert.assertEquals('X', noEntryValue);
        Assert.assertArrayEquals(new char[17], charHash._set);
        Arrays.fill(charHash._set, noEntryValue);
    }

    @Test
    public void testGetNoEntryValue() {
        charHash.put('A');
        char defaultValue = charHash.getNoEntryValue();
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
    }

    @Test
    public void testSetUp() {
        int capacity = charHash.setUp(7);
        Assert.assertEquals(7, charHash.capacity());
        char[] set = charHash._set;
        Assert.assertArrayEquals(new char[7], set);
    }

    @Test
    public void testContains() {
        charHash.put('A');
        Assert.assertTrue(charHash.contains('A'));
        Assert.assertFalse(charHash.contains('B'));
    }

    @Test
    public void testForEach() {
        charHash.put('A');
        charHash.put('B');
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char character) {
                return character == 'A' || character == 'B';
            }
        };
        boolean result = charHash.forEach(procedure);
        Assert.assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        charHash.put('A');
        charHash.put('B');
        charHash.removeAt(0);
        charHash.put('C');
        Assert.assertFalse(charHash.contains('A'));
        Assert.assertTrue(charHash.contains('B'));
        Assert.assertTrue(charHash.contains('C'));
    }

    @Test
    public void testIndex() {
        charHash.put('A');
        charHash.put('B');
        int index = charHash.index('B');
        Assert.assertEquals(1, index);
        index = charHash.index('C');
        Assert.assertEquals(-1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testIndexRehashed() {
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');
        charHash.put('F');
        charHash.put('G');
        charHash.put('H');
        charHash.put('I');
        charHash.put('J');
        charHash.put('K');
        charHash.put('L');
        charHash.put('M');
        charHash.put('N');
        charHash.put('O');
        charHash.put('P');
        charHash.put('Q');
        charHash.put('R');
        charHash.put('S');
        charHash.put('T');
        charHash.put('U');
        charHash.put('V');
        charHash.put('W');
        charHash.put('X');
        charHash.put('Y');
        charHash.put('Z');
        char HashKey = 'Z';
        int index = charHash.indexRehashed(HashKey, -1, 0, (byte) 0);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        charHash.put('A');
        charHash.put('B');
        char HashKey = 'C';
        int index = charHash.insertKey(HashKey);
        Assert.assertEquals(3, index);
        charHash.put('D');
        Assert.assertEquals('C', charHash._set[2]);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        charHash.put('A');
        charHash.put('B');
        charHash.put('C');
        charHash.put('D');
        charHash.put('E');
        charHash.put('F');
        charHash.put('G');
        charHash.put('H');
        charHash.put('I');
        charHash.put('J');
        charHash.put('K');
        charHash.put('L');
        charHash.put('M');
        charHash.put('N');
        charHash.put('O');
        charHash.put('P');
        charHash.put('Q');
        charHash.put('R');
        charHash.put('S');
        charHash.put('T');
        charHash.put('U');
        charHash.put('V');
        charHash.put('W');
        charHash.put('X');
        charHash.put('Y');
        charHash.put('Z');
        char HashKey = 'A';
        int index = charHash.insertKeyRehash(HashKey, -1, 0, (byte) 0);
        Assert.assertEquals(-1, index);
    }
}