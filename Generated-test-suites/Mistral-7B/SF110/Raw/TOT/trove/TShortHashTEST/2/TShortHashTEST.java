import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(15, 0.8f, (short) 127);
 assertEquals(15, hash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Short.MIN_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash hash = new TShortHash(-5);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFactorGreaterThanOne() {
 TShortHash hash = new TShortHash(10, 1.5f);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullNoEntryValue() {
 TShortHash hash = new TShortHash(10, 0.8f, null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testContains() {
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

 assertFalse(hash.contains((short) 0));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short val) {
 assertEquals(val, (short) (val + 1));
 return true;
 }
 };

 hash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 hash.removeAt(-1);
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 hash.removeAt(5);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 hash.removeAt(-1);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 assertEquals(-1, hash.insertKey((short) 1));
 assertEquals(6, hash.size());

 assertEquals(0, hash.insertKey((short) 0));
 assertEquals(7, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNegativeValue() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 hash.insertKey((short) Short.MIN_VALUE);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testResize() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 hash.put((short) 8);
 hash.put((short) 9);
 hash.put((short) 10);

 assertEquals(11, hash.size());
 }
}