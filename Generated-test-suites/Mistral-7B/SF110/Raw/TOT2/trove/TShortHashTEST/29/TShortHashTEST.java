package gnu.trove.impl.hash.test;

import org.junit.Assert;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testDefaultConstructor() {
        TShortHash hash = new TShortHash();
        Assert.assertEquals(0, hash.size());
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        Assert.assertNull(hash.contains((short) 1));
    }

    @Test
    public void testCustomConstructorWithInitialCapacity() {
        TShortHash hash = new TShortHash(5);
        Assert.assertEquals(5, hash.size());
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testCustomConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash hash = new TShortHash(5, 0.75f);
        Assert.assertEquals(5, hash.size());
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testCustomConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash hash = new TShortHash(5, 0.75f, (short) 123);
        Assert.assertEquals(5, hash.size());
        Assert.assertEquals((short) 123, hash.getNoEntryValue());
    }

    @Test
    public void testMaximumHashTableLength() {
        TShortHash hash = new TShortHash(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, hash.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacityAndLoadFactor() {
        new TShortHash(-1, -1.5f);
    }

    @Test(expected = NullPointerException.class)
    public void testNullNoEntryValue() {
        new TShortHash(5, 0.75f, null);
    }

}