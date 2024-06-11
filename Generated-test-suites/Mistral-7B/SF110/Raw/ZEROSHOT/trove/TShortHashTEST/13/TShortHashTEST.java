import org.junit.After;
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

 @After
 public void tearDown() {
 hash = null;
 }

 @Test
 public void testTShortHashWithDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testTShortHashWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testTShortHashWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(5, 0.75f);
 assertEquals(5, hash.size());
 }

 @Test
 public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 127);
 assertEquals(5, hash.size());
 assertEquals((short) 127, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 int count = 0;
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short arg) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.removeAt(1);
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 5));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 short oldValue = hash.put((short) 1);
 assertEquals((short) 2, oldValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWhenTableIsFull() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 }
}