package test;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TShortHashTest {

    @Test
    public void testTShortHashConstructor() {
        TShortHash hash = new TShortHash();
        Assert.assertNotNull(hash);
    }

    @Test
    public void testTShortHashInitialCapacityConstructor() {
        TShortHash hash = new TShortHash(10);
        Assert.assertEquals(10, hash.size());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorConstructor() {
        TShortHash hash = new TShortHash(10, 0.75f);
        Assert.assertEquals(10, hash.capacity());
        Assert.assertEquals(8, hash.size());
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        short noEntryValue = 1;
        TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
        Assert.assertEquals(10, hash.capacity());
        Assert.assertEquals(8, hash.size());
        Assert.assertEquals(noEntryValue, hash.getNoEntryValue());
    }

    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHash();
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash(5);
        Assert.assertEquals(5, hash.capacity());
        Assert.assertArrayEquals(new short[5], hash._set);
    }

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        Assert.assertTrue(hash.contains((short) 1));
        Assert.assertFalse(hash.contains((short) 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForEachWithNullProcedure() {
        TShortHash hash = new TShortHash();
        hash.forEach(null);
    }

    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        short[] expected = new short[5];
        hash.put((short) 1);
        hash.put((short) 2);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                int index = Arrays.asList(hash._set).indexOf((Object) value);
                expected[index] = value;
                return true;
            }
        };
        hash.forEach(procedure);
        Assert.assertArrayEquals(Arrays.asList(1, 2).toArray(new short[2]), expected);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.removeAt(0);
        Assert.assertFalse(hash.contains((short) 1));
        Assert.assertTrue(hash.contains((short) 2));
    }

    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        Assert.assertEquals(0, hash.index((short) 1));
        Assert.assertEquals(-1, hash.index((short) 3));
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        Assert.assertEquals(2, hash.index((short) 3));
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        int index = hash.insertKey((short) 1);
        Assert.assertEquals(0, index);
        Assert.assertTrue(hash.contains((short) 1));
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        hash.put((short) 1);
        hash.put((short) 2);
        hash.put((short) 3);
        hash.put((short) 4);
        int index = hash.insertKey((short) 5);
        Assert.assertEquals(-1, index);
        Assert.assertFalse(hash.contains((short) 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertKeyWithNullValue() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) null);
    }

    @Test
    public void testConsumeFreeSlot() {
        TShortHash hash = new TShortHash();
        Assert.assertTrue(hash.consumeFreeSlot);
        hash.put((short) 1);
        Assert.assertFalse(hash.consumeFreeSlot);
    }
}