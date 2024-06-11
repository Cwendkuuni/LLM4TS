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

 // Test constructor with default initial capacity and load factor
 @Test
 public void testTShortHashDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 // Test constructor with specified initial capacity and load factor
 @Test
 public void testTShortHashCustomConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.capacity());
 }

 // Test constructor with specified initial capacity, load factor and no_entry_value
 @Test
 public void testTShortHashCustomConstructorWithNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 1);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.capacity());
 }

 // Test contains method
 @Test
 public void testContains() {
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 // Test forEach method with empty hash
 @Test
 public void testForeEachWithEmptyHash() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 return true;
 }
 };
 hash.forEach(procedure);
 assertTrue(procedure.wasExecuted());
 }

 // Test forEach method with non-empty hash
 @Test
 public void testForeEachWithNonEmptyHash() {
 hash.put((short) 1);
 hash.put((short) 2);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 return shortValue == 1;
 }
 };
 hash.forEach(procedure);
 assertTrue(procedure.wasExecuted());
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(0);
 assertFalse(hash.contains((short) 1));
 }

 // Test index method with empty hash
 @Test
 public void testIndexWithEmptyHash() {
 assertEquals(-1, hash.index((short) 1));
 }

 // Test index method with non-empty hash and existing key
 @Test
 public void testIndexWithNonEmptyHashAndExistingKey() {
 hash.put((short) 1);
 assertEquals(0, hash.index((short) 1));
 }

 // Test index method with non-empty hash and non-existing key
 @Test
 public void testIndexWithNonEmptyHashAndNonExistingKey() {
 hash.put((short) 1);
 assertEquals(-1, hash.index((short) 3));
 }

 // Test indexRehashed method with empty hash
 @Test(expected = IllegalStateException.class)
 public void testIndexRehashedWithEmptyHash() {
 hash.indexRehashed((short) 1, -1, 0, (byte) 0);
 }

 // Test indexRehashed method with non-empty hash and existing key
 @Test
 public void testIndexRehashedWithNonEmptyHashAndExistingKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 int index = hash.indexRehashed((short) 1, 0, 0, (byte) 1);
 assertEquals(0, index);
 }

 // Test indexRehashed method with non-empty hash and non-existing key
 @Test(expected = IllegalStateException.class)
 public void testIndexRehashedWithNonEmptyHashAndNonExistingKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.indexRehashed((short) 3, 0, 0, (byte) 1);
 }

 // Test insertKey method
 @Test
 public void testInsertKey() {
 hash.insertKey((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 // Test insertKeyRehash method with empty hash
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithEmptyHash() {
 hash.insertKeyRehash((short) 1, -1, 0, (byte) 0);
 }

 // Test insertKeyRehash method with non-empty hash and existing key
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithNonEmptyHashAndExistingKey() {
 hash.put((short) 1);
 hash.insertKeyRehash((short) 1, 0, 0, (byte) 1);
 }

 // Test insertKeyRehash method with non-empty hash and non-existing key
 @Test
 public void testInsertKeyRehashWithNonEmptyHashAndNonExistingKey
 () {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKeyRehash((short) 3, 0, 0, (byte) 1);
 assertTrue(hash.contains((short) 3));
 }

 // Test insertKeyAt method with empty hash
 @Test
 public void testInsertKeyAtWithEmptyHash() {
 hash.insertKeyAt(0, (short) 1);
 assertTrue(hash.contains((short) 1));
 }

 // Test insertKeyAt method with non-empty hash
 @Test
 public void testInsertKeyAtWithNonEmptyHash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.insertKeyAt(2, (short) 3);
 assertTrue(hash.contains((short) 3));
 }
}