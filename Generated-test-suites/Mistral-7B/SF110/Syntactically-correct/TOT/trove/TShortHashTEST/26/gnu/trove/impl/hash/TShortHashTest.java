import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TShortHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(5, hashMap.size());
 assertNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hashMap = new TShortHash(10);
 assertEquals(10, hashMap.size());
 assertNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hashMap = new TShortHash(10, 0.75f);
 assertEquals(10, hashMap.capacity());
 assertEquals(7, hashMap.size()); // assuming 3 slots are used for empty and deleted elements
 assertNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hashMap = new TShortHash(10, 0.75f, (short) 42);
 assertEquals(10, hashMap.capacity());
 assertEquals(7, hashMap.size()); // assuming 3 slots are used for empty and deleted elements
 assertEquals((short) 42, hashMap.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hashMap = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hashMap = new TShortHash(5);
 assertEquals(5, hashMap.capacity());
 assertEquals(0, hashMap.size());
 assertArrayEquals(new short[5], hashMap._set);
 }

 @Test
 public void testContains() {
 hashMap.put((short) 1);
 assertTrue(hashMap.contains((short) 1));
 }

 @Test
 public void testContainsNullValue() {
 hashMap.put((short) 1);
 assertFalse(hashMap.contains(null));
 }

 @Test
 public void testContainsEmptyHashMap() {
 assertFalse(hashMap.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachOnEmptyHashMap() {
 hashMap.forEach(new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return true;
 }
 });
 }

 @Test
 public void testRemoveAt() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.removeAt(0);
 assertEquals(1, hashMap.size());
 assertFalse(hashMap.contains((short) 1));
 assertTrue(hashMap.contains((short) 2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtNegativeIndex() {
 hashMap.put((short) 1);
 hashMap.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.removeAt(2);
 }

 @Test
 public void testInsertKey() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.put((short) 3);
 assertEquals(3, hashMap.size());
 assertTrue(hashMap.contains((short) 1));
 assertTrue(hashMap.contains((short) 2));
 assertTrue(hashMap.contains((short) 3));
 }

 @Test
 public void testInsertKeyDuplicateValue() {
 hashMap.put((short) 1);
 hashMap.put((short) 1);
 assertEquals(1, hashMap.size());
 assertTrue(hashMap.contains((short) 1));
 }

 @Test
 public void testInsertKeyNullValue() {
 hashMap.put((short) 1);
 assertFalse(hashMap.contains(null));
 hashMap.put(null);
 assertEquals(2, hashMap.size());
 assertTrue(hashMap.contains(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNegativeValue() {
 hashMap.put((short) 1);
 assertFalse(hashMap.contains((short) -1));
 hashMap.put((short) -1);
 }

 @Test
 public void testIndex() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.put((short) 3);
 assertEquals(1, hashMap.index((short) 1));
 assertEquals(-1, hashMap.index((short) 4));
 }

 @Test
 public void testIndexRehashed() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.put((short) 3);
 hashMap.put((short) 4);
 hashMap.put((short) 5);
 assertEquals(1, hashMap.index((short) 1));
 assertEquals(3, hashMap.index((short) 4));
 }
}