import org.junit.Before;
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

 @Test
 public void testEmptyHash() {
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains((short) 1));
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitializationWithInitialCapacity() {
 assertEquals(5, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testInitializationWithInitialCapacityAndLoadFactor() {
 assertEquals(5, hash.size());
 assertFalse(hash.contains((short) 1));
 assertEquals(0.5f, hash.getLoadFactor(), 0.01f);
 }

 @Test
 public void testInitializationWithInitialCapacityLoadFactorAndNoEntryValue() {
 assertEquals(5, hash.size());
 assertFalse(hash.contains((short) 1));
 assertEquals(0.5f, hash.getLoadFactor(), 0.01f);
 assertEquals((short) 0, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(5, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 new TShortHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testZeroInitialCapacity() {
 new TShortHash(0);
 }

 @Test
 public void testContains() {
 hash.put((short) 1, (short) 1);
 assertTrue(hash.contains((short) 1));
 hash.remove((short) 1);
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 hash.put((short) 1, (short) 1);
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 int index = hash.indexOf((short) 1);
 hash.removeAt(index);
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testIndex() {
 hash.put((short) 1, (short) 1);
 int index = hash.indexOf((short) 1);
 assertEquals(index, 0);
 }

 @Test
 public void testIndexRehashed() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 int index = hash.indexOf((short) 2);
 assertEquals(index, 1);
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 assertFalse(hash.put((short) 4, (short) 4));
 assertEquals(4, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 }
}