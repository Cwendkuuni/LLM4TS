import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(11);
 }

 @Test
 public void testTShortHashConstruction() {
 TShortHash hash1 = new TShortHash();
 TShortHash hash2 = new TShortHash(5);
 TShortHash hash3 = new TShortHash(5, 0.75f);
 TShortHash hash4 = new TShortHash(5, 0.75f, (short) 1);
 assertEquals(11, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(5);
 assertEquals((short) 0, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.removeAt(1);
 assertEquals((short) 1, hash.getNoEntryValue());
 assertFalse(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 int index = hash.index((short) 1);
 assertEquals(0, index);
 index = hash.index((short) 2);
 assertEquals(1, index);
 index = hash.index((short) 3);
 assertEquals(2, index);
 index = hash.index((short) 4);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 assertEquals(4, hash.size());
 hash.put((short) 5, (short) 5);
 assertEquals(5, hash.size());
 assertEquals((short) 1, hash.get((short) 1));
 assertEquals((short) 2, hash.get((short) 2));
 assertEquals((short) 3, hash.get((short) 3));
 assertEquals((short) 4, hash.get((short) 4));
 assertEquals((short) 5, hash.get((short) 5));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 }

}