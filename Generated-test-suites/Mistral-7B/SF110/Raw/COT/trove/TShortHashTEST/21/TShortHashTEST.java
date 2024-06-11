import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 hash = new TShortHash(11);
 }

 @Test
 public void testEmptyConstructor() {
 TShortHash emptyHash = new TShortHash();
 short noEntryValue = emptyHash.getNoEntryValue();
 assertTrue(emptyHash.isEmpty());
 assertEquals(noEntryValue, emptyHash.contains((short) 0));
 }

 @Test
 public void testInitialCapacityConstructor() {
 assertEquals(11, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hashWithLoadFactor = new TShortHash(11, 0.75f);
 assertEquals(8, hashWithLoadFactor.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hashWithNoEntryValue = new TShortHash(11, 0.75f, (short) 1);
 assertEquals(8, hashWithNoEntryValue.size());
 assertEquals((short) 1, hashWithNoEntryValue.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(11);
 short noEntryValue = hash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(11);
 assertEquals(11, hash.capacity());
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertFalse(hash.contains((short) 0));
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

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWithEmptyHash() {
 hash.removeAt(-1);
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 assertSame((short) 2, hash.get(0));
 assertSame((short) 3, hash.get(1));
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 int index = hash.index((short) 2);
 assertEquals(1, index);
 assertNull(hash.get((short) 0));
 }

 @Test
 public void testIndexRehashed() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 int index = hash.index((short) 4);
 assertEquals(4, index);
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 int index = hash.insertKey((short) 7);
 assertEquals(7, index);
 assertEquals(7, hash.get(7));
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
 hash.put((short) 8);
 hash.put((short) 9);
 hash.put((short) 10);
 hash.put((short) 11);
 int index = hash.insertKey((short) 12);
 assertEquals(-1, index);
 }
}