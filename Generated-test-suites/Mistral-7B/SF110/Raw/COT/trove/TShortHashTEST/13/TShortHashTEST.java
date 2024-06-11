import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Test
 public void testTShortHashConstructorWithDefaults() {
 hash = new TShortHash();
 assertNotNull(hash);
 }

 @Test
 public void testTShortHashConstructorWithInitialCapacity() {
 hash = new TShortHash(10);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTShortHashConstructorWithInitialCapacityAndLoadFactor() {
 hash = new TShortHash(10, 0.75f);
 assertNotNull(hash);
 assertEquals(10, hash.capacity());
 assertEquals(7, hash.size());
 }

 @Test
 public void testTShortHashConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 hash = new TShortHash(10, 0.75f, (short) 1);
 assertNotNull(hash);
 assertEquals(10, hash.capacity());
 assertEquals(7, hash.size());
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash();
 hash.setNoEntryValue((short) 1);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 assertEquals((short) 1, shortValue);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 hash = new TShortHash();
 hash.put((short) 1);
 hash.removeAt(0);
 }

 @Test
 public void testIndex() {
 hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(-1, hash.index((short) 3));
 assertEquals(1, hash.index((short) 1));
 assertEquals(2, hash.index((short) 2));
 }

 @Test
 public void testIndexRehashed() {
 hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(-1, hash.indexRehashed((short) 4, 0, 0, (byte) 0));
 assertEquals(1, hash.indexRehashed((short) 1, 0, 0, (byte) 1));
 }

 @Test
 public void testInsertKey() {
 hash = new TShortHash();
 assertTrue(hash.insertKey((short) 1) >= 0);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testInsertKeyAlreadyExists() {
 hash = new TShortHash();
 hash.put((short) 1);
 assertTrue(hash.insertKey((short) 1) < 0);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 hash.put((short) 8);
 hash.put((short) 9);
 hash.put((short) 10);
 assertTrue(hash.insertKey((short) 11) < 0);
 }
}