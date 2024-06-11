import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash shortHash;
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 shortHash = new TShortHash(5);
 procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return true;
 }
 };
 }

 @Test
 public void testEmptyHash() {
 assertTrue(shortHash.isEmpty());
 assertFalse(shortHash.contains((short) 1));
 assertEquals(0, shortHash.size());
 }

 @Test
 public void testBasicFunctions() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);

 assertTrue(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 assertTrue(shortHash.contains((short) 3));
 assertEquals(3, shortHash.size());

 assertFalse(shortHash.contains((short) 0));
 assertFalse(shortHash.contains((short) 4));

 shortHash.remove((short) 1);
 shortHash.remove((short) 2);
 shortHash.remove((short) 3);

 assertFalse(shortHash.contains((short) 1));
 assertFalse(shortHash.contains((short) 2));
 assertFalse(shortHash.contains((short) 3));
 assertEquals(0, shortHash.size());
 }

 @Test
 public void testLoadFactor() {
 shortHash = new TShortHash(3, 0.5f);
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);

 assertTrue(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 assertTrue(shortHash.contains((short) 3));
 assertEquals(3, shortHash.size());

 shortHash.put((short) 4);

 assertTrue(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 assertTrue(shortHash.contains((short) 3));
 assertTrue(shortHash.contains((short) 4));
 assertEquals(4, shortHash.size());

 assertFalse(shortHash.isEmpty());
 }

 @Test
 public void testCustomNoEntryValue() {
 shortHash = new TShortHash(5, -1);;
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);

 assertTrue(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 assertTrue(shortHash.contains((short) 3));
 assertEquals(3, shortHash.size());

 assertFalse(shortHash.contains((short) 0));

 shortHash.remove((short) 1);
 shortHash.remove((short) 2);
 shortHash.remove((short) 3);

 assertFalse(shortHash.contains((short) 1));
 assertFalse(shortHash.contains((short) 2));
 assertFalse(shortHash.contains((short) 3));
 assertEquals(0, shortHash.size());

 shortHash.put((short) -1);
 shortHash.put((short) -1);
 shortHash.put((short) -1);
 shortHash.put((short) -1);
 shortHash.put((short) -1);

 assertEquals(-1, shortHash.getNoEntryValue());
 assertEquals(5, shortHash.size());
 }

 @Test
 public void testForEachProcedure() {
 shortHash = new TShortHash(5);
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);

 assertTrue(shortHash.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 shortHash = new TShortHash(5);
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 shortHash.put((short) i);
 }
 }
}