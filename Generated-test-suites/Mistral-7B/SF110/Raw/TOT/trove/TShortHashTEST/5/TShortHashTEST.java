import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(11, 0.75f);
 }

 @Test
 public void testInitialization() {
 assertNotNull(hash);
 assertEquals(11, hash.capacity());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Short.MIN_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testContainsNull() {
 assertFalse(hash.contains(null));
 }

 @Test
 public void testContainsNonExistent() {
 assertFalse(hash.contains((short) 0));
 }

 @Test
 public void testPutAndGet() {
 hash.put((short) 1, (short) 1);
 assertEquals((short) 1, hash.get((short) 1));
 }

 @Test
 public void testPutNullKey() {
 assertNull(hash.get((short) null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 hash.put((short) 1, (short) null);
 }

 @Test
 public void testPutExists() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 1, (short) 2);
 assertEquals((short) 2, hash.get((short) 1));
 }

 @Test
 public void testPutLoadFactor() {
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 hash.put((short) 1);
 assertFalse(hash.put((short) 1, (short) 1));
 }

 @Test
 public void testForEach() {
 short count = 0;
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(21, count);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachConcurrentModification() {
 short count = 0;
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 count++;
 hash.remove((short) value);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemove() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.remove((short) 1);
 assertFalse(hash.contains((short) 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistent() {
 hash.remove((short) 1);
 }

 @Test
 public void testRemoveAll() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.removeAll((short) 1);
 assertFalse(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 }

 @Test
 public void testClear() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.clear();
 assertFalse(hash.contains((short) 1));
 assertFalse(hash.contains((short) 2));
 }

}