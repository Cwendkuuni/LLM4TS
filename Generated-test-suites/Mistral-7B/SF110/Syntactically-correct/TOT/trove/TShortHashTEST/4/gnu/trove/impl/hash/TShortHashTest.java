import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;
 private short[] testValues = {1, 2, 3, 4, 5};
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return hash.contains(value);
 }
 };
 }

 @Test
 public void testDefaultConstructor() {
 // Test default constructor
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 // Test constructor with initial capacity
 hash = new TShortHash(5);
 // Test adding elements
 for (short value : testValues) {
 hash.insert(value);
 assertEquals(value, hash.get(value));
 assertEquals(value, hash.containsKey(value) ? hash.get(value) : hash.getNoEntryValue());
 }
 // Test size
 assertEquals(testValues.length, hash.size());
 // Test contains
 for (short value : testValues) {
 assertTrue(hash.contains(value));
 }
 // Test forEach
 hash.forEach(procedure);
 assertTrue(procedure.isSatisfied());
 }

 @Test
 public void testInitialCapacityLoadFactorConstructor() {
 // Test constructor with initial capacity and load factor
 hash = new TShortHash(5, 0.5f);
 // Test adding elements
 for (short value : testValues) {
 hash.insert(value);
 assertEquals(value, hash.get(value));
 assertEquals(value, hash.containsKey(value) ? hash.get(value) : hash.getNoEntryValue());
 }
 // Test size
 assertEquals(testValues.length, hash.size());
 // Test contains
 for (short value : testValues) {
 assertTrue(hash.contains(value));
 }
 // Test forEach
 hash.forEach(procedure);
 assertTrue(procedure.isSatisfied());
 }

 @Test
 public void testInitialCapacityLoadFactorNoEntryValueConstructor() {
 // Test constructor with initial capacity, load factor, and no entry value
 hash = new TShortHash(5, 0.5f, (short) 10);
 // Test adding elements
 for (short value : testValues) {
 hash.insert(value);
 assertEquals(value, hash.get(value));
 assertEquals(value, hash.containsKey(value) ? hash.get(value) : (short) 10);
 }
 // Test size
 assertEquals(testValues.length, hash.size());
 // Test contains
 for (short value : testValues) {
 assertTrue(hash.contains(value));
 }
 // Test forEach
 hash.forEach(procedure);
 assertTrue(procedure.isSatisfied());
 }

 @Test
 public void testGetNoEntryValue() {
 // Test getNoEntryValue
 hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 hash = new TShortHash(5);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 hash = new TShortHash(5, 0.5f);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 hash = new TShortHash(5, 0.5f, (short) 10);
 assertEquals((short) 10, hash.getNoEntryValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 // Test exception
 hash = new TShortHash(-5);
 fail("Initial capacity should be positive");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 // Test exception
 hash = new TShortHash(5, -0.5f);
 fail("Load factor should be non-negative");
 }

 @Test
 public void testContains() {
 // Test contains
 hash = new TShortHash();
 assertFalse(hash.contains((short) 1));
 // Test adding elements and checking contains
 hash = new TShortHash(5);
 for (short value : testValues) {
 hash.insert(value);
 assertTrue(hash.contains(value));
 }
 // Test non-existing value
 assertFalse(hash.contains((short) 6));
 }

 @Test
 public void testForEach() {
 // Test forEach
 hash = new TShortHash(5);
 for (short value : testValues) {
 hash.insert(value);
 }
 hash.forEach(procedure);
 assertTrue(procedure.isSatisfied());
 // Test empty hash
 hash = new TShortHash();
 hash.forEach(procedure);
 assertFalse(procedure.isSatisfied());
 }

 @Test
 public void testRemoveAt() {
 // Test removeAt
 hash = new TShortHash(5);
 for (short value : testValues) {
 hash.insert(value);
 }
 // Test removing existing value
 hash.removeAt(Arrays.asList(testValues).indexOf(2));
 assertFalse(hash.contains((short) 2));
 // Test removing non-existing value
 hash.removeAt(5);
 assertFalse(hash.contains((short) 6));
 }

 @Test
 public void testIndex() {
 // Test index
 hash = new TShortHash(5);
 for (short value : testValues) {
 hash.insert(value);
 }
 // Test index of existing value
 assertEquals(Arrays.asList(testValues).indexOf(3), hash.index(3));
 // Test index of non-existing value
 assertEquals(-1, hash.index((short) 6));
 }

 @Test
 public void testIndexRehashed() {
 // Test indexRehashed
 hash = new TShortHash(5);
 for (short value : testValues) {
 hash.insert(value);
 }
 // Test indexRehashed of existing value
 assertEquals(Arrays.asList(testValues).indexOf(4), hash.indexRehashed(4, 3, HashFunctions.hash(4), (byte) 1));
 // Test indexRehashed of non-existing value
 assertEquals(-1, hash.indexRehashed((short) 6, 3, HashFunctions.hash(6), (byte) 1));
 }
}