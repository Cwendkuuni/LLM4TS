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
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityLoadFactorConstructor() {
 hash = new TShortHash(5, 0.75f);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityLoadFactorNoEntryValueConstructor() {
 hash = new TShortHash(5, 0.75f, (short) 10);
 assertEquals(5, hash.size());
 assertEquals((short) 10, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash(5);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 hash.put((short) 1);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testContainsFalse() {
 hash.put((short) 1);
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testForEach() {
 short count = 0;
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithUnsupportedProcedure() {
 hash.put((short) 1);
 hash.forEach(new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 throw new UnsupportedOperationException("Unsupported operation");
 }
 });
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 int index = hash.indexOf((short) 1);
 hash.removeAt(index);
 assertEquals(1, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtNegativeIndex() {
 hash.put((short) 1);
 hash.removeAt(-1);
 }

 @Test
 public void testRemoveAtIndexOutOfBounds() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(hash.size());
 assertEquals(2, hash.size());
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test
 public void testIndexRehashed() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(2, hash.index((short) 3));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 assertEquals(0, hash.index((short) 4));
 short key = (short) 4;
 int index = hash.insertKey(key);
 assertEquals(4, hash.size());
 assertEquals(index, hash.indexOf(key));
 }

 @Test
 public void testInsertKeyAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.insertKeyAt(1, (short) 5);
 assertEquals(4, hash.size());
 assertEquals(1, hash.indexOf((short) 5));
 }

 @Test
 public void testInsertKeyRehash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 short key = (short) 6;
 int index = hash.insertKey(key);
 assertEquals(6, hash.size());
 assertEquals(index, hash.indexOf(key));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashException() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 }
}