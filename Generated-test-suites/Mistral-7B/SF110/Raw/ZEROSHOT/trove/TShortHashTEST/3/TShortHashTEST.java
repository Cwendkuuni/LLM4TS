import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

 private TShortHash hash;
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 procedure = new TAdder();
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, hash.size());
 assertFalse(hash.contains((short) 5));
 }

 @Test
 public void testInitialCapacity() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 assertFalse(hash.contains((short) 5));
 }

 @Test
 public void testLoadFactor() {
 hash = new TShortHash(3, 0.5f);
 assertEquals(6, hash.size());
 assertFalse(hash.contains((short) 5));
 }

 @Test
 public void testNoEntryValue() {
 hash = new TShortHash(3, 0.5f, (short) 10);
 assertEquals(6, hash.size());
 assertEquals((short) 10, hash.getNoEntryValue());
 }

 @Test
 public void testConsumeFreeSlot() {
 hash = new TShortHash(3, 0.5f);
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 assertEquals(3, hash.size());
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testContains() {
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 assertTrue(hash.contains((short) 5));
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testForEach() {
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 assertTrue(hash.forEach(procedure));
 assertEquals(15, procedure.sum());
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAt() {
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 hash.removeAt(2);
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 assertEquals(1, hash.index((short) 5));
 assertEquals(-1, hash.index((short) 2));
 }

 @Test
 public void testIndexRehashed() {
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 assertEquals(1, hash.indexRehashed((short) 5, 1, 1, (byte) 1));
 assertEquals(-1, hash.indexRehashed((short) 2, 1, 1, (byte) 1));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 hash.put((short) 20);
 assertEquals(4, hash.size());
 assertFalse(hash.contains((short) 2));
 hash.put((short) 2);
 assertEquals(5, hash.size());
 assertTrue(hash.contains((short) 2));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashLimit() {
 hash.put((short) 5);
 hash.put((short) 10);
 hash.put((short) 15);
 hash.put((short) 20);
 hash.put((short) 21);
 hash.put((short) 22);
 hash.put((short) 23);
 hash.put((short) 24);
 }
}