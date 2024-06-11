import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash floatHash;

 @Before
 public void setUp() {
 floatHash = new TFloatCharHash(5);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 TFloatCharHash emptyHash = new TFloatCharHash();
 assertEquals(0, emptyHash.size());
 // Test constructor with initial capacity
 TFloatCharHash hashWithCapacity = new TFloatCharHash(10);
 assertEquals(10, hashWithCapacity.capacity());
 // Test constructor with initial capacity and load factor
 TFloatCharHash hashWithInitialCapacityAndLoadFactor = new TFloatCharHash(5, 0.75f);
 assertEquals(5, hashWithInitialCapacityAndLoadFactor.capacity());
 // Test constructor with initial capacity, load factor, no entry key, and no entry value
 TFloatCharHash hashWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue = new TFloatCharHash(10, 0.75f, 1.0f, 'X');
 assertEquals(10, hashWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue.capacity());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertEquals(5, hash.setUp(5));
 }

 @Test
 public void testContains() {
 // Test empty hash
 TFloatCharHash emptyHash = new TFloatCharHash();
 assertFalse(emptyHash.contains(1.0f));
 // Test hash with no entry
 TFloatCharHash hashWithNoEntry = new TFloatCharHash();
 hashWithNoEntry.insert(1.0f, 'A');
 assertFalse(hashWithNoEntry.contains(1.0f));
 // Test hash with entry
 TFloatCharHash hashWithEntry = new TFloatCharHash();
 hashWithEntry.insert(1.0f, 'A');
 assertTrue(hashWithEntry.contains(1.0f));
 // Test hash with duplicate entry
 TFloatCharHash hashWithDuplicateEntry = new TFloatCharHash();
 hashWithDuplicateEntry.insert(1.0f, 'A');
 hashWithDuplicateEntry.insert(1.0f, 'B');
 assertTrue(hashWithDuplicateEntry.contains(1.0f));
 }

 @Test
 public void testForEach() {
 // Test empty hash
 TFloatCharHash emptyHash = new TFloatCharHash();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 fail("Should not call execute method for empty hash");
 return false;
 }
 };
 assertFalse(emptyHash.forEach(procedure));
 // Test hash with entry
 TFloatCharHash hashWithEntry = new TFloatCharHash();
 hashWithEntry.insert(1.0f, 'A');
 TFloatProcedure procedure1 = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(1.0f, value);
 return true;
 }
 };
 assertTrue(hashWithEntry.forEach(procedure1));
 // Test hash with multiple entries
 TFloatCharHash hashWithMultipleEntries = new TFloatCharHash();
 hashWithMultipleEntries.insert(1.0f, 'A');
 hashWithMultipleEntries.insert(2.0f, 'B');
 TFloatProcedure procedure2 = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 fail("Should not call execute method for hash with multiple entries");
 return false;
 }
 };
 assertFalse(hashWithMultipleEntries.forEach(procedure2));
 }

 @Test
 public void testRemoveAt() {
 // Test empty hash
 TFloatCharHash emptyHash = new TFloatCharHash();
 assertFalse(emptyHash.contains(1.0f));
 emptyHash.removeAt(-1);
 // Test hash with no entry
 TFloatCharHash hashWithNoEntry = new TFloatCharHash();
 hashWithNoEntry.insert(1.0f, 'A');
 hashWithNoEntry.removeAt(-1);
 assertEquals(0, hashWithNoEntry.size());
 // Test hash with entry
 TFloatCharHash hashWithEntry = new TFloatCharHash();
 hashWithEntry.insert(1.0f, 'A');
 hashWithEntry.insert(2.0f, 'B');
 hashWithEntry.removeAt(0);
 assertEquals(1, hashWithEntry.size());
 assertEquals(2.0f, hashWithEntry.get(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testIndex() {
 // Test empty hash
 TFloatCharHash emptyHash = new TFloatCharHash();
 int index = emptyHash.index(1.0f);
 assertEquals(-1, index);
 // Test hash with no entry for given key
 TFloatCharHash hashWithNoEntry = new TFloatCharHash();
 hashWithNoEntry.insert(1.0f, 'A');
 index = hashWithNoEntry.index(2.0f);
 assertEquals(-1, index);
 // Test hash with entry for given key
 TFloatCharHash hashWithEntry = new TFloatCharHash();
 hashWithEntry.insert(1.0f, 'A');
 hashWithEntry.insert(2.0f, 'B');
 index = hashWithEntry.index(1.0f);
 assertEquals(0, index);
 // Test hash with duplicate entry for given key
 TFloatCharHash hashWithDuplicateEntry = new TFloatCharHash();
 hashWithDuplicateEntry.insert(1.0f, 'A');
 hashWithDuplicateEntry.insert(1.0f, 'B');
 index = hashWithDuplicateEntry.index(1.0f);
 assertEquals(0, index);
 // Test hash with entry for given key after rehash
 TFloatCharHash hashWithRehash = new TFloatCharHash();
 hashWithRehash.insert(1.0f, 'A');
 hashWithRehash.insert(2.0f, 'B');
 hashWithRehash.insert(3.0f, 'C');
 index = hashWithRehash.index(1.0f);
 assertEquals(0, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKey() {
 // Test hash with no free or removed slots available
 TFloatCharHash hash = new TFloatCharHash();
 hash.insert(1.0f);
 hash.insert(2.0f);
 hash.insert(3.0f);
 hash.insert(4.0f);
 try {
 hash.insert(5.0f);
 fail("Should throw IllegalStateException for hash with no free or removed slots available");
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}