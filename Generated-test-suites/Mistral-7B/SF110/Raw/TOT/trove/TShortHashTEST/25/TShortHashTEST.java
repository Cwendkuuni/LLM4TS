import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertNull(hash.getNoEntryValue());
 assertEquals(0, hash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 1);
 assertEquals(10, hash.size());
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 hash.setNoEntryValue((short) 1);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash hash = new TShortHash(-1);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TShortHash hash = new TShortHash(10, -0.5f);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(0);
 assertEquals((short) 2, hash.get(0));
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test
 public void testRehash() {
 TShortHash hash = new TShortHash(2);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 fail("Should not reach this point");
 return false;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 TShortHash hash = new TShortHash(2, 0.5f);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 }

 @Test
 public void testPutAndGet() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 assertEquals((short) 1, hash.get((short) 1));
 }

 @Test
 public void testPutAndGetNullKey() {
 TShortHash hash = new TShortHash();
 assertNull(hash.get((short) null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutAndGetNullValue() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) null);
 }
}