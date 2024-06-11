import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Test
 public void testTFloatCharHashInitialization() {
 hash = new TFloatCharHash();
 assertEquals(0, hash.size());
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testTFloatCharHashInitializationWithCapacity() {
 hash = new TFloatCharHash(10);
 assertEquals(0, hash.size());
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testTFloatCharHashInitializationWithLoadFactor() {
 hash = new TFloatCharHash(10, 0.75f);
 assertEquals(0, hash.size());
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testTFloatCharHashInitializationWithCapacityAndLoadFactor() {
 hash = new TFloatCharHash(10, 0.75f, 0.1f, '\u0001');
 assertEquals(0, hash.size());
 assertEquals(0.1f, hash.getNoEntryKey());
 assertEquals('\u0001', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(10);
 assertEquals(10, hash.setUp(10));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 hash = new TFloatCharHash(-5);
 }

 @Test
 public void testContains() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 assertTrue(hash.contains(0.1f));
 assertTrue(hash.contains(0.2f));
 assertTrue(hash.contains(0.3f));
 }

 @Test
 public void testDoesNotContain() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 assertFalse(hash.contains(0.4f));
 }

 @Test
 public void testForEach() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 if (key == 0.1f) {
 return false;
 }
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = NullPointerException.class)
 public void testNullProcedure() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 TFloatProcedure procedure = null;
 assertFalse(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 int index = hash.index(0.2f);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(0.2f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtInvalidIndex() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 hash.removeAt(4);
 }

 @Test
 public void testIndex() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 int index = hash.index(0.2f);
 assertEquals(1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeValue() {
 hash = new TFloatCharHash(3);
 hash.insertKey((float)-1);
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TFloatCharHash(3);
 hash.insertKey(0.1f);
 hash.insertKey(0.3f);
 hash.insertKey(0.2f);
 hash.insertKey(0.4f);
 hash.insertKey(0.5f);
 int index = hash.insertKey(0.1f);
 assertEquals(5, hash.size());
 assertEquals(-1, index);
 }
}