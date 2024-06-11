import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

 private TShortHash hash;

 @Test
 public void testTShortHashConstruction() {
 hash = new TShortHash();
 assertNotNull(hash);
 assertEquals(0, hash.size());
 }

 @Test
 public void testTShortHashWithInitialCapacity() {
 hash = new TShortHash(10);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTShortHashWithInitialCapacityAndLoadFactor() {
 hash = new TShortHash(10, 0.75f);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
 hash = new TShortHash(10, 0.75f, (short) 1);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 hash = new TShortHash(-5);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testZeroInitialCapacity() {
 hash = new TShortHash(0);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hash = new TShortHash(10, -0.5f);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testContains() {
 hash = new TShortHash();
 assertFalse(hash.contains((short) 1));
 hash.put((short) 1, (short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 final int expectedSize = 5;
 hash = new TShortHash(expectedSize);
 final TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short object) {
 hash.remove((short) object);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 assertEquals(expectedSize - 1, hash.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWhenHashIsEmpty() {
 hash = new TShortHash();
 final TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short object) {
 return false;
 }
 };
 hash.forEach(procedure);
 fail("UnsupportedOperationException expected but not thrown");
 }

 @Test
 public void testRemoveAt() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 assertEquals(3, hash.size());
 hash.removeAt(1);
 assertEquals(2, hash.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWhenIndexIsOutOfBounds() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.removeAt(-1);
 fail("IndexOutOfBoundsException expected but not thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWhenIndexIsLargerThanTheSize() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 assertEquals(1, hash.size());
 hash.removeAt(10);
 fail("IndexOutOfBoundsException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndexForRemoveAt() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.removeAt(-1);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testIndex() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 assertEquals(1, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 4));
 }

 @Test(expected = NullPointerException.class)
 public void testIndexWithNullValue() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.index(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testInsertKey() {
 hash = new TShortHash();
 assertEquals(-1, hash.insertKey((short) 1));
 hash.put((short) 1, (short) 1);
 assertEquals(0, hash.insertKey((short) 1));
 }

 @Test
 public void testInsertKeyWhenHashIsFull() {
 hash = new TShortHash(4, 1.0f);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 try {
 hash.put((short) 5, (short) 5);
 fail("IllegalStateException expected but not thrown");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TShortHash(4, 1.0f);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 assertEquals(0, hash.insertKey((short) 5));
 assertEquals(5, hash.size());
 }

 @Test
 public void testInsertKeyAt() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 assertEquals(3, hash.size());
 hash.insertKeyAt(2, (short) 4);
 assertEquals(4, hash.size());
 assertEquals((short) 4, hash.get((short) 2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertKeyAtWhenIndexIsOutOfBounds() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.insertKeyAt(-1, (short) 4);
 fail("IndexOutOfBoundsException expected but not thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertKeyAtWhenIndexIsLargerThanTheSize() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.insertKeyAt(10, (short) 4);
 fail("IndexOutOfBoundsException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndexForInsertKeyAt() {
 hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.insertKeyAt(-1, (short) 4);
 fail("IllegalArgumentException expected but not thrown");
 }

}