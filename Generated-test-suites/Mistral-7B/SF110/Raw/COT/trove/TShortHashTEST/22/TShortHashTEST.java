import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(11);
 }

 // Test constructor with default arguments
 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 // Test constructor with initial capacity
 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.size());
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(11, 0.75f);
 assertEquals(11, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 }

 // Test constructor with initial capacity, load factor, and no entry value
 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(11, 0.75f, (short) 1);
 assertEquals(11, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(11);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 hash.setNoEntryValue((short) 1);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.capacity());
 assertEquals(new short[11], hash._set);
 }

 // Test contains method
 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.contains((short) 1));
 assertFalse(hash.contains((short) 3));
 }

 // Test forEach method with empty hash
 @Test
 public void testForEachWithEmptyHash() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 return false;
 }
 };
 assertFalse(hash.forEach(procedure));
 }

 // Test forEach method with non-empty hash
 @Test
 public void testForEachWithNonEmptyHash() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 assertEquals((short) 1, shortValue);
 return true;
 }
 };
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.forEach(procedure));
 }

 // Test removeAt method with empty hash
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithEmptyHash() {
 hash.removeAt(-1);
 }

 // Test removeAt method with non-empty hash
 @Test
 public void testRemoveAtWithNonEmptyHash() {
 hash.put((short) 1);
 hash.put((short) 2);
 short removedVal = hash.removeAt(0);
 assertEquals((short) 1, removedVal);
 assertEquals(1, hash.size());
 }

 // Test index method with empty hash
 @Test
 public void testIndexWithEmptyHash() {
 assertEquals(-1, hash.index((short) 1));
 }

 // Test index method with non-empty hash and present key
 @Test
 public void testIndexWithNonEmptyHashAndPresentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(0, hash.index((short) 1));
 }

 // Test index method with non-empty hash and absent key
 @Test
 public void testIndexWithNonEmptyHashAndAbsentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(-1, hash.index((short) 3));
 }

 // Test indexRehashed method with empty hash
 @Test
 public void testIndexRehashedWithEmptyHash() {
 assertEquals(-1, hash.indexRehashed((short) 1, 0, 1, (byte) 0));
 }

 // Test indexRehashed method with non-empty hash and present key
 @Test
 public void testIndexRehashedWithNonEmptyHashAndPresentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(0, hash.indexRehashed((short) 1, 0, 1, (byte) 1));
 }

 // Test indexRehashed method with non-empty hash and absent key
 @Test
 public void testIndexRehashedWithNonEmptyHashAndAbsentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(-1, hash.indexRehashed((short) 3, 0, 1, (byte) 1));
 }

 // Test insertKey method with empty hash
 @Test
 public void testInsertKeyWithEmptyHash() {
 hash.insertKey((short) 1);
 assertEquals((short) 1, hash._set[0]);
 assertEquals(1, hash.size());
 }

 // Test insertKey method with non-empty hash and present key
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithNonEmptyHashAndPresentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKey((short) 1);
 }

 // Test insertKey method with non-empty hash and absent key
 @Test
 public void testInsertKeyWithNonEmptyHashAndAbsentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKey((short) 3);
 assertEquals((short) 3, hash._set[hash.size() - 1]);
 assertEquals(3, hash.size());
 }

 // Test insertKeyRehash method with empty hash
 @Test
 public void testInsertKeyRehashWithEmptyHash() {
 hash.insertKeyRehash((short) 1, 0, 1, (byte) 0);
 assertEquals((short) 1, hash._set[0]);
 assertEquals(1, hash.size());
 }

 // Test insertKeyRehash method with non-empty hash and present key
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithNonEmptyHashAndPresentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKeyRehash((short) 1, 0, 1, (byte) 1);
 }

 // Test insertKeyRehash method with non-empty hash and absent key
 @Test
 public void testInsertKeyRehashWithNonEmptyHashAndAbsentKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKeyRehash((short) 3, 0, 1, (byte) 1);
 assertEquals((short) 3, hash._set[hash.size() - 1]);
 assertEquals(3, hash.size());
 }

 // Test insertKeyAt method with empty hash
 @Test
 public void testInsertKeyAtWithEmptyHash() {
 hash.insertKeyAt(0, (short) 1);
 assertEquals((short) 1, hash._set[0]);
 assertEquals(1, hash.size());
 }

 // Test insertKeyAt method with non-empty hash and present index
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyAtWithNonEmptyHashAndPresentIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKeyAt(0, (short) 1);
 }

 // Test insertKeyAt method with non-empty hash and absent index
 @Test
 public void testInsertKeyAtWithNonEmptyHashAndAbsentIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKeyAt(2, (short) 3);
 assertEquals((short) 3, hash._set[2]);
 assertEquals(3, hash.size());
 }

 // Test insertKeyAt method with non-empty hash and free slot
 @Test
 public void testInsertKeyAtWithNonEmptyHashAndFreeSlot() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertFalse(hash.consumeFreeSlot);
 hash.removeAt(1);
 assertTrue(hash.consumeFreeSlot);
 hash.insertKeyAt(1, (short) 3);
 assertEquals((short) 3, hash._set[1]);
 assertEquals(3, hash.size());
 }
}