import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

 @Test
 public void testEmptyTShortHash() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.size());
 assertFalse(hash.contains((short) 0));
 }

 @Test
 public void testInitialCapacityTShortHash() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorTShortHash() {
 TShortHash hash = new TShortHash(15, 0.75f);
 assertEquals(11, hash.size());
 }

 @Test
 public void testCustomNoEntryValueTShortHash() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 123);
 assertEquals(10, hash.size());
 assertDefaultValue(hash.getNoEntryValue(), (short) 123);
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 assertEquals(5, hash._set.length);
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 1); // duplicate
 assertTrue(hash.contains((short) 1));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 ShortProcedure procedure = new ShortProcedure() {
 @Override
 public boolean execute(short value) {
 return value > 1 && value < 4;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.removeAt(0);
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(1, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertEquals(2, hash.index((short) 3));
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.size());
 hash.put((short) 1);
 assertEquals(1, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertionFailsWhenTableIsFull() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 0);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 }
}