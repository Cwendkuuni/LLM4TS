import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testTShortHashConstructors() {
 // Test default constructor
 TShortHash defaultHash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, defaultHash.getNoEntryValue());
 // Test initial capacity constructor
 TShortHash capacityHash = new TShortHash(3);
 assertEquals(3, capacityHash.size());
 // Test initial capacity and load factor constructor
 TShortHash capacityAndLoadFactorHash = new TShortHash(3, 0.75f);
 assertEquals(3, capacityAndLoadFactorHash.size());
 // Test initial capacity, load factor, and no_entry_value constructor
 TShortHash capacityLoadFactorNoEntryValueHash = new TShortHash(3, 0.75f, (short) 127);
 assertEquals(127, capacityLoadFactorNoEntryValueHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 // Test setUp method
 hash.setUp(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 // Test contains method with existing value
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 // Test contains method with non-existing value
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testForEach() {
 // Test forEach method with empty hash
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return false;
 }
 };
 assertFalse(hash.forEach(procedure));
 // Test forEach method with existing values
 hash.put((short) 1);
 hash.put((short) 2);
 TShortProcedure procedure1 = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure1));
 // Test forEach method with removed value
 hash.remove((short) 1);
 TShortProcedure procedure2 = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 2, value);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure2));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWithFullHash() {
 // Test removeAt method with full hash
 hash.put((short) 1);
 hash.put((short) 2);
 int indexToRemove = 0;
 hash.removeAt(indexToRemove);
 hash.put((short) 3); // This should cause an IllegalStateException
 }

 @Test
 public void testIndex() {
 // Test index method with existing value
 hash.put((short) 1);
 int index = hash.index((short) 1);
 assertEquals(0, index);
 // Test index method with non-existing value
 index = hash.index((short) 2);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 // Test insertKey method with existing value
 hash.put((short) 1);
 short valueToInsert = (short) 1;
 int index = hash.insertKey(valueToInsert);
 assertEquals(0, index);
 // Test insertKey method with new value
 valueToInsert = (short) 2;
 index = hash.insertKey(valueToInsert);
 assertEquals(1, index);
 // Test insertKey method with hash collision
 valueToInsert = (short) 1;
 index = hash.insertKey(valueToInsert);
 assertEquals(0, index); // Should not insert the same value again
 }

 @Test
 public void testInsertKeyRehash() {
 // Test insertKeyRehash method with existing value
 hash.put((short) 1);
 short valueToInsert = (short) 2;
 int index = hash.insertKeyRehash(valueToInsert, 0, 1, (byte) 1);
 assertEquals(-1, index);
 // Test insertKeyRehash method with new value
 valueToInsert = (short) 3;
 index = hash.insertKeyRehash(valueToInsert, 0, 1, (byte) 0);
 assertEquals(1, index);
 // Test insertKeyRehash method with hash collision
 valueToInsert = (short) 2;
 index = hash.insertKeyRehash(valueToInsert, 0, 1, (byte) 1);
 assertEquals(-1, index); // Should not insert the same value again
 }
}