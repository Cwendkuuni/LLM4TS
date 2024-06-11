import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setup() {
 hash = new TShortHash(5);
 }

 // Test constructor with default arguments
 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertNotNull(hash);
 equal(hash.getNoEntryValue(), Short.MIN_VALUE);
 }

 // Test constructor with initial capacity
 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(10);
 assertNotNull(hash);
 equal(hash.getNoEntryValue(), Short.MIN_VALUE);
 equal(hash.size(), 0);
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(15, 0.75f);
 assertNotNull(hash);
 equal(hash.getNoEntryValue(), Short.MIN_VALUE);
 equal(hash.size(), 0);
 }

 // Test constructor with initial capacity, load factor, and no entry value
 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(20, 0.75f, (short) 1);
 assertNotNull(hash);
 equal(hash.getNoEntryValue(), (short) 1);
 equal(hash.size(), 0);
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 hash = new TShortHash(5);
 equal(hash.size(), 0);
 int capacity = hash.setUp(10);
 equal(capacity, 10);
 assertArrayEquals(new short[10], hash._set);
 }

 // Test contains method
 @Test
 public void testContains() {
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 assertFalse(hash.contains((short) 2));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 hash.put((short) 1);
 hash.put((short) 2);
 final short[] expected = new short[]{1, 2};
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 assertEquals(expected[0], short1);
 expected --;
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(0);
 assertFalse(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 }

 // Test index method with no entry
 @Test
 public void testIndexWithNoEntry() {
 assertEquals(-1, hash.index((short) 3));
 }

 // Test index method with existing entry
 @Test
 public void testIndexWithExistingEntry() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 // Test indexRehashed method with no entry
 @Test
 public void testIndexRehashedWithNoEntry() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(-1, hash.indexRehashed((short) 3, 0, 1, (byte) 0));
 }

 // Test indexRehashed method with existing entry
 @Test
 public void testIndexRehashedWithExistingEntry() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(1, hash.indexRehashed((short) 2, 0, 1, (byte) 0));
 }

 // Test insertKey method with no entry
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithNoEntry() {
 hash.insertKey((short) 1);
 }

 // Test insertKey method with existing entry
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithExistingEntry() {
 hash.put((short) 1);
 hash.insertKey((short) 1);
 }

 // Test insertKeyRehash method with no entry
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithNoEntry() {
 hash.insertKeyRehash((short) 1, 0, 1, (byte) 0);
 }

 // Test insertKeyRehash method with occupied slot
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithOccupiedSlot() {
 hash.put((short) 1);
 hash.insertKeyRehash((short) 1, 0, 1, (byte) 1);
 }

 // Test insertKeyRehash method with chaining collision
 @Test
 public void testInsertKeyRehashWithChainingCollision() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKeyRehash((short) 1, 1, 1, (byte) 1);
 assertEquals(1, hash.index((short) 1));
 }

 // Test insertKeyAt method with no entry
 @Test
 public void testInsertKeyAtWithNoEntry() {
 hash.insertKeyAt(0, (short) 1);
 assertEquals(1, hash.size());
 }

 // Test insertKeyAt method with occupied slot
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyAtWithOccupiedSlot() {
 hash.put((short) 1);
 hash.insertKeyAt(0, (short) 2);
 }
}