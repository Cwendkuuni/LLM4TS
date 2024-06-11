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
 public void testEmptyConstructor() {
 assertEquals(0, hash.size());
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(2, hash.size());
 assertFalse(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hash = new TShortHash(5, 0.75f);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 assertFalse(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 hash = new TShortHash(5, 0.75f, (short) 123);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 assertFalse(hash.isEmpty());
 assertSame((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash();
 hash.setNoEntryValue((short) 456);
 assertEquals((short) 456, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(5);
 assertEquals(5, hash.capacity());
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 }

 @Test
 public void testContainsNegative() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertFalse(hash.contains((short) 3));
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
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 int index = 1;
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtNegativeIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(1, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 4));
 }

 @Test(expected = IllegalStateException.class)
 public void testIndexWithEmptyHash() {
 hash.index((short) 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testIndexRehashed() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 assertEquals(-1, hash.index((short) 8));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyNegativeIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.insertKeyAt(-1, (short) 4);
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 short value = (short) 4;
 int index = hash.insertKey(value);
 assertEquals(4, hash.size());
 assertSame(value, hash.get(index));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNullValue() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 short[] values = new short[0];
 hash.insertKey(values);
 }

 @Test
 public void testInsertKeyRehash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 short value = (short) 8;
 int index = hash.insertKey(value);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyFull() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 hash.put((short) 8);
 hash.put((short) 9);
 short value = (short) 10;
 int index = hash.insertKey(value);
 assertEquals(-1, index);
 }

}