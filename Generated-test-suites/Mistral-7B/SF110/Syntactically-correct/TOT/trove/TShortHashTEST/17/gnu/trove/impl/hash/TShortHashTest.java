import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.size());
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 100);
 assertEquals(10, hash.size());
 assertEquals((short) 100, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash hash = new TShortHash(-5);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals(value, (short) 1);
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
 hash.put((short) 3);
 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertEquals((short) 1, hash.get(0));
 assertEquals((short) 3, hash.get(1));
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 4));
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(-1, hash.indexRehashed((short) 4, 0, 0, (byte) 0));
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 assertEquals(4, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 fail("Expected IllegalStateException");
 }
}