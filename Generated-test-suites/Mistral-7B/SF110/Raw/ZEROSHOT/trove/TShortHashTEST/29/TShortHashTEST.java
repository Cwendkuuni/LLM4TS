import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;
import java.util.Arrays;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertFalse(hash.contains((short) 0));
 assertFalse(hash.contains((short) 1));
 assertEquals(0, hash.size());
 }

 @Test
 public void testBasicFunctionality() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 hash.put((short) 5, (short) 5);

 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));

 assertEquals(5, hash.size());
 }

 @Test
 public void testPutWithSameKey() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 1, (short) 2);

 assertEquals(1, hash.size());
 assertTrue(hash.containsKey((short) 1));
 assertNotSame((short) 1, hash.get((short) 1));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWithFullHash() {
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 hash.put(i, i);
 }
 }

 @Test
 public void testRemove() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 hash.put((short) 5, (short) 5);

 hash.remove((short) 3);
 assertEquals(4, hash.size());
 assertFalse(hash.containsKey((short) 3));
 }

 @Test
 public void testForEach() {
 short[] expected = new short[5];
 int index = 0;
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 expected[index++] = value;
 return true;
 }
 };
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 hash.put((short) 5, (short) 5);
 hash.forEach(procedure);
 assertArrayEquals(new short[]{(short) 1, (short) 2, (short) 3, (short) 4, (short) 5}, expected);
 }

}