import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

 private TShortHash shortHash;
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 shortHash = new TShortHash(3);
 procedure = new TShortAdder();
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, shortHash.size());
 assertFalse(shortHash.contains((short) 1));
 }

 @Test
 public void testPutGet() {
 shortHash.put((short) 1);
 assertEquals(1, shortHash.size());
 assertTrue(shortHash.contains((short) 1));
 assertEquals((short) 1, shortHash.get((short) 1));
 }

 @Test
 public void testPutMultipleValues() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 assertEquals(2, shortHash.size());
 assertTrue(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 }

 @Test
 public void testPutSameValue() {
 shortHash.put((short) 1);
 shortHash.put((short) 1);
 assertEquals(1, shortHash.size());
 assertTrue(shortHash.contains((short) 1));
 }

 @Test
 public void testPutNegativeValue() {
 shortHash.put((short) -1);
 assertEquals(1, shortHash.size());
 assertTrue(shortHash.contains((short) -1));
 }

 @Test
 public void testPutNullValue() {
 try {
 shortHash.put((short) null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testRemove() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.remove((short) 1);
 assertEquals(1, shortHash.size());
 assertFalse(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 }

 @Test
 public void testRemoveNonExistentValue() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.remove((short) 3);
 assertEquals(2, shortHash.size());
 assertFalse(shortHash.contains((short) 3));
 }

 @Test
 public void testPutLoadFactorReached() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);
 shortHash.put((short) 4);
 shortHash.put((short) 5);
 procedure.applyAll(shortHash.values());
 assertEquals(5, shortHash.size());
 assertTrue(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 assertTrue(shortHash.contains((short) 3));
 assertTrue(shortHash.contains((short) 4));
 assertTrue(shortHash.contains((short) 5));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWhenFull() {
 shortHash = new TShortHash(3);
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 shortHash.put((short) i);
 }
 }

 @Test
 public void testForEach() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);
 short sum = 0;
 shortHash.forEach(procedure);
 assertEquals(6, sum);
 }
}