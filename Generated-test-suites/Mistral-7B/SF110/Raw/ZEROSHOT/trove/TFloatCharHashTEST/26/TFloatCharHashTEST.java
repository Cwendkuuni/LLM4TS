import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Test
 public void testConstruction() {
 hash = new TFloatCharHash();
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryKey());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructionWithInitialCapacity() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.capacity());
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryKey());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructionWithInitialCapacityAndLoadFactor() {
 hash = new TFloatCharHash(5, 0.75f);
 assertEquals(5, hash.capacity());
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryKey());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructionWithInitialCapacityLoadFactorAndNoEntryKeyAndValue() {
 hash = new TFloatCharHash(5, 0.75f, 0.5f, 'A');
 assertEquals(5, hash.capacity());
 assertEquals(0, hash.size());
 assertEquals(0.5f, hash.getNoEntryKey());
 assertEquals('A', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 final int initialCapacity = 10;
 hash = new TFloatCharHash(initialCapacity);
 assertEquals(initialCapacity, hash._set.length);
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 'A');
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsWithEmptyHash() {
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 final TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f);
 return true;
 }
 };
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithEmptyHash() {
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.removeAt(0);
 assertEquals(1, hash.size());
 assertFalse(hash.containsKey(1.0f));
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testIndexRehashed() {
 final int initialCapacity = 3;
 hash = new TFloatCharHash(initialCapacity);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 assertEquals(3, hash.size());
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(4.0f));
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 assertEquals(3, hash.size());
 assertEquals(1, hash.index(1.0f));
 assertEquals(2, hash.index(2.0f));
 assertEquals(3, hash.index(3.0f));
 assertTrue(hash.containsKey(1.0f));
 assertTrue(hash.containsKey(2.0f));
 assertTrue(hash.containsKey(3.0f));
 }

 @Test
 public void testXinsertKey() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.put(4.0f, 'D');
 assertEquals(4, hash.size());
 assertEquals(1, hash.index(1.0f));
 assertEquals(2, hash.index(2.0f));
 assertEquals(3, hash.index(3.0f));
 assertEquals(4, hash.index(4.0f));
 assertTrue(hash.containsKey(1.0f));
 assertTrue(hash.containsKey(2.0f));
 assertTrue(hash.containsKey(3.0f));
 assertTrue(hash.containsKey(4.0f));
 }
}