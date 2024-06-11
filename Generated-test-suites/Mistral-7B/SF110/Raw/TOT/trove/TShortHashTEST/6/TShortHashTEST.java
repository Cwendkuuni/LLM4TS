import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setup() {
 hash = new TShortHash(10);
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(10, hash.capacity());
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TShortHash(5);
 assertEquals(5, hash.capacity());
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 hash = new TShortHash(10, 0.75f, (short) 42);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 assertSame((short) 42, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash.setNoEntryValue((short) 1);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 final int capacity = hash.setUp(8);
 assertEquals(8, capacity);
 }

 @Test
 public void testContains() {
 hash.insert((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testContainsEmptyHash() {
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testContainsNullValue() {
 assertFalse(hash.contains((short) null));
 }

 @Test
 public void testForEach() {
 hash.insert((short) 1);
 hash.insert((short) 2);
 final short[] expected = {1, 2};
 final TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertTrue(contains(value));
 int index = hash.indexOf(value);
 assertEquals((short) value, hash._set[index]);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 assertArrayEquals(expected, new short[expected.length]);
 }

 @Test
 public void testForEachEmptyHash() {
 final TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 fail("Should not be called");
 return false;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.removeAt(0);
 assertFalse(hash.contains((short) 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 hash.removeAt(10);
 }

 @Test
 public void testIndex() {
 hash.insert((short) 1);
 hash.insert((short) 2);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test(expected = NullPointerException.class)
 public void testIndexNullValue() {
 hash.index((short) null);
 }

 @Test
 public void testInsertKey() {
 hash.insert((short) 1);
 assertFalse(hash.contains((short) 1));
 hash.insert((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNegativeValue() {
 hash.insert((short) Short.MIN_VALUE);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFullHash() {
 final short[] values = new short[hash.capacity()];
 for (short value : values) {
 hash.insert(value);
 }
 }
}