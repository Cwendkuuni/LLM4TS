import org.junit.Test;
import org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testConstructorWithDefaultInitialCapacity() {
 TShortHash hash = new TShortHash();
 // Check if no_entry_value is set correctly
 assertEquals(Short.MIN_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 // Check if initial capacity is set correctly
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(5, 0.75f);
 // Check if initial capacity and load factor are set correctly
 assertEquals(5, hash.size());
 assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 123);
 // Check if initial capacity, load factor and no_entry_value are set correctly
 assertEquals(5, hash.size());
 assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
 assertEquals((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(5);
 hash.setNoEntryValue((short) 456);
 assertEquals((short) 456, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 10);
 assertTrue(hash.contains((short) 10));
 }

 @Test
 public void testContainsDoesNotContain() {
 TShortHash hash = new TShortHash(5);
 assertFalse(hash.contains((short) 20));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(3);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TAdder();
 hash.forEach(procedure);
 assertEquals(6, procedure.getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(3);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.removeAt(1);
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 int index1 = hash.index((short) 2);
 int index2 = hash.index((short) 6);
 assertEquals(1, index1);
 assertEquals(-1, index2);
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash(3);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.resize(5);
 int index1 = hash.indexRehashed((short) 2, -1, -1, (byte) 1);
 int index2 = hash.indexRehashed((short) 6, -1, -1, (byte) 1);
 assertEquals(-1, index1);
 assertEquals(-1, index2);
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 int index = hash.insertKey((short) 10);
 assertEquals(0, index);
 // Check if value is present
 assertTrue(hash.contains((short) 10));
 }

 @Test
 public void testInsertKeyAlreadyPresent() {
 TShortHash hash = new TShortHash(3);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 int index = hash.insertKey((short) 2);
 assertEquals(-1, index);
 // Check if value is still present
 assertTrue(hash.contains((short) 2));
 }

 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(3);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.resize(5);
 int index = hash.insertKey((short) 2);
 assertEquals(-1, index);
 // Check if value is still present
 assertTrue(hash.contains((short) 2));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TShortHash hash = new TShortHash(3);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 int index = hash.insertKey((short) 4);
 }
}