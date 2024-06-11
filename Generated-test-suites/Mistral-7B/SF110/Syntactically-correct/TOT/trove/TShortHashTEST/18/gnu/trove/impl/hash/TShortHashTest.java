import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains((short) 0));
 }

 @Test
 public void testPutGet() {
 hash.put((short) 1);
 assertEquals((short) 1, hash.get((short) 1));
 }

 @Test
 public void testPutMultipleValues() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);

 assertEquals((short) 1, hash.get((short) 1));
 assertEquals((short) 2, hash.get((short) 2));
 assertEquals((short) 3, hash.get((short) 3));
 }

 @Test
 public void testPutSameValueTwice() {
 hash.put((short) 1);
 hash.put((short) 1);

 assertEquals((short) 1, hash.get((short) 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCapacity() {
 hash = new TShortHash(-1);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hash = new TShortHash(5, -0.5f);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullNoEntryValue() {
 hash = new TShortHash(5, null);
 fail("Expected NullPointerException");
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
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test
 public void testRemove() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.remove((short) 2);
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testRemoveNonExistentValue() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.remove((short) 4);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertFalse(hash.contains((short) 4));
 }

 @Test
 public void testResize() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);

 assertEquals(6, hash.size());
 }
}