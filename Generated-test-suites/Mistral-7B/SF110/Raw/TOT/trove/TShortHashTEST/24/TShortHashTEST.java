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
 public void testDefaultConstructor() {
 assertEquals(5, hash.size());
 assertArrayEquals(new short[5], hash._set);
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hash = new TShortHash(15, 0.75f);
 assertEquals(15, hash.size());
 assertArrayEquals(new short[15], hash._set);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndInitialValueConstructor() {
 hash = new TShortHash(20, 0.75f, (short) 123);
 assertEquals(20, hash.size());
 assertArrayEquals(new short[20], hash._set);
 for (short i : hash._set) {
 assertEquals((short) 123, i);
 }
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash(5);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 assertEquals(10, hash._states.length);
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testContainsEmptyHash() {
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testContainsNullValue() {
 hash.put((short) 1);
 assertFalse(hash.contains((short) null));
 }

 @Test
 public void testForEach() {
 short count = 0;
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test
 public void testForEachEmptyHash() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 fail("Should not be called");
 return false;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 int index = 1;
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertArrayEquals(new short[] { (short) 1, (short) 3 }, hash._set);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(2);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveAtNullIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(null);
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 1);
 assertEquals(1, hash.index((short) 1));
 }

 @Test
 public void testIndexEmptyHash() {
 assertEquals(-1, hash.index((short) 1));
 }

 @Test
 public void testIndexNullValue() {
 hash.put((short) 1);
 assertEqual(-1, hash.index((short) null));
 }

 @Test
 public void testIndexRehashed() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 assertEquals(3, hash.index((short) 3));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertEquals(5, hash.size());
 assertEquals(5, hash._set.length);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFullHash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 }

 @Test(expected = NullPointerException.class)
 public void testInsertKeyNullValue() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) null);
 }
}