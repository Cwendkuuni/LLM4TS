import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testPutAndGet() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(2, hash.size());
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertEquals((short) 1, hash.get((short) 1));
 assertEquals((short) 2, hash.get((short) 2));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWhenFull() {
 for (short i = 0; i < 5; i++) {
 hash.put((short) i);
 }
 hash.put((short) 5); // should throw an exception
 }

 @Test
 public void testRemove() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.remove((short) 2);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 3));
 }

 @Test
 public void testForEachProcedure() {
 int count = 0;
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 final TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

}